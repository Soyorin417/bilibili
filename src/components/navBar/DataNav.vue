<template>
  <div class="upload-nav">
    <div class="nav-header">
      <h3>投稿管理</h3>
    </div>
    <div class="nav-items">
      <router-link class="nav-item" to="/">
        <i class="bi bi-house"></i>
        <span>首页</span>
      </router-link>
      <router-link class="nav-item" to="/content">
        <i class="bi bi-file-earmark"></i>
        <span>内容管理</span>
      </router-link>
      <router-link class="nav-item" to="/upload">
        <i class="bi bi-cloud-upload"></i>
        <span>视频投稿</span>
      </router-link>
      <router-link class="nav-item" to="/data">
        <i class="bi bi-bar-chart"></i>
        <span>数据统计</span>
      </router-link>
      <router-link class="nav-item" to="/audio">
        <i class="bi bi-collection"></i>
        <span>音频投稿</span>
      </router-link>
      <router-link class="nav-item" to="/article">
        <i class="bi bi-pencil-square"></i>
        <span>专栏投稿</span>
      </router-link>
      <router-link class="nav-item" to="/upload-history">
        <i class="bi bi-camera-video"></i>
        <span>投稿记录</span>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "DataNav",
  data() {
    return {
      stats: [
        { type: "视频", count: 120 },
        { type: "音频", count: 30 },
        { type: "专栏", count: 15 },
      ],
    };
  },
};
</script>

<style scoped>
.upload-nav {
  width: 200px;
  background: white;
  border-radius: 8px;
  padding: 16px 0;
}

.nav-header {
  padding: 0 16px 16px;
  border-bottom: 1px solid #f4f4f4;
}

.nav-header h3 {
  margin: 0;
  font-size: 16px;
  color: #18191c;
}

.nav-items {
  padding: 8px 0;
}

.nav-item {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #61666d;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.nav-item:hover {
  background: #f4f4f4;
  color: #00aeec;
}

.router-link-exact-active.nav-item,
.nav-item.router-link-active {
  color: #00aeec;
  background: #f4f4f4;
}

.nav-item i {
  font-size: 18px;
}
</style>
