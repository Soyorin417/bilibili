<template>
  <div v-if="post && userInfo" class="post-card card">
    <div class="post-header">
      <div class="post-user">
        <img :src="post.avatar" class="avatar" alt="用户头像" />
        <div class="post-info">
          <div class="post-author">{{ post.author }}</div>
          <div class="post-meta">
            <span class="post-time">{{ post.time }}</span>
            <span class="post-type">投稿了视频</span>
          </div>
        </div>
      </div>
      <div
        v-if="post.user_id && userInfo.id && post.user_id === userInfo.id"
        class="post-more"
        @click="handleDelete"
      >
        <i class="bi bi-x"></i>
      </div>
    </div>
    <div class="post-content">
      <div v-if="post.media">
        <div class="video-card-content">
          <div class="video-thumbnail" v-if="post.is_image">
            <img :src="post.media" alt="" />
          </div>
          <div class="video-info">
            <div class="video-title">{{ post.content }}</div>
            <div class="video-stats" v-if="post.is_image">
              <i class="bi bi-play-btn me-1"></i><span>{{ post.views }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="post-actions">
      <div class="action-btn" :class="{ active: localShared }" @click="handleShare">
        <share-two
          v-if="!localShared"
          theme="outline"
          size="21"
          fill="#000000"
          :strokeWidth="2"
          strokeLinecap="square"
        />
        <share-two
          v-else
          theme="filled"
          size="21"
          fill="#00aeec"
          :strokeWidth="2"
          strokeLinecap="square"
        />
        <span>转发</span>
      </div>
      <div class="action-btn" :class="{ active: is_comments }" @click="handleComments">
        <i class="bi bi-chat"></i>
        <span>评论 {{ post.comments }}</span>
      </div>
      <div class="action-btn" :class="{ active: localLiked }" @click="handleLike">
        <thumbs-up
          v-if="!localLiked"
          theme="outline"
          size="21"
          fill="#000000"
          :strokeWidth="2"
          strokeLinecap="square"
        />
        <thumbs-up
          v-else
          theme="filled"
          size="21"
          fill="#00aeec"
          :strokeWidth="2"
          strokeLinecap="square"
        />
        <span>{{ localLikes }}</span>
      </div>
    </div>

    <div class="post-box card" v-if="localShared || is_comments">
      <textarea
        class="post-input"
        :placeholder="localShared ? '转发时添加评论...' : '写下你的评论...'"
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
            {{ localShared ? "转发" : "评论" }}
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

    <!-- 评论列表 -->
    <div v-if="is_comments" class="comments-list">
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <img :src="comment.avatar" class="comment-avatar" alt="用户头像" />
        <div class="comment-content">
          <div class="comment-header">
            <span class="comment-author">{{ comment.author }}</span>
            <span class="comment-time">{{ comment.time }}</span>
          </div>
          <div class="comment-text">{{ comment.content }}</div>
          <div class="comment-actions">
            <button class="comment-action-btn" @click="likeComment(comment)">
              <i class="bi bi-hand-thumbs-up"></i>
              <span>{{ comment.likes }}</span>
            </button>
            <button class="comment-action-btn" @click="replyComment(comment)">
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
import { ShareTwo, ThumbsUp } from "@icon-park/vue-next";
import EmojiPicker from "vue3-emoji-picker";

export default {
  name: "ActivityCard",
  components: {
    ShareTwo,
    ThumbsUp,
    EmojiPicker,
  },
  props: {
    post: {
      type: Object,
      required: true,
      default: () => ({
        id: "",
        user_id: "",
        author: "",
        avatar: "",
        time: "",
        content: "",
        media: "",
        duration: "",
        views: 0,
        comments: 0,
        likes: 0,
        liked: false,
        shared: false,
      }),
    },
    userInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localLiked: this.post.liked,
      localShared: this.post.shared,
      localLikes: this.post.likes,
      localComments: this.post.comments,
      is_comments: false,
      postContent: "",
      showEmojiPicker: false,
      selectedImage: null,
      comments: [
        {
          id: 1,
          author: "用户1",
          avatar: "http://113.45.69.13:9000/image/laoba.jpg",
          time: "2小时前",
          content: "这个视频太棒了！",
          likes: 5,
        },
        {
          id: 2,
          author: "用户2",
          avatar: "http://113.45.69.13:9000/image/arknight_w.jpg",
          time: "1小时前",
          content: "支持一下！",
          likes: 3,
        },
      ],
    };
  },
  methods: {
    handleLike() {
      this.localLiked = !this.localLiked;
      if (this.localLiked) {
        this.localLikes = this.localLikes + 1;
      } else {
        this.localLikes = this.localLikes - 1;
      }
      this.$emit("like", {
        id: this.post.id,
        liked: this.localLiked,
        likes: this.localLikes,
      });
    },
    handleComments() {
      this.is_comments = !this.is_comments;
      this.$emit("comments", { id: this.post.id, comments: this.localComments });
    },
    handleShare() {
      this.localShared = !this.localShared;
      this.$emit("share", { id: this.post.id, shared: this.localShared });
    },
    handleDelete() {
      this.$emit("delete", this.post.id);
    },
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

      if (this.localShared) {
        // 处理转发
        this.$emit("share-post", {
          id: this.post.id,
          content: this.postContent,
          image: this.selectedImage,
        });
      } else {
        // 处理评论
        this.comments.unshift({
          id: this.comments.length + 1,
          author: this.userInfo.name,
          avatar: this.userInfo.avatar,
          time: "刚刚",
          content: this.postContent,
          likes: 0,
        });
        this.localComments++;
        this.$emit("new-comment", {
          id: this.post.id,
          comments: this.localComments,
        });
      }

      this.postContent = "";
      this.removeImage();
      this.showEmojiPicker = false;
    },
    likeComment(comment) {
      comment.likes++;
    },
    replyComment(comment) {
      this.postContent = `@${comment.author} `;
      this.is_comments = true;
    },
  },
  watch: {
    "post.liked"(newVal) {
      this.localLiked = newVal;
    },
    "post.shared"(newVal) {
      this.localShared = newVal;
    },
    "post.likes"(newVal) {
      this.localLikes = newVal;
    },
    "post.comments"(newVal) {
      this.localComments = newVal;
    },
  },
};
</script>

<style scoped>
.post-card {
  background: #fff;
  border-radius: 4px;
  padding: 12px 16px;
  margin-bottom: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s ease;
}

.post-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.post-user {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  box-shadow: none;
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.post-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.post-author {
  font-size: 13px;
  font-weight: 500;
  color: #18191c;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #9499a0;
}

.post-type {
  color: #9499a0;
}

.post-more {
  color: #9499a0;
  padding: 4px;
  cursor: pointer;
  font-size: 16px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.post-more:hover {
  color: #fb7299;
  background-color: rgba(251, 114, 153, 0.1);
}

.post-content {
  margin-bottom: 8px;
  padding-left: 44px;
}

.video-card-content {
  display: flex;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.video-card-content:hover {
  transform: translateY(-2px);
}

.video-thumbnail {
  flex-shrink: 0;
  width: 160px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-thumbnail:hover img {
  transform: scale(1.05);
}

.video-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: left;
  padding: 4px 0;
}

.video-title {
  font-size: 13px;
  color: #18191c;
  line-height: 1.5;
  display: -webkit-box;

  -webkit-box-orient: vertical;
  overflow: hidden;
  text-align: left;
  margin-bottom: 8px;
}

.video-stats {
  font-size: 12px;
  color: #9499a0;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-actions {
  display: flex;
  gap: 24px;
  padding: 8px 0 0 44px;
  border-top: 1px solid #f1f2f3;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #9499a0;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 4px;
}

.action-btn:hover {
  color: #00aeec;
  background-color: rgba(0, 174, 236, 0.1);
}

.action-btn.active {
  color: #00aeec;
}

.action-btn i {
  font-size: 15px;
}

/* Post Box Styles */
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

/* Emoji Picker Styles */
.emoji-picker-container {
  position: relative;
  display: inline-block;
}

/* Selected Image Styles */
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

/* Comments List Styles */
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
