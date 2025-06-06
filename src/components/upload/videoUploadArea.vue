<template>
  <div class="upload-area" @dragover.prevent @drop.prevent="handleDrop">
    <div class="upload-box">
      <i class="bi bi-cloud-arrow-up"></i>
      <h3>点击选择文件或将文件拖入此处</h3>
      <p>支持 mp4、flv、avi、mov、wmv、mpg、mkv 等格式</p>
      <div class="upload-limits">
        <span><i class="bi bi-clock"></i> 视频最长60分钟</span>
        <span><i class="bi bi-hdd"></i> 文件最大8GB</span>
      </div>
      <input
        type="file"
        ref="fileInput"
        @change="onFileSelect"
        accept="video/*"
        class="file-input"
      />
      <button class="select-btn" @click="triggerFileInput">选择文件</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "VideoUploadArea",
  emits: ["file-selected"],
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
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
  },
};
</script>

<style scoped>
.upload-area {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.upload-box {
  text-align: center;
  padding: 40px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  background: #fafafa;
  width: 100%;
  max-width: 600px;
}
.upload-box i {
  font-size: 48px;
  color: #00aeec;
  margin-bottom: 16px;
}
.upload-box h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #18191c;
}
.upload-box p {
  margin: 0 0 16px;
  color: #61666d;
}
.upload-limits {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 24px;
  color: #61666d;
  font-size: 14px;
}
.file-input {
  display: none;
}
.select-btn {
  background: #00aeec;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s ease;
}
.select-btn:hover {
  background: #0095cc;
}
</style>
