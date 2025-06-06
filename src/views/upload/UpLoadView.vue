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
          @reset-upload="resetUpload"
          @submit-video="submitVideo"
          @remove-cover="removeCover"
          @update-video-info="updateVideoInfo"
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
import { danmakuApi } from "@/api/content/danmaku";
import { videoApi } from "@/api/content/video";

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
      isUploading: false,
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
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    triggerCoverInput() {
      this.$refs.coverInput.click();
    },
    removeCover() {
      this.videoInfo.cover = null;
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
      if (!this.selectedFile) {
        this.$message.error("请先上传视频文件");
        return;
      }

      if (!this.videoInfo.title) {
        this.$message.error("请输入视频标题");
        return;
      }

      if (!this.userInfo || !this.userInfo.id) {
        this.$message.error("请先登录");
        return;
      }

      this.isUploading = true;
      this.uploadProgress = 0;

      try {
        // 构建视频信息
        const formData = new FormData();
        formData.append("title", this.videoInfo.title);
        formData.append("videoFile", this.selectedFile);
        formData.append("description", this.videoInfo.description || "");
        const rawDuration = 18668; // 毫秒
        const totalSeconds = Math.floor(rawDuration / 1000);
        const minutes = Math.floor(totalSeconds / 60);
        const seconds = totalSeconds % 60;

        const formattedDuration = `${String(minutes).padStart(2, "0")}:${String(
          seconds
        ).padStart(2, "0")}`;

        formData.append("duration", formattedDuration);
        formData.append("authorId", String(this.userInfo.id));

        // 如果有封面，添加封面文件
        if (this.videoInfo.cover) {
          const coverBlob = await fetch(this.videoInfo.cover).then((r) => r.blob());
          formData.append(
            "coverFile",
            new File([coverBlob], "cover.jpg", { type: "image/jpeg" })
          );
        }

        // 如果有弹幕数据，添加弹幕文件
        if (this.videoInfo.cid) {
          console.log("准备获取B站弹幕，CID:", this.videoInfo.cid);
          const response = await danmakuApi.getBilibiliDanmaku(this.videoInfo.cid);
          console.log("B站弹幕API响应:", response);

          if (response.data) {
            console.log("获取到的B站弹幕数据:", response.data.substring(0, 200) + "...");

            // 将弹幕字符串转换为文件
            const danmakuBlob = new Blob([response.data], { type: "application/xml" });
            const danmakuFile = new File(
              [danmakuBlob],
              `danmaku_${this.videoInfo.cid}.xml`,
              {
                type: "application/xml",
              }
            );
            console.log("创建的弹幕文件:", danmakuFile);

            formData.append("danmakuFileName", danmakuFile);
          } else {
            console.warn("B站弹幕API返回数据为空");
          }
        }

        // 添加标签
        const tags = Array.from(this.videoInfo.tags || []);
        if (tags.length > 0) {
          tags.forEach((tag) => {
            formData.append("tags", tag);
          });
        }
        // 提交视频信息
        const response = await videoApi.uploadVideo(formData);
        if (response.data) {
          this.$message.success("视频上传成功");
          this.$router.push("/");
        }
      } catch (error) {
        console.error("上传失败:", error);
        if (error.response) {
          console.error("错误响应:", error.response.data);
          this.$message.error(error.response.data.message || "上传失败，请稍后重试");
        } else {
          this.$message.error(error.message || "上传失败，请稍后重试");
        }
      } finally {
        this.isUploading = false;
        this.uploadProgress = 0;
      }
    },
    handleFileFromChild({ file, duration }) {
      this.selectedFile = file;
      this.videoInfo.duration = duration;
      this.durationReady = true;
      // 生成视频预览URL
      const reader = new FileReader();
      reader.onload = (e) => {
        this.videoUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    updateVideoInfo(newInfo) {
      this.videoInfo = newInfo;
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
