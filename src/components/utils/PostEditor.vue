<template>
  <div class="post-box card">
    <textarea
      class="post-input"
      :placeholder="placeholder"
      v-model="content"
      @input="onInput"
    ></textarea>
    <div class="post-tools">
      <div class="tool-icons">
        <div class="emoji-picker-container">
          <button class="tool-btn" @click="toggleEmojiPicker">
            <i class="bi bi-emoji-smile"></i>
          </button>
          <EmojiPicker
            v-if="showEmojiPicker"
            @select="onSelectEmoji"
            :style="{ position: 'absolute', 'z-index': 1000, top: '100%', left: 0 }"
          />
        </div>
        <button class="tool-btn" @click="triggerFileInput">
          <i class="bi bi-image"></i>
        </button>
        <input
          type="file"
          ref="fileInput"
          accept="image/*"
          @change="handleImageUpload"
          style="display: none"
        />
        <button class="tool-btn"><i class="bi bi-at"></i></button>
        <button class="tool-btn"><i class="bi bi-bar-chart"></i></button>
        <button class="tool-btn"><i class="bi bi-hash"></i></button>
      </div>
      <div class="post-actions">
        <span class="word-count">{{ content.length }}/200</span>
        <button class="publish-btn" @click="emitPost">发布</button>
      </div>
    </div>
    <div v-if="selectedImage" class="selected-image">
      <img :src="selectedImage" alt="Selected" />
      <button class="remove-image" @click="removeImage">
        <i class="bi bi-x"></i>
      </button>
    </div>
  </div>
</template>

<script>
import EmojiPicker from "vue3-emoji-picker";
import "vue3-emoji-picker/css";

export default {
  name: "PostEditor",
  components: { EmojiPicker },
  props: {
    placeholder: {
      type: String,
      default: "好的标题更容易获得支持，选填20字",
    },
    modelValue: {
      type: String,
      default: "",
    },
  },
  emits: [
    // v-model 双向绑定
    "update:modelValue",
    // 发布动态
    "post",
    // 图片相关
    "image-change",
    "image-remove",
    // emoji相关
    "emoji-picker-toggle",
    // 内容变化
    "content-change",
  ],
  data() {
    return {
      content: this.modelValue ?? "",
      selectedImage: null,
      showEmojiPicker: false,
    };
  },
  watch: {
    modelValue(val) {
      this.content = val ?? "";
    },
  },
  methods: {
    onInput() {
      this.$emit("update:modelValue", this.content);
      this.$emit("content-change", this.content);
    },
    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker;
      this.$emit("emoji-picker-toggle", this.showEmojiPicker);
    },
    onSelectEmoji(emoji) {
      this.content += emoji.i;
      this.showEmojiPicker = false;
      this.$emit("update:modelValue", this.content);
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.selectedImage = e.target.result;
          this.$emit("image-change", e.target.result);
        };
        reader.readAsDataURL(file);
      }
    },
    removeImage() {
      this.selectedImage = null;
      this.$refs.fileInput.value = "";
      this.$emit("image-remove");
    },
    emitPost() {
      this.$emit("post", {
        content: this.content,
        image: this.selectedImage,
      });
      this.content = "";
      this.selectedImage = null;
      this.$emit("update:modelValue", "");
    },
    handleTopicClick(topic) {
      if (typeof topic === "object") {
        this.$router.push({
          name: "TopicDetail",
          params: { id: topic.id },
          query: {
            title: topic.title,
            views: topic.views,
            comments: topic.comments,
          },
        });
      }
    },
    handleEmojiSelect(emoji) {
      this.content += emoji.i;
    },
  },
};
</script>

<style scoped>
.post-box {
  background: #fff;
}
.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 16px;
  margin-bottom: 16px;
}
.emoji-picker-container {
  position: relative;
  display: inline-block;
}
.post-input {
  width: 100%;
  border: none;
  resize: none;
  padding: 12px;
  font-size: 14px;
  height: 80px;
  outline: none;
}
.post-tools {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  position: relative;
  z-index: 100;
}

.tool-icons {
  display: flex;
  gap: 12px;
}

.tool-btn {
  border: none;
  background: none;
  color: #757575;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
}
.tool-btn:hover {
  background: #f5f5f5;
}
.post-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}
.word-count {
  color: #999;
  font-size: 14px;
}

.publish-btn {
  background: #00aeec;
  color: #fff;
  border: none;
  padding: 8px 24px;
  border-radius: 4px;
  cursor: pointer;
}
.selected-image {
  position: relative;
  margin-top: 12px;
  width: 160px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
}
.selected-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.remove-image {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  border: none;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
}

.remove-image i {
  font-size: 12px;
}

/* 自定义 emoji picker 样式 */
:deep(.v3-emoji-picker) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  border: none;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search) {
  padding: 8px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search input) {
  border: 1px solid #e5e5e5;
  border-radius: 4px;
  padding: 6px 12px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search input:focus) {
  border-color: #00aeec;
  outline: none;
}

:deep(.v3-emoji-picker .v3-emoji-picker-category-title) {
  font-size: 12px;
  color: #666;
  padding: 4px 8px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-emoji) {
  cursor: pointer;
  padding: 4px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-emoji:hover) {
  background: rgba(0, 174, 236, 0.1);
  border-radius: 4px;
}
</style>
