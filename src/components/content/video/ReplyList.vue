<template>
  <div class="replies-section mt-3">
    <!-- 回复列表 -->
    <div v-for="reply in replies" :key="reply.commentId" class="reply-item mb-3">
      <div class="d-flex">
        <div class="user-avatar me-3">
          <img
            :src="reply.avatar"
            class="rounded-circle"
            width="32"
            height="32"
            alt="user avatar"
          />
        </div>
        <div class="reply-content">
          <div class="d-flex align-items-center mb-1">
            <h6 class="mb-0 me-2">{{ reply.username }}</h6>
            <span class="badge bg-primary me-2" v-if="reply.isUp">UP主</span>
            <span class="badge bg-secondary">Lv.{{ reply.level }}</span>
          </div>
          <p class="mb-1">{{ reply.content }}</p>
          <div class="d-flex align-items-center text-muted">
            <small class="me-3">{{ formatDateType(reply.createTime) }}</small>
            <div class="d-flex align-items-center me-3">
              <thumbs-up
                @click="$emit('like', reply)"
                theme="filled"
                size="16"
                :fill="reply.isLiked ? '#00aeec' : '#666666'"
                class="me-1"
              /><small>{{ reply.likeCount }}</small>
              <thumbs-down
                @click="$emit('dislike', reply)"
                theme="filled"
                size="16"
                :fill="reply.isDisliked ? '#00aeec' : '#666666'"
                class="ms-2"
              />
              <small>{{ reply.dislikeCount || 0 }}</small>
            </div>
            <!-- 添加编辑和删除按钮，只有回复作者可见 -->
            <div
              v-if="userInfo && userInfo.id === reply.userUid"
              class="d-flex align-items-center"
            >
              <i class="bi bi-x" @click="$emit('delete', reply)" style="cursor: pointer"
                ><small>删除</small></i
              >
            </div>
          </div>

          <!-- 编辑回复的输入框 -->
          <div v-if="reply.isEditing" class="edit-reply mt-2">
            <div class="form-floating">
              <input
                v-model="reply.editContent"
                type="text"
                class="form-control"
                :id="'editReplyInput-' + reply.id"
                placeholder="编辑回复"
              />
              <label :for="'editReplyInput-' + reply.id">编辑回复</label>
            </div>
            <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
              <button
                @click="$emit('update', reply)"
                class="btn btn-primary btn-sm me-2"
                type="button"
              >
                保存
              </button>
              <button
                @click="cancelEdit(reply)"
                class="btn btn-secondary btn-sm"
                type="button"
              >
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 回复输入框 -->
    <div class="reply-input mt-3">
      <div class="form-floating">
        <input
          v-model="localReplyContent"
          type="text"
          class="form-control"
          :id="'replyInput-' + comment.commentId"
          placeholder="回复评论"
          @keyup.enter="handleSendReply"
        />
        <label :for="'replyInput-' + comment.commentId">回复评论</label>
      </div>
      <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
        <button @click="handleSendReply" class="btn btn-primary btn-sm" type="button">
          发送回复
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ThumbsUp, ThumbsDown } from "@icon-park/vue-next";
import { formatDateType } from "@/utils/date";

export default {
  name: "ReplyList",
  components: {
    ThumbsUp,
    ThumbsDown,
  },
  props: {
    replies: {
      type: Array,
      required: true,
    },
    userInfo: {
      type: Object,
      default: null,
    },
    comment: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localReplyContent: "",
    };
  },
  emits: ["like", "dislike", "delete", "update", "send"],
  methods: {
    formatDateType,
    cancelEdit(reply) {
      reply.isEditing = false;
      reply.editContent = reply.content;
    },
    handleSendReply() {
      if (!this.localReplyContent.trim()) {
        return;
      }
      const emittedData = {
        ...this.comment,
        replyContent: this.localReplyContent,
      };
      console.log("ReplyList emitting:", emittedData);
      this.$emit("send", emittedData);
      this.localReplyContent = "";
    },
  },
};
</script>

<style scoped>
.reply-item {
  padding: 8px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.reply-item:hover {
  background-color: #f8f9fa;
}

.user-avatar img {
  object-fit: cover;
}

.reply-content {
  flex: 1;
}

.badge {
  font-size: 0.75rem;
  padding: 0.25em 0.6em;
}

.reply-input {
  margin-left: 40px;
}
</style>
