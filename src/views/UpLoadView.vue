<template>
  <div class="upload-view">
    <VideoBar />
    <div class="upload-container">
      <!-- 左侧导航 -->
      <DataNav currentPage="upload" />

      <!-- 主内容区 -->
      <div class="upload-main">
        <!-- 上传区域 -->
        <VideoUploadArea v-if="!selectedFile" @file-selected="handleFileFromChild" />

        <!-- 视频信息表单 -->
        <VideoInfoForm
          v-else
          :videoInfo="videoInfo"
          :tagInput="tagInput"
          @reset-upload="resetUpload"
          @save-draft="saveDraft"
          @submit-video="submitVideo"
          @add-tag="handleAddTag"
          @remove-tag="removeTag"
          @remove-cover="removeCover"
          @handle-cover-select="handleCoverSelect"
          @update:tagInput="(val) => (tagInput = val)"
          @update-video-info="updateVideoInfo"
          @fetch-danmaku="fetchBilibiliDanmaku"
        />
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import DataNav from "@/components/navBar/DataNav.vue";
import { uploadApi } from "@/api/upload";
import { mapGetters } from "vuex";
import VideoUploadArea from "@/components/upload/videoUploadArea.vue";
import VideoInfoForm from "@/components/upload/VideoInfoForm.vue";
import { createTag, getAllTags, addTagToVideo } from "@/api/tag";
import { danmakuApi } from "@/api/danmaku";
export default {
  name: "UpLoadView",
  components: {
    VideoBar,
    DataNav,
    VideoUploadArea,
    VideoInfoForm,
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  data() {
    return {
      selectedFile: null,
      videoUrl: null,
      tagInput: "",
      uploadProgress: 0,
      videoInfo: {
        title: "",
        category: "",
        description: "",
        tags: [],
        cover: null,
        publishType: "now",
        scheduleTime: null,
        duration: 0,
        cid: "",
        danmakuFileName: "",
      },
      durationReady: false,
    };
  },
  methods: {
    extractFileNameFromUrl(url) {
      try {
        const decoded = decodeURIComponent(url);
        return decoded.substring(decoded.lastIndexOf("/") + 1);
      } catch (e) {
        console.warn("无法解析URL，返回原值");
        return url;
      }
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    triggerCoverInput() {
      this.$refs.coverInput.click();
    },
    handleDrop(e) {
      const file = e.dataTransfer.files[0];
      if (file && file.type.startsWith("video/")) {
        this.getVideoDuration(file);
      }
    },
    onFileSelect(e) {
      const file = e.target.files[0];
      if (file) {
        this.getVideoDuration(file);
      }
    },
    getVideoDuration(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const video = document.createElement("video");
        video.preload = "metadata";
        video.src = e.target.result;
        video.onloadedmetadata = () => {
          const duration = video.duration;
          this.$emit("file-selected", { file, duration });
        };
      };
      reader.readAsDataURL(file);
    },
    handleCoverSelect(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.videoInfo.cover = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    removeCover() {
      this.videoInfo.cover = null;
    },
    async addTag() {
      if (this.tagInput && this.videoInfo.tags.length < 10) {
        const tag = {
          name: this.tagInput,
          videoId: this.videoInfo.id,
        };
        try {
          const response = await createTag(tag);
          if (response.status === 200) {
            this.videoInfo.tags.push(this.tagInput);
            this.tagInput = "";
            console.log("添加标签成功:", this.videoInfo.tags);
          }
        } catch (error) {
          console.error("添加标签失败:", error);
        }
      }
    },
    removeTag(index) {
      this.videoInfo.tags.splice(index, 1);
    },
    resetUpload() {
      this.selectedFile = null;
      this.videoUrl = null;
      this.videoInfo = {
        title: "",
        category: "",
        description: "",
        tags: [],
        cover: null,
        publishType: "now",
        scheduleTime: null,
        duration: 0,
        cid: "",
        danmakuFileName: "",
      };
    },
    async submitVideo() {
      if (!this.videoInfo.title.trim()) {
        alert("请输入视频标题");
        return;
      }
      if (!this.videoInfo.category) {
        alert("请选择视频分区");
        return;
      }
      if (!this.selectedFile) {
        alert("请选择要上传的视频文件");
        return;
      }

      const token = localStorage.getItem("token");
      if (!token) {
        alert("请先登录");
        this.$router.push("/login");
        return;
      }

      try {
        const videoFormData = new FormData();
        videoFormData.append("file", this.selectedFile);
        console.log("准备上传视频文件:", {
          fileName: this.selectedFile.name,
          fileType: this.selectedFile.type,
          fileSize: this.selectedFile.size,
        });

        const videoResponse = await uploadApi.uploadToMinIO(videoFormData, (progress) => {
          this.uploadProgress = progress;
          console.log(`视频上传进度: ${progress}%`);
        });

        console.log("视频上传响应:", videoResponse.data);

        if (videoResponse.status !== 200) {
          throw new Error("视频上传失败");
        }

        // 重置进度条
        this.uploadProgress = 0;

        // 使用URL对象解析视频文件路径
        let videoFileName = "";
        try {
          const videoUrl = videoResponse.data;
          console.log("视频上传返回数据:", videoUrl);
          console.log("视频上传返回数据类型:", typeof videoUrl);

          if (typeof videoUrl === "object" && videoUrl !== null) {
            // 后端返回了结构化的响应对象
            const { fileName } = videoUrl;
            const extractedFileName = this.extractFileNameFromUrl(fileName);
            videoFileName = extractedFileName;
            console.log("视频文件名:", videoFileName);
          } else {
            console.error("视频上传返回数据格式不正确:", videoUrl);
            throw new Error("视频上传返回数据格式不正确");
          }

          if (!videoFileName || videoFileName === "undefined") {
            console.error("无效的文件名:", videoFileName);
            throw new Error("无法获取有效的视频文件名");
          }

          console.log("视频上传成功，文件名:", videoFileName);
        } catch (error) {
          console.error("解析视频URL失败:", error);
          console.error("返回的数据:", videoResponse.data);
          throw new Error("视频URL解析失败");
        }

        // 生成随机文件名
        const generateRandomFileName = (prefix) => {
          const timestamp = new Date().getTime();
          const randomStr = Math.random().toString(36).substring(2, 8);
          return `${prefix}_${timestamp}_${randomStr}.jpg`;
        };

        let coverFileName = generateRandomFileName("default_cover");
        if (this.videoInfo.cover) {
          const coverFormData = new FormData();
          // 将Base64图片转换为Blob
          const coverBlob = await fetch(this.videoInfo.cover).then((r) => r.blob());
          // 生成唯一的文件名
          coverFileName = generateRandomFileName("cover");
          const coverFile = new File([coverBlob], coverFileName, {
            type: coverBlob.type,
          });
          coverFormData.append("file", coverFile);

          const coverResponse = await uploadApi.uploadToMinIO(coverFormData);

          console.log("封面上传响应:", coverResponse.data);

          if (coverResponse.status === 200) {
            try {
              const coverUrl = coverResponse.data;
              console.log("封面上传返回数据:", coverUrl);
              console.log("封面上传返回数据类型:", typeof coverUrl);

              if (typeof coverUrl === "object" && coverUrl !== null) {
                // 后端返回了结构化的响应对象
                const { fileName: coverFileNameRaw } = coverUrl;
                const extractedCoverFileName = this.extractFileNameFromUrl(
                  coverFileNameRaw
                );
                coverFileName = extractedCoverFileName;
                console.log("封面文件名:", coverFileName);
              } else {
                console.error("封面上传返回数据格式不正确:", coverUrl);
                coverFileName = generateRandomFileName("default_cover");
              }

              if (!coverFileName || coverFileName === "undefined") {
                console.error("无效的封面文件名:", coverFileName);
                coverFileName = generateRandomFileName("default_cover");
              }

              console.log("封面上传成功，文件名:", coverFileName);
            } catch (error) {
              console.error("解析封面URL失败:", error);
              coverFileName = generateRandomFileName("default_cover");
            }
          }
        }

        // 3. 提交视频信息
        const formDataSubmit = new FormData();
        formDataSubmit.append("title", this.videoInfo.title);
        formDataSubmit.append("videoFileName", videoFileName);
        formDataSubmit.append("coverFileName", coverFileName);
        formDataSubmit.append("description", this.videoInfo.description || "");
        formDataSubmit.append("authorId", Number(this.userInfo?.id));
        formDataSubmit.append("avatar", this.userInfo?.avatar || "");
        formDataSubmit.append("duration", String(this.videoInfo.duration));
        // 添加弹幕文件名，如果没有则使用空字符串
        formDataSubmit.append("danmakuFileName", this.videoInfo.danmakuFileName || "");

        console.log("提交的表单数据:", {
          title: this.videoInfo.title,
          videoFileName,
          coverFileName,
          danmakuFileName: this.videoInfo.danmakuFileName || "",
          description: this.videoInfo.description,
          authorId: this.userInfo?.id,
          duration: this.videoInfo.duration,
        });

        const submitResponse = await uploadApi.submitVideo(formDataSubmit);
        const videoId = submitResponse.data.videoId;

        if (submitResponse.status === 200) {
          await this.handleTagsAfterUpload(videoId, this.videoInfo.tags);
          alert("视频投稿成功！");
          this.resetUpload();
          //this.$router.push("/");
        } else {
          throw new Error(submitResponse.data || "提交失败");
        }
      } catch (error) {
        console.error("投稿失败:", error);
        if (error.code === "ECONNABORTED") {
          alert("请求超时，请检查网络连接");
        } else if (error.response) {
          alert(`服务器响应错误: ${error.response.data || error.message}`);
        } else if (error.request) {
          alert("无法连接到服务器，请确保后端服务已启动");
        } else {
          alert(`请求错误: ${error.message}`);
        }
      }
    },
    handleFileFromChild({ file, duration }) {
      this.selectedFile = file;
      this.videoInfo.duration = duration;
      this.durationReady = true;
      const reader = new FileReader();
      reader.onload = (e) => {
        this.videoUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    updateVideoInfo(newInfo) {
      this.videoInfo = newInfo;
    },
    handleAddTag() {
      const tag = this.tagInput.trim();
      if (tag && !this.videoInfo.tags.includes(tag) && this.videoInfo.tags.length < 10) {
        this.videoInfo.tags.push(tag);
        this.tagInput = "";
      }
    },
    async handleTagsAfterUpload(videoId, tags) {
      const allTagsRes = await getAllTags();
      console.log("所有标签:", allTagsRes.data);
      const allTags = allTagsRes.data;
      for (const tagName of tags) {
        let tagObj = allTags.find((t) => t.name === tagName);
        if (!tagObj) {
          const createRes = await createTag({ name: tagName });
          tagObj = createRes.data;
        }
        console.log("添加标签", videoId, tagObj.id);
        await addTagToVideo(videoId, Number(tagObj.id));
      }
    },
    // 获取B站弹幕
    async fetchBilibiliDanmaku() {
      if (!this.videoInfo.cid) {
        this.$message.warning("请先输入视频CID");
        return;
      }
      try {
        const response = await danmakuApi.getBilibiliDanmaku(this.videoInfo.cid);
        if (response.data) {
          // 将XML数据转换为Blob
          const xmlBlob = new Blob([response.data], {
            type: "application/xml;charset=UTF-8",
          });
          const xmlFile = new File([xmlBlob], `danmaku_${this.videoInfo.cid}.xml`, {
            type: "application/xml;charset=UTF-8",
          });

          // 创建FormData并上传到MinIO
          const formData = new FormData();
          formData.append("file", xmlFile);

          const uploadResponse = await uploadApi.uploadToMinIO(formData);
          if (uploadResponse.status === 200) {
            this.$message.success("弹幕获取并上传成功");
            // 保存弹幕文件名到videoInfo
            const danmakuFileName = this.extractFileNameFromUrl(
              uploadResponse.data.fileName
            );
            this.videoInfo.danmakuFileName = danmakuFileName;
            console.log("弹幕文件名:", danmakuFileName);
          }
        }
      } catch (error) {
        console.error("获取或上传弹幕失败:", error);
        this.$message.error(
          "获取或上传弹幕失败: " + (error.response?.data || error.message)
        );
      }
    },
  },
};
</script>

<style scoped>
.upload-main {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}
.upload-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.upload-container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 20px;
}
</style>
