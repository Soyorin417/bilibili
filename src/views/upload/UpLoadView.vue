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
import { mapGetters } from "vuex";
import VideoUploadArea from "@/components/upload/videoUploadArea.vue";
import VideoInfoForm from "@/components/upload/VideoInfoForm.vue";
import { createTag, getAllTags, addTagToVideo } from "@/api/tag";
import { danmakuApi } from "@/api/content/danmaku";
import UploadUtils from "@/utils/uploadUtils";

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
        // 1. 上传视频文件
        const videoFileName = await UploadUtils.uploadVideo(
          this.selectedFile,
          (progress) => {
            this.uploadProgress = progress;
            console.log(`视频上传进度: ${progress}%`);
          }
        );

        // 重置进度条
        this.uploadProgress = 0;

        // 2. 上传封面
        const coverFileName = await UploadUtils.uploadCover(this.videoInfo.cover);

        // 3. 提交视频信息
        const videoInfo = {
          title: this.videoInfo.title,
          videoFileName,
          coverFileName,
          description: this.videoInfo.description || "",
          duration: this.videoInfo.duration,
          danmakuFileName: this.videoInfo.danmakuFileName || "",
        };

        const submitResponse = await UploadUtils.submitVideoInfo(
          videoInfo,
          this.userInfo
        );
        const videoId = submitResponse.videoId;

        if (videoId) {
          await this.handleTagsAfterUpload(videoId, this.videoInfo.tags);
          alert("视频投稿成功！");
          this.resetUpload();
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
          const danmakuFileName = await UploadUtils.uploadDanmaku(
            response.data,
            this.videoInfo.cid
          );
          this.$message.success("弹幕获取并上传成功");
          this.videoInfo.danmakuFileName = danmakuFileName;
          console.log("弹幕文件名:", danmakuFileName);
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
