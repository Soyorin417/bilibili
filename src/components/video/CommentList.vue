<template>
  <div class="comment-list">
    <div v-for="comment in comments" :key="comment.id" class="comment-item mb-4">
      <div class="d-flex">
        <div class="user-avatar me-3">
          <img
            :src="comment.user.avatar"
            class="rounded-circle"
            width="48"
            height="48"
            alt="user avatar"
          />
        </div>
        <div class="comment-content">
          <div class="d-flex align-items-center mb-1">
            <h6 class="mb-0 me-2">{{ comment.user.name }}</h6>
            <span class="badge bg-primary me-2" v-if="comment.user.isUp">UP主</span>
            <span class="badge bg-secondary">Lv.{{ comment.user.level }}</span>
          </div>
          <p class="mb-1">{{ comment.content }}</p>
          <div class="d-flex align-items-center text-muted">
            <small class="me-3">{{ comment.time }}</small>
            <div class="d-flex align-items-center me-3">
              <thumbs-up
                @click="$emit('like', comment)"
                theme="filled"
                size="16"
                :fill="comment.isLiked ? '#00aeec' : '#666666'"
                class="me-1"
              />
              <small>{{ comment.likeCount }}</small>
            </div>
            <div class="d-flex align-items-center">
              <comment-one
                @click="toggleReplies(comment)"
                theme="filled"
                size="16"
                fill="#666666"
                class="me-1"
              />
              <small>{{ comment.replyCount }}</small>
            </div>
          </div>

          <!-- 回复列表 -->
          <div v-if="comment.showReplies" class="replies-section mt-3">
            <div v-for="reply in comment.replies" :key="reply.id" class="reply-item mb-3">
              <div class="d-flex">
                <div class="user-avatar me-3">
                  <img
                    :src="reply.user.avatar"
                    class="rounded-circle"
                    width="32"
                    height="32"
                    alt="user avatar"
                  />
                </div>
                <div class="reply-content">
                  <div class="d-flex align-items-center mb-1">
                    <h6 class="mb-0 me-2">{{ reply.user.name }}</h6>
                    <span class="badge bg-primary me-2" v-if="reply.user.isUp">UP主</span>
                    <span class="badge bg-secondary">Lv.{{ reply.user.level }}</span>
                  </div>
                  <p class="mb-1">{{ reply.content }}</p>
                  <div class="d-flex align-items-center text-muted">
                    <small class="me-3">{{ reply.time }}</small>
                    <div class="d-flex align-items-center">
                      <thumbs-up
                        @click="$emit('like', reply)"
                        theme="filled"
                        size="16"
                        :fill="reply.isLiked ? '#00aeec' : '#666666'"
                        class="me-1"
                      />
                      <small>{{ reply.likeCount }}</small>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ThumbsUp, CommentOne } from "@icon-park/vue-next";

export default {
  name: "CommentList",
  components: {
    ThumbsUp,
    CommentOne,
  },
  props: {
    comments: {
      type: Array,
      required: true,
    },
  },
  methods: {
    toggleReplies(comment) {
      comment.showReplies = !comment.showReplies;
    },
    videoLike() {
      this.$emit("video-like");
    },
    videoDislike() {
      this.$emit("video-dislike");
    },
    videoCollect() {
      this.$emit("video-collect");
    },
    videoDiscollect() {
      this.$emit("video-discollect");
    },
    videoShare() {
      this.$emit("video-share");
    },
    videoDisshare() {
      this.$emit("video-disshare");
    },
    videoCoin() {
      this.$emit("video-coin");
    },
    videoDiscoin() {
      this.$emit("video-discoin");
    },
  },
};
</script>

<style scoped>
.comment-list {
  margin-top: 20px;
}

.comment-item .comment-content {
  flex: 1;
}

.replies-section {
  margin-left: 48px;
}

.replies-section .reply-item .reply-content {
  flex: 1;
}
</style>
