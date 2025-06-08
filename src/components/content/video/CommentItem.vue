<template>
  <div class="comment-item mb-4">
    <div class="d-flex">
      <div class="user-avatar me-3">
        <img
          :src="localComment.avatar"
          class="rounded-circle"
          width="48"
          height="48"
          alt="user avatar"
        />
      </div>
      <div class="comment-content">
        <div class="d-flex align-items-center mb-1">
          <h6 class="mb-0 me-2">{{ localComment.username }}</h6>
          <span class="badge bg-primary me-2" v-if="localComment.isUp">UP主</span>
          <span class="badge bg-secondary">Lv.{{ localComment.level }}</span>
        </div>
        <p class="comment-text mb-1">{{ localComment.content }}</p>
        <div class="d-flex align-items-center text-muted">
          <small class="me-3">{{ formatDateType(localComment.createTime) }}</small>
          <div class="d-flex align-items-center me-3">
            <thumbs-up
              @click="$emit('like', localComment)"
              theme="filled"
              size="16"
              :fill="thumbsUpFillColor"
              class="me-1"
              style="cursor: pointer"
            /><small>{{ localComment.likeCount }}</small>
            <thumbs-down
              @click="$emit('dislike', localComment)"
              theme="filled"
              size="16"
              :fill="thumbsDownFillColor"
              class="ms-2"
              style="cursor: pointer"
            />
            <small>{{ localComment.dislikeCount || 0 }}</small>
          </div>
          <div class="d-flex align-items-center me-3">
            <comment-one
              @click.stop="$emit('toggle-replies', localComment)"
              theme="filled"
              size="16"
              fill="#666666"
              class="me-1"
              style="cursor: pointer"
            />
            <small>{{ localComment.replyCount }}</small>
            <small
              v-if="localComment.showReplies"
              class="text-muted ms-2"
              style="cursor: pointer"
              @click.stop="$emit('toggle-replies', localComment)"
            >
              收起
            </small>
          </div>
          <div
            v-if="userInfo && userInfo.id === localComment.userUid"
            class="d-flex align-items-center"
          >
            <i class="bi bi-x" @click="$emit('delete', localComment)" style="cursor: pointer">
              <small>删除</small></i
            >
          </div>
        </div>

        <!-- 回复列表 -->
        <ReplyList
          v-if="localComment.showReplies"
          :replies="localComment.replies || []"
          :user-info="userInfo"
          :comment="localComment"
          @like="$emit('like', $event)"
          @dislike="$emit('dislike', $event)"
          @delete="$emit('delete-reply', $event)"
          @update="$emit('update-reply', $event)"
          @send="$emit('send-reply', $event)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ThumbsUp, ThumbsDown, CommentOne } from "@icon-park/vue-next";
import { formatDateType } from "@/utils/date";
import ReplyList from "./ReplyList.vue";

export default {
  name: "CommentItem",
  components: {
    ThumbsUp,
    ThumbsDown,
    CommentOne,
    ReplyList,
  },
  props: {
    comment: {
      type: Object,
      required: true,
    },
    userInfo: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      localComment: { ...this.comment }, // Deep clone the comment prop
    };
  },
  emits: [
    "like",
    "dislike",
    "toggle-replies",
    "delete",
    "delete-reply",
    "update-reply",
    "send-reply",
  ],
  computed: {
    thumbsUpFillColor() {
      return this.localComment.isLiked ? '#00aeec' : '#666666';
    },
    thumbsDownFillColor() {
      return this.localComment.isDisliked ? '#00aeec' : '#666666';
    },
  },
  watch: {
    comment: {
      handler(newVal) {
        // Update the local copy when the prop changes
        this.localComment = { ...newVal };
        console.log("Comment prop changed in CommentItem:", newVal.isLiked, newVal.isDisliked);
      },
      deep: true,
    },
  },
  mounted() {
    console.log("CommentItem mounted, initial isLiked:", this.localComment.isLiked);
  },
  methods: {
    formatDateType,
  },
};
</script>

<style scoped>
.comment-item {
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.comment-item:hover {
  background-color: #f8f9fa;
}

.user-avatar img {
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.badge {
  font-size: 0.75rem;
  padding: 0.25em 0.6em;
}

.comment-text {
  background-color: transparent;
  border: none;
  padding: 0;
  margin-bottom: 0.5rem; /* Matches Bootstrap's mb-1 */
}
</style>
