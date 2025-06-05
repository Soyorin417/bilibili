<template>
  <div class="content-view">
    <VideoBar />
    <div class="container">
      <DataNav currentPage="content" />
      <div class="main-content">
        <h2>内容管理</h2>
        <div class="content-section">
          <div class="content-header">
            <div class="search-box">
              <input type="text" placeholder="搜索内容..." />
              <i class="bi bi-search"></i>
            </div>
            <div class="filter-group">
              <select>
                <option value="">全部分区</option>
                <option value="video">视频</option>
                <option value="audio">音频</option>
                <option value="article">专栏</option>
              </select>
              <select>
                <option value="">全部状态</option>
                <option value="published">已发布</option>
                <option value="draft">草稿</option>
                <option value="review">审核中</option>
              </select>
            </div>
          </div>
          <div class="content-list">
            <div class="empty-state">
              <i class="bi bi-file-earmark-text"></i>
              <p>暂无内容</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import DataNav from "@/components/navBar/DataNav.vue";

export default {
  name: "ContentView",
  components: {
    VideoBar,
    DataNav,
  },
};
</script>

<style scoped>
.content-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 20px;
}

.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

.content-section {
  margin-top: 24px;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.search-box {
  position: relative;
  width: 300px;
}

.search-box input {
  width: 100%;
  padding: 8px 12px;
  padding-left: 36px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-box i {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #61666d;
}

.filter-group {
  display: flex;
  gap: 12px;
}

.filter-group select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  color: #61666d;
}

.content-list {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-state {
  text-align: center;
  color: #9499a0;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state p {
  margin: 0;
  font-size: 16px;
}
</style>
