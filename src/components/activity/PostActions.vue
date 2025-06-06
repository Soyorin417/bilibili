<template>
  <div class="post-actions">
    <div class="action-btn" :class="{ active: shared }" @click="$emit('share')">
      <share-two
        v-if="!shared"
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
    <div class="action-btn" :class="{ active: isComments }" @click="$emit('comments')">
      <i class="bi bi-chat"></i>
      <span>评论 {{ commentCount }}</span>
    </div>
    <div class="action-btn" :class="{ active: liked }" @click="$emit('like')">
      <thumbs-up
        v-if="!liked"
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
      <span>{{ likeCount }}</span>
    </div>
  </div>
</template>

<script>
import { ShareTwo, ThumbsUp } from "@icon-park/vue-next";

export default {
  name: "PostActions",
  components: {
    ShareTwo,
    ThumbsUp,
  },
  props: {
    shared: {
      type: Boolean,
      default: false,
    },
    isComments: {
      type: Boolean,
      default: false,
    },
    liked: {
      type: Boolean,
      default: false,
    },
    commentCount: {
      type: Number,
      default: 0,
    },
    likeCount: {
      type: Number,
      default: 0,
    },
  },
};
</script>

<style scoped>
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
</style>
