<template>
  <div>
    <div class="post-box card" v-if="showCommentBox">
      <textarea
        class="post-input"
        :placeholder="isShare ? '转发时添加评论...' : '写下你的评论...'"
        v-model="postContent"
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
          <span class="word-count">{{ postContent.length }}/200</span>
          <button class="publish-btn" @click="sendPost">
            {{ isShare ? "转发" : "评论" }}
          </button>
        </div>
      </div>
      <div v-if="selectedImage" class="selected-image">
        <img :src="selectedImage" alt="Selected" />
        <button class="remove-image" @click="removeImage">
          <i class="bi bi-x"></i>
        </button>
      </div>
    </div>

    <div v-if="showComments" class="comments-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <img :src="comment.avatar" class="comment-avatar" alt="用户头像" />
        <div class="comment-content">
          <div class="comment-header">
            <span class="comment-author">{{ comment.username }}</span>
            <span class="comment-time">{{ comment.time }}</span>
            <button
              v-if="userInfo && comment.userId === userInfo.id"
              class="comment-action-btn"
              @click="$emit('delete-comment', comment.id)"
            >
              删除
            </button>
          </div>
          <div class="comment-text">{{ comment.content }}</div>
          <div class="comment-actions">
            <button class="comment-action-btn" @click="$emit('like-comment', comment)">
              <i class="bi bi-hand-thumbs-up"></i>
              <span>{{ comment.likes }}</span>
            </button>
            <button class="comment-action-btn" @click="$emit('reply-comment', comment)">
              <i class="bi bi-reply"></i>
              <span>回复</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EmojiPicker from "vue3-emoji-picker";

export default {
  name: "CommentSection",
  components: {
    EmojiPicker,
  },
  props: {
    showCommentBox: {
      type: Boolean,
      default: false,
    },
    showComments: {
      type: Boolean,
      default: false,
    },
    isShare: {
      type: Boolean,
      default: false,
    },
    comments: {
      type: Array,
      default: () => [],
    },
    userInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      postContent: "",
      showEmojiPicker: false,
      selectedImage: null,
    };
  },
  methods: {
    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker;
    },
    onSelectEmoji(emoji) {
      this.postContent += emoji;
      this.showEmojiPicker = false;
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
        };
        reader.readAsDataURL(file);
      }
    },
    removeImage() {
      this.selectedImage = null;
      this.$refs.fileInput.value = "";
    },
    sendPost() {
      if (!this.postContent.trim() && !this.selectedImage) return;

      this.$emit("send-post", {
        content: this.postContent,
        image: this.selectedImage,
      });

      this.postContent = "";
      this.removeImage();
      this.showEmojiPicker = false;
    },
  },
};
</script>

<style scoped>
.post-box {
  margin-top: 12px;
  padding: 12px;
  border: none;
}

.post-input {
  width: 100%;
  border: none;
  resize: none;
  padding: 8px;
  font-size: 14px;
  height: 60px;
  outline: none;
  background: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 8px;
}

.post-tools {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
  position: relative;
}

.tool-icons {
  display: flex;
  gap: 8px;
}

.tool-btn {
  border: none;
  background: none;
  color: #757575;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.tool-btn:hover {
  background: #f5f5f5;
  color: #00aeec;
}

.word-count {
  color: #999;
  font-size: 14px;
  margin-right: 12px;
}

.publish-btn {
  background: #00aeec;
  color: #fff;
  border: none;
  padding: 6px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.publish-btn:hover {
  background: #0098d4;
}

.emoji-picker-container {
  position: relative;
  display: inline-block;
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
  transition: all 0.3s ease;
}

.remove-image:hover {
  background: rgba(0, 0, 0, 0.7);
}

.remove-image i {
  font-size: 12px;
}

.comments-list {
  margin-top: 12px;
  padding: 0 44px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f1f2f3;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-author {
  font-size: 13px;
  font-weight: 500;
  color: #18191c;
}

.comment-time {
  font-size: 12px;
  color: #9499a0;
}

.comment-text {
  font-size: 14px;
  color: #18191c;
  line-height: 1.5;
  margin-bottom: 8px;
  text-align: left;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.comment-action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #9499a0;
  font-size: 12px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.comment-action-btn:hover {
  color: #00aeec;
  background: rgba(0, 174, 236, 0.1);
}

.comment-action-btn i {
  font-size: 14px;
}
</style>
