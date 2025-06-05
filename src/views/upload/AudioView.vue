<template>
  <div class="audio-view">
    <VideoBar />
    <div class="container">
      <DataNav currentPage="upload" />
      <div class="main-content">
        <h2>音频投稿</h2>
        <div
          class="upload-area"
          v-if="!selectedFile"
          @dragover.prevent
          @drop.prevent="handleDrop"
        >
          <div class="upload-box">
            <i class="bi bi-music-note-list"></i>
            <h3>点击选择文件或将文件拖入此处</h3>
            <p>支持 mp3、wav、flac、aac 等格式</p>
            <div class="upload-limits">
              <span><i class="bi bi-clock"></i> 音频最长30分钟</span>
              <span><i class="bi bi-hdd"></i> 文件最大500MB</span>
            </div>
            <input
              type="file"
              ref="fileInput"
              @change="handleFileSelect"
              accept="audio/*"
              class="file-input"
            />
            <button class="select-btn" @click="triggerFileInput">选择文件</button>
          </div>
        </div>

        <div class="audio-form" v-else>
          <div class="form-header">
            <h2>音频投稿</h2>
            <button class="cancel-btn" @click="resetUpload">
              <i class="bi bi-x"></i>
              取消投稿
            </button>
          </div>

          <div class="form-content">
            <div class="form-section">
              <h3>基本信息</h3>
              <div class="form-group">
                <label>标题 <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="audioInfo.title"
                  placeholder="请输入音频标题"
                  maxlength="80"
                />
                <span class="char-count">{{ audioInfo.title.length }}/80</span>
              </div>

              <div class="form-group">
                <label>分类 <span class="required">*</span></label>
                <select v-model="audioInfo.category">
                  <option value="">请选择分类</option>
                  <option value="music">音乐</option>
                  <option value="podcast">播客</option>
                  <option value="asmr">ASMR</option>
                  <option value="voice">配音</option>
                </select>
              </div>

              <div class="form-group">
                <label>简介</label>
                <textarea
                  v-model="audioInfo.description"
                  placeholder="填写更全面的相关信息，让更多人找到你的音频吧"
                  rows="4"
                ></textarea>
              </div>
            </div>

            <div class="form-section">
              <h3>封面设置</h3>
              <div class="cover-upload">
                <div class="cover-preview" v-if="audioInfo.cover">
                  <img :src="audioInfo.cover" alt="音频封面" />
                  <div class="cover-actions">
                    <button @click="removeCover"><i class="bi bi-trash"></i></button>
                  </div>
                </div>
                <div class="cover-upload-box" v-else @click="triggerCoverInput">
                  <i class="bi bi-image"></i>
                  <p>点击上传封面</p>
                  <input
                    type="file"
                    ref="coverInput"
                    @change="handleCoverSelect"
                    accept="image/*"
                    class="file-input"
                  />
                </div>
              </div>
            </div>
          </div>

          <div class="form-actions">
            <button class="draft-btn">保存草稿</button>
            <button class="submit-btn">立即投稿</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import DataNav from "@/components/navBar/DataNav.vue";

export default {
  name: "AudioView",
  components: {
    VideoBar,
    DataNav,
  },
  data() {
    return {
      selectedFile: null,
      audioUrl: null,
      audioInfo: {
        title: "",
        category: "",
        description: "",
        cover: null,
      },
    };
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    triggerCoverInput() {
      this.$refs.coverInput.click();
    },
    handleDrop(e) {
      const file = e.dataTransfer.files[0];
      if (file && file.type.startsWith("audio/")) {
        this.selectedFile = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          this.audioUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    handleFileSelect(e) {
      const file = e.target.files[0];
      if (file) {
        this.selectedFile = file;
        const reader = new FileReader();
        reader.onload = (e) => {
          this.audioUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    handleCoverSelect(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.audioInfo.cover = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    removeCover() {
      this.audioInfo.cover = null;
    },
    resetUpload() {
      this.selectedFile = null;
      this.audioUrl = null;
      this.audioInfo = {
        title: "",
        category: "",
        description: "",
        cover: null,
      };
    },
  },
};
</script>

<style scoped>
.audio-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 20px;
}

.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

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

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-header h2 {
  margin: 0;
  font-size: 20px;
}

.cancel-btn {
  background: none;
  border: none;
  color: #61666d;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.form-section {
  margin-bottom: 32px;
}

.form-section h3 {
  font-size: 16px;
  margin: 0 0 16px;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #18191c;
}

.required {
  color: #ff6b6b;
}

input[type="text"],
textarea,
select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea {
  resize: vertical;
}

.char-count {
  position: absolute;
  right: 8px;
  bottom: 8px;
  color: #9499a0;
  font-size: 12px;
}

.cover-upload {
  width: 240px;
}

.cover-preview {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
}

.cover-preview img {
  width: 100%;
  display: block;
}

.cover-actions {
  position: absolute;
  top: 8px;
  right: 8px;
}

.cover-actions button {
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
}

.cover-upload-box {
  border: 2px dashed #ddd;
  border-radius: 4px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
}

.cover-upload-box i {
  font-size: 32px;
  color: #9499a0;
  margin-bottom: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f4f4f4;
}

.draft-btn {
  padding: 12px 24px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn {
  padding: 12px 24px;
  background: #00aeec;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background: #0095cc;
}
</style>
