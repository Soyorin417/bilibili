<template>
  <div class="post-header">
    <div class="post-user">
      <img :src="post.avatar" class="avatar" alt="用户头像" />
      <div class="post-info">
        <div class="post-author">{{ post.username }}</div>
        <div class="post-meta">
          <span class="post-time">{{ formatTime(post.time) }}</span>
          <span class="post-type">投稿了视频</span>
        </div>
      </div>
    </div>
    <div
      v-if="post.user_id && userInfo.id && post.user_id === userInfo.id"
      class="post-more"
      @click="$emit('delete')"
    >
      <i class="bi bi-x"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: "PostHeader",
  props: {
    post: {
      type: Object,
      required: true,
    },
    userInfo: {
      type: Object,
      required: true,
    },
  },
  methods: {
    formatTime(isoString) {
      if (!isoString) return "";
      const str = isoString.replace("T", " ");
      const date = new Date(str);
      if (isNaN(date.getTime())) return isoString;

      const pad = (n) => n.toString().padStart(2, "0");
      return (
        date.getFullYear() +
        "-" +
        pad(date.getMonth() + 1) +
        "-" +
        pad(date.getDate()) +
        " " +
        pad(date.getHours()) +
        ":" +
        pad(date.getMinutes()) +
        ":" +
        pad(date.getSeconds())
      );
    },
  },
};
</script>

<style scoped>
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
  text-align: left;
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
</style>
