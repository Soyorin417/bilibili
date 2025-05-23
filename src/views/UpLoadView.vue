<template>
  <div class="upload-view">
    <VideoBar />
    <div class="upload-container">
      <!-- 左侧导航 -->
      <DataNav currentPage="upload" />

      <!-- 主内容区 -->
      <div class="upload-main">
        <!-- 上传区域 -->
        <div
          class="upload-area"
          v-if="!selectedFile"
          @dragover.prevent
          @drop.prevent="handleDrop"
        >
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
              @change="handleFileSelect"
              accept="video/*"
              class="file-input"
            />
            <button class="select-btn" @click="triggerFileInput">选择文件</button>
          </div>
        </div>

        <!-- 视频信息表单 -->
        <div class="video-form" v-else>
          <div class="form-header">
            <h2>视频投稿</h2>
            <button class="cancel-btn" @click="resetUpload">
              <i class="bi bi-x"></i>
              取消投稿
            </button>
          </div>

          <div class="form-content">
            <!-- 基本信息 -->
            <div class="form-section">
              <h3>基本信息</h3>
              <div class="form-group">
                <label>标题 <span class="required">*</span></label>
                <input
                  type="text"
                  v-model="videoInfo.title"
                  placeholder="请输入稿件标题"
                  maxlength="80"
                />
                <span class="char-count">{{ videoInfo.title.length }}/80</span>
              </div>

              <div class="form-group">
                <label>分区 <span class="required">*</span></label>
                <select v-model="videoInfo.category">
                  <option value="">请选择分区</option>
                  <option value="animation">动画</option>
                  <option value="music">音乐</option>
                  <option value="dance">舞蹈</option>
                  <option value="game">游戏</option>
                  <option value="knowledge">知识</option>
                  <option value="tech">科技</option>
                  <option value="sports">运动</option>
                  <option value="car">汽车</option>
                  <option value="life">生活</option>
                  <option value="food">美食</option>
                </select>
              </div>

              <div class="form-group">
                <label>简介</label>
                <textarea
                  v-model="videoInfo.description"
                  placeholder="填写更全面的相关信息，让更多人找到你的视频吧"
                  rows="4"
                ></textarea>
              </div>

              <div class="form-group">
                <label>标签</label>
                <div class="tags-input">
                  <div class="tag" v-for="(tag, index) in videoInfo.tags" :key="index">
                    {{ tag }}
                    <i class="bi bi-x" @click="removeTag(index)"></i>
                  </div>
                  <input
                    type="text"
                    v-model="tagInput"
                    @keydown.enter.prevent="addTag"
                    placeholder="输入标签按回车添加，最多10个"
                    :disabled="videoInfo.tags.length >= 10"
                  />
                </div>
              </div>
            </div>

            <!-- 封面设置 -->
            <div class="form-section">
              <h3>封面设置</h3>
              <div class="cover-upload">
                <div class="cover-preview" v-if="videoInfo.cover">
                  <img :src="videoInfo.cover" alt="视频封面" />
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

            <!-- 发布设置 -->
            <div class="form-section">
              <h3>发布设置</h3>
              <div class="form-group">
                <label>发布时间</label>
                <div class="radio-group">
                  <label class="radio">
                    <input type="radio" v-model="videoInfo.publishType" value="now" />
                    <span>立即发布</span>
                  </label>
                  <label class="radio">
                    <input
                      type="radio"
                      v-model="videoInfo.publishType"
                      value="schedule"
                    />
                    <span>定时发布</span>
                  </label>
                </div>
                <input
                  type="datetime-local"
                  v-if="videoInfo.publishType === 'schedule'"
                  v-model="videoInfo.scheduleTime"
                  class="schedule-time"
                />
              </div>
            </div>
          </div>

          <!-- 提交按钮 -->
          <div class="form-actions" @click="submitVideo">
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
import axios from "axios";

export default {
  name: "UpLoadView",
  components: {
    VideoBar,
    DataNav,
  },
  data() {
    return {
      selectedFile: null,
      videoUrl: null,
      tagInput: "",
      videoInfo: {
        title: "",
        category: "",
        description: "",
        tags: [],
        cover: null,
        publishType: "now",
        scheduleTime: null,
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
      if (file && file.type.startsWith("video/")) {
        this.selectedFile = file;
        // 将文件转换为Base64
        const reader = new FileReader();
        reader.onload = (e) => {
          this.videoUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    handleFileSelect(e) {
      const file = e.target.files[0];
      if (file) {
        this.selectedFile = file;
        // 将文件转换为Base64
        const reader = new FileReader();
        reader.onload = (e) => {
          this.videoUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      }
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
    addTag() {
      if (this.tagInput && this.videoInfo.tags.length < 10) {
        this.videoInfo.tags.push(this.tagInput);
        this.tagInput = "";
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
      };
    },
    async submitVideo() {
      // 验证必填字段
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
        // 1. 先上传视频文件到云端 MinIO
        const videoFormData = new FormData();
        videoFormData.append("file", this.selectedFile);

        const videoResponse = await axios.post("/api/upload", videoFormData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
          timeout: 30000, // 30秒超时
        });

        if (videoResponse.status !== 200) {
          throw new Error("视频文件上传失败");
        }

        const videoUrl = videoResponse.data;

        // 2. 上传封面图片到云端 MinIO（如果有）
        let coverUrl = null;
        if (this.videoInfo.cover) {
          const coverFormData = new FormData();
          coverFormData.append("file", this.videoInfo.cover);

          const coverResponse = await axios.post("/api/upload", coverFormData, {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
            timeout: 30000, // 30秒超时
          });

          if (coverResponse.status === 200) {
            coverUrl = coverResponse.data;
          }
        }

        // 3. 提交视频信息到本地后端
        const videoData = {
          title: this.videoInfo.title,
          category: this.videoInfo.category,
          description: this.videoInfo.description,
          tags: this.videoInfo.tags,
          publishType: this.videoInfo.publishType,
          scheduleTime: this.videoInfo.scheduleTime,
          view_count: 0,
          danmaku_count: 0,
          like_count: 0,
          collect_count: 0,
          coin_count: 0,
          share_count: 0,
          create_time: new Date().toISOString(),
          video_url: videoUrl,
          image: coverUrl || "http://113.45.69.13:9000/image/lucy_moon.jpg", // 默认封面
        };

        const submitResponse = await axios.post("/api/submit", videoData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          timeout: 10000, // 10秒超时
        });

        if (submitResponse.status === 200) {
          alert("视频投稿成功！");
          this.resetUpload();
          this.$router.push("/");
        } else {
          throw new Error(submitResponse.data || "投稿失败");
        }
      } catch (error) {
        console.error("投稿失败:", error);
        if (error.code === 'ECONNABORTED') {
          alert("请求超时，请检查网络连接");
        } else if (error.response) {
          // 服务器响应了错误状态码
          alert(`投稿失败: ${error.response.data || error.message}`);
        } else if (error.request) {
          // 请求已发送但没有收到响应
          alert("服务器无响应，请检查网络连接");
        } else {
          // 请求配置出错
          alert(`请求错误: ${error.message}`);
        }
      }
    },
  },
};
</script>

<style scoped>
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

/* 主内容区样式 */
.upload-main {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

/* 上传区域样式 */
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

/* 表单样式 */
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

/* 标签输入样式 */
.tags-input {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.tag {
  background: #f4f4f4;
  padding: 4px 8px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.tag i {
  cursor: pointer;
}

.tags-input input {
  border: none;
  outline: none;
  padding: 4px;
  flex: 1;
  min-width: 100px;
}

/* 封面上传样式 */
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

/* 发布设置样式 */
.radio-group {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.radio {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.schedule-time {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* 表单操作按钮 */
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
