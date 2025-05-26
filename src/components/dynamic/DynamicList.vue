<template>
  <div class="dynamic-list">
    <div class="dynamic-title ms-2">动态</div>
    <div class="dynamic-divider-with-text">
      <span class="divider-text">历史动态</span>
    </div>
    <div class="dynamic-item" v-for="item in dynamics" :key="item.id">
      <router-link :to="`/video/${item.id}`" class="dynamic-link">
        <img :src="item.avatar" class="avatar" />
        <div class="dynamic-content">
          <div class="user">{{ item.username }}</div>
          <div class="content ellipsis">{{ item.title }}</div>
          <div class="date">{{ formatDate(item.createTime) }}</div>
        </div>
        <img :src="item.image" class="dynamic-thumb" />
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "DynamicList",
  props: {
    dynamics: {
      type: Array,
      required: true,
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
.dynamic-list {
  margin: 10px 0;
  padding: 0 16px;
}
.dynamic-item {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
}
.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}
.dynamic-content {
  flex: 1;
  margin-left: 8px;
}
.user {
  font-weight: 600;
  font-size: 13px;
  color: #333;
}
.content {
  font-size: 12px;
  color: #444;
  margin: 2px 0 4px 0;
  word-break: break-all;
}
.date {
  font-size: 11px;
  color: #b8b8b8;
}
.dynamic-thumb {
  width: 60px;
  height: 36px;
  object-fit: cover;
  border-radius: 6px;
  margin-left: 8px;
}
.dynamic-divider-with-text {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 8px 0 12px 0;
}
.divider-text {
  color: #b8b8b8;
  font-size: 14px;
  margin: 0 12px;
  white-space: nowrap;
}
.dynamic-divider-with-text::before,
.dynamic-divider-with-text::after {
  content: "";
  flex: 1;
  height: 1px;
  background: #e3e5e7;
}
.dynamic-title {
  font-weight: bold;
  font-size: 16px;
  color: #222;
  margin-bottom: 6px;
  margin-left: 2px;
}
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 180px; /* 根据实际需求调整宽度 */
  display: block;
}
.dynamic-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: inherit;
  width: 100%;
}
.dynamic-link:hover {
  background: #f6f7f8;
  border-radius: 8px;
}
</style>
