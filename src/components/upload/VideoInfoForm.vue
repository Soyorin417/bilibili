<template>
  <div class="video-form">
    <div class="form-header">
      <h2>视频投稿</h2>
      <button class="cancel-btn" @click="$emit('reset-upload')">
        <i class="bi bi-x"></i>
        取消投稿
      </button>
    </div>

    <div class="form-content">
      <h3 class="main-title">基本设置</h3>
      <!--视频预览-->

      <!-- 视频上传进度区 -->
      <div class="video-upload-status">
        <div class="video-file-info">
          <i class="bi bi-play-btn-fill file-icon"></i>
          <div class="file-meta">
            <div class="file-title">{{ videoInfo.title }}</div>
            <div class="file-status">
              <i class="bi bi-check-circle-fill success-icon"></i>
              上传完成
            </div>
          </div>
        </div>
        <button class="replace-btn" @click="$emit('replace-video')">
          <i class="bi bi-arrow-repeat"></i> 更换视频
        </button>
        <div class="progress-bar-wrap">
          <div class="progress-bar">
            <div class="progress" :style="{ width: uploadProgress + '%' }"></div>
          </div>
        </div>
      </div>
      <!-- 封面 -->
      <div class="form-group d-flex">
        <label>封面 <span class="required">*</span></label>
        <div class="cover-upload">
          <div class="cover-preview" v-if="videoInfo.cover">
            <img :src="videoInfo.cover" alt="视频封面" />
            <div class="cover-actions">
              <button @click="removeCover"><i class="bi bi-trash"></i></button>
            </div>
          </div>
          <div class="cover-upload-box" v-else>
            <label class="cover-select-btn">
              <i class="bi bi-image"></i>
              选择图片文件
              <input
                type="file"
                @change="handleCoverSelect"
                accept="image/*"
                class="file-input"
                style="display: none"
              />
            </label>
          </div>
        </div>
      </div>

      <!-- 标题 -->
      <div class="form-group d-flex">
        <label>标题 <span class="required">*</span></label>
        <input
          type="text"
          class="long-input"
          :value="videoInfo.title"
          @input="
            $emit('update-video-info', { ...videoInfo, title: $event.target.value })
          "
          placeholder="请输入稿件标题"
          maxlength="80"
        />
        <span class="char-count">{{ videoInfo.title.length }}/80</span>
      </div>

      <!-- 类型（原创/转载） -->
      <div class="form-group d-flex">
        <label>类型 <span class="required">*</span></label>
        <div class="radio-group">
          <label class="radio mt-3">
            <input
              type="radio"
              :checked="videoInfo.copyrightType === 'original'"
              @change="
                $emit('update-video-info', { ...videoInfo, copyrightType: 'original' })
              "
              value="original"
            />
            <span>原创</span>
          </label>
          <label class="radio mt-3">
            <input
              type="radio"
              :checked="videoInfo.copyrightType === 'repost'"
              @change="
                $emit('update-video-info', { ...videoInfo, copyrightType: 'repost' })
              "
              value="repost"
            />
            <span>转载</span>
          </label>
        </div>
      </div>

      <!-- 分区 -->
      <div class="form-group d-flex">
        <label>分区 <span class="required">*</span></label>
        <div class="tags-input">
          <div class="tag" v-if="videoInfo.category">
            {{ videoInfo.category }}
            <i class="bi bi-x" @click="removeCategory"></i>
          </div>
          <select
            class="short-input"
            :value="tagInput"
            @change="addCategory"
            :disabled="!!videoInfo.category"
          >
            <option value="" disabled>请选择分区</option>
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
      </div>

      <!-- 标签 -->
      <div class="form-group d-flex">
        <label>标签</label>
        <div class="tags-input">
          <div class="tag" v-for="(tag, index) in videoInfo.tags" :key="index">
            {{ tag }}
            <i class="bi bi-x" @click="removeTag(index)"></i>
          </div>
          <input
            type="text"
            class="short-input"
            :value="tagInput"
            @input="handleTagInput"
            @keydown.enter.prevent="addTag"
            placeholder="输入标签按回车添加，最多10个"
            :disabled="videoInfo.tags.length >= 10"
          />
        </div>
      </div>

      <!-- 简介 -->
      <div class="form-group d-flex">
        <label>简介</label>
        <textarea
          class="long-input"
          :value="videoInfo.description"
          @input="
            $emit('update-video-info', { ...videoInfo, description: $event.target.value })
          "
          placeholder="填写更全面的相关信息，让更多人找到你的视频吧"
          rows="4"
        ></textarea>
      </div>

      <!-- CID -->
      <div class="form-group">
        <label>视频CID</label>
        <div class="input-group">
          <input
            type="text"
            :value="videoInfo.cid"
            @input="
              $emit('update-video-info', { ...videoInfo, cid: $event.target.value })
            "
            class="form-control"
            placeholder="请输入B站视频CID"
          />
          <div class="input-group-append">
            <button
              class="btn btn-primary"
              @click="$emit('fetch-danmaku')"
              :disabled="!videoInfo.cid"
            >
              获取弹幕
            </button>
          </div>
        </div>
      </div>

      <div class="form-group d-flex">
        <label>发布时间</label>
        <div>
          <div class="radio-group">
            <label class="radio mt-3">
              <input
                type="radio"
                :checked="videoInfo.publishType === 'now'"
                @change="$emit('update-video-info', { ...videoInfo, publishType: 'now' })"
                value="now"
              />
              <span>立即发布</span>
            </label>
            <label class="radio mt-3">
              <input
                type="radio"
                :checked="videoInfo.publishType === 'schedule'"
                @change="
                  $emit('update-video-info', { ...videoInfo, publishType: 'schedule' })
                "
                value="schedule"
              />
              <span>定时发布</span>
            </label>
          </div>
          <input
            type="datetime-local"
            v-if="videoInfo.publishType === 'schedule'"
            :value="videoInfo.scheduleTime"
            @input="
              $emit('update-video-info', {
                ...videoInfo,
                scheduleTime: $event.target.value,
              })
            "
            class="schedule-time"
          />
        </div>
      </div>
    </div>

    <!-- 发布设置 -->

    <!-- 提交按钮 -->
    <div class="form-actions">
      <button class="draft-btn" @click.stop="$emit('save-draft')">保存草稿</button>
      <button class="submit-btn" @click.stop="$emit('submit-video')">立即投稿</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "VideoInfoForm",
  data() {
    return {
      tagInput: "",
      uploadProgress: 0,
    };
  },
  props: {
    videoInfo: {
      type: Object,
      required: true,
    },
  },
  methods: {
    resetUpload() {
      this.$emit("reset-upload");
    },
    submitVideo() {
      this.$emit("submit-video");
    },
    removeCover() {
      this.$emit("remove-cover");
    },
    handleCoverSelect(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.$emit("update-video-info", {
            ...this.videoInfo,
            cover: e.target.result,
          });
        };
        reader.readAsDataURL(file);
      }
    },
    handleTagInput(e) {
      this.tagInput = e.target.value;
    },
    addTag() {
      const tag = this.tagInput.trim();
      if (tag && this.videoInfo.tags.length < 10) {
        this.$emit("update-video-info", {
          ...this.videoInfo,
          tags: [...this.videoInfo.tags, tag],
        });
        this.tagInput = "";
      }
    },
    removeTag(index) {
      const newTags = [...this.videoInfo.tags];
      newTags.splice(index, 1);
      this.$emit("update-video-info", {
        ...this.videoInfo,
        tags: newTags,
      });
    },
    addCategory(e) {
      this.$emit("update-video-info", {
        ...this.videoInfo,
        category: e.target.value,
      });
    },
    removeCategory() {
      this.$emit("update-video-info", {
        ...this.videoInfo,
        category: "",
      });
    },
  },
  watch: {
    videoInfo: {
      handler(newVal) {
        this.$emit("update-video-info", newVal);
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
/* 上传进度条样式 */
.upload-progress {
  margin-bottom: 20px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.progress-bar {
  height: 6px;
  background: #e9ecef;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress {
  height: 100%;
  background: #00aeec;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 14px;
  color: #61666d;
  text-align: center;
}
.form-content {
  width: 700px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  padding: 24px 32px 32px 32px;
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

.form-group.d-flex {
  display: flex;
  align-items: center;
  margin-bottom: 18px;
}

.form-group label {
  width: 90px;
  min-width: 80px;
  text-align: right;
  margin-right: 16px;
  color: #18191c;
  font-size: 15px;
  flex-shrink: 0;
}

.required {
  color: #ff6b6b;
}

.long-input,
.tags-input,
.short-input,
textarea.long-input {
  width: 420px;
  min-width: 200px;
  max-width: 100%;
  display: inline-block;
  background: #fff;
  border: 1px solid #e3e5e7;
  border-radius: 4px;
  font-size: 15px;
  padding: 8px 12px;
}

textarea.short-input {
  height: 80px;
  resize: vertical;
}

textarea.long-input {
  height: 80px;
  resize: vertical;
}

.tags-input {
  width: 420px;
  background: #f7f7fa;
  border: 1px solid #e3e5e7;
  border-radius: 4px;
  padding: 6px 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 8px;
}

.recommend-tags {
  width: 420px;
  margin-left: 106px; /* label宽+margin-right */
  margin-bottom: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.recommend-tag {
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 3px;
  padding: 2px 8px;
  font-size: 13px;
  cursor: pointer;
}

.tag {
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 3px;
  padding: 2px 8px;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.tag i {
  cursor: pointer;
  margin-left: 2px;
}

.tags-input input.short-input {
  border: none;
  background: transparent;
  box-shadow: none;
  padding: 2px 4px;
  min-width: 60px;
  font-size: 14px;
}

.char-count {
  margin-left: 8px;
  color: #9499a0;
  font-size: 12px;
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

/* 视频上传进度区样式 */
.video-upload-status {
  display: flex;
  align-items: center;
  background: #f8f9fa;
  border-radius: 6px;
  padding: 16px 24px;
  margin-bottom: 24px;
  gap: 24px;
  width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.video-file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.file-icon {
  font-size: 36px;
  color: #00aeec;
}

.file-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.file-title {
  font-size: 16px;
  color: #18191c;
  font-weight: 500;
}

.file-status {
  font-size: 14px;
  color: #43cf7c;
  display: flex;
  align-items: center;
  gap: 4px;
}

.success-icon {
  color: #43cf7c;
  font-size: 16px;
}

.replace-btn {
  background: none;
  border: none;
  color: #00aeec;
  font-size: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.progress-bar-wrap {
  flex: 2;
  margin-left: 24px;
}

.progress-bar {
  height: 6px;
  background: #e9ecef;
  border-radius: 3px;
  overflow: hidden;
  width: 100%;
}

.progress {
  height: 100%;
  background: #43cf7c;
  transition: width 0.3s;
}
</style>
