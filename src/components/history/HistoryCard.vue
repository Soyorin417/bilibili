<template>
  <div class="history-card">
    <div class="cover">
      <img :src="item.cover" alt="cover" />
      <span class="duration">{{ item.duration }}</span>
    </div>
    <div class="info">
      <div class="title" :title="item.title">{{ item.title }}</div>
      <div class="meta">
        <span>{{ item.author }}</span>
        <span>{{ item.time }}</span>
        <span>{{ item.category }}</span>
      </div>
      <div class="actions">
        <i class="bi bi-trash" title="删除"></i>
        <i class="bi bi-star" title="收藏"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    item: Object,
  },
};
</script>

<style scoped>
.history-card {
  width: 220px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.2s;
  display: flex;
  flex-direction: column;
}
.history-card:hover {
  box-shadow: 0 4px 16px rgba(0, 174, 236, 0.12);
}
.cover {
  position: relative;
  width: 100%;
  height: 124px;
  overflow: hidden;
}
.cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.duration {
  position: absolute;
  right: 8px;
  bottom: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
}
.info {
  padding: 10px 12px 8px 12px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.title {
  font-size: 15px;
  color: #18191c;
  font-weight: 500;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.meta {
  font-size: 12px;
  color: #8a8a8a;
  display: flex;
  gap: 8px;
  margin-bottom: 6px;
}
.actions {
  display: flex;
  gap: 12px;
  color: #b1b1b1;
  font-size: 16px;
}
.actions i:hover {
  color: #00aeec;
  cursor: pointer;
}
</style>
