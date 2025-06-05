<template>
  <div class="video-card">
    <router-link :to="`/video/${item.id}`" class="card-link">
      <div class="card-content">
        <div class="left-section">
          <img :src="item.avatar" class="avatar" />
          <div class="info">
            <div class="username">{{ item.username }}</div>
            <div class="title ellipsis">{{ item.title }}</div>
            <div class="date">{{ formatDate(item.createTime) }}</div>
          </div>
        </div>
        <img :src="item.image" class="thumbnail" />
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "VideoCard",
  props: {
    item: {
      type: Object,
      required: true,
      validator: (obj) => {
        return obj.id && obj.username && obj.title && obj.createTime;
      },
    },
    type: {
      type: String,
      default: "dynamic", // 'dynamic' 或 'history'
      validator: (value) => ["dynamic", "history"].includes(value),
    },
  },
  methods: {
    formatDate(dateStr) {
      const date = new Date(dateStr);
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return `${month}月${day}日`;
    },
  },
};
</script>

<style scoped>
.video-card {
  margin-bottom: 14px;
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.video-card:hover {
  background-color: #f6f7f8;
}

.card-link {
  display: block;
  text-decoration: none;
  color: inherit;
}

.card-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left-section {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0; /* 防止flex子元素溢出 */
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.info {
  margin-left: 8px;
  min-width: 0; /* 防止文本溢出 */
  flex: 1;
}

.username {
  font-weight: 600;
  font-size: 13px;
  color: #333;
  margin-bottom: 2px;
}

.title {
  font-size: 12px;
  color: #444;
  margin-bottom: 4px;
}

.date {
  font-size: 11px;
  color: #b8b8b8;
}

.thumbnail {
  width: 60px;
  height: 36px;
  object-fit: cover;
  border-radius: 6px;
  margin-left: 8px;
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 180px;
  display: block;
}

/* 历史记录模式特定样式 */
.video-card[data-type="history"] .thumbnail {
  width: 120px;
  height: 68px;
}

.video-card[data-type="history"] .title {
  font-size: 14px;
  margin-bottom: 6px;
}

/* 动态模式特定样式 */
.video-card[data-type="dynamic"] .info {
  max-width: calc(100% - 100px); /* 为缩略图留出空间 */
}
</style>
