<template>
  <div class="history-view">
    <VideoBar />
    <div class="container">
      <!-- 左侧导航 -->
      <DataNav currentPage="history" />

      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 筛选器 -->
        <div class="filters">
          <div class="search-box">
            <input type="text" placeholder="搜索投稿..." v-model="searchQuery" />
            <i class="bi bi-search"></i>
          </div>
          <div class="filter-group">
            <select v-model="contentType">
              <option value="">全部类型</option>
              <option value="video">视频</option>
              <option value="audio">音频</option>
              <option value="article">专栏</option>
            </select>
            <select v-model="status">
              <option value="">全部状态</option>
              <option value="approved">已通过</option>
              <option value="draft">草稿</option>
              <option value="pending">审核中</option>
              <option value="rejected">未通过</option>
            </select>
          </div>
        </div>

        <!-- 投稿列表 -->
        <div class="upload-list">
          <div v-if="filteredUploads.length === 0" class="empty-state">
            <i class="bi bi-file-earmark-text"></i>
            <p>暂无投稿记录</p>
          </div>
          <div v-else class="upload-items">
            <div
              v-for="upload in filteredUploads"
              :key="upload.id"
              class="upload-item"
              @click="goToVideo(upload)"
            >
              <div class="upload-cover">
                <img :src="upload.cover" :alt="upload.title" />
                <div class="upload-type" :class="upload.type">
                  {{
                    upload.type === "video"
                      ? "视频"
                      : upload.type === "audio"
                      ? "音频"
                      : "专栏"
                  }}
                </div>
              </div>
              <div class="upload-info">
                <h3 class="upload-title">{{ upload.title }}</h3>
                <div class="upload-meta">
                  <span class="upload-time">{{ upload.time }}</span>
                  <span class="upload-status" :class="upload.status">
                    {{ getStatusText(upload.status) }}
                  </span>
                </div>
                <div class="upload-stats">
                  <span v-if="upload.views">
                    <i class="bi bi-eye"></i>
                    {{ upload.views }}
                  </span>
                  <span v-if="upload.likes">
                    <i class="bi bi-hand-thumbs-up"></i>
                    {{ upload.likes }}
                  </span>
                  <span v-if="upload.comments">
                    <i class="bi bi-chat"></i>
                    {{ upload.comments }}
                  </span>
                </div>
              </div>
              <div class="upload-actions" @click.stop>
                <button class="action-btn" @click="editUpload(upload)">
                  <i class="bi bi-pencil"></i>
                  编辑
                </button>
                <button class="action-btn" @click="deleteUpload(upload)">
                  <i class="bi bi-trash"></i>
                  删除
                </button>
              </div>
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
import { videoApi } from "@/api/content/video";
import { formatDate, formatCount, limitTextLength, goToVideo } from "@/utils/date";

export default {
  name: "UploadHistoryView",
  components: {
    VideoBar,
    DataNav,
  },
  data() {
    return {
      searchQuery: "",
      contentType: "",
      status: "",
      uploads: [],
    };
  },
  computed: {
    filteredUploads() {
      return this.uploads.filter((upload) => {
        const matchesSearch = upload.title
          .toLowerCase()
          .includes(this.searchQuery.toLowerCase());
        const matchesType = !this.contentType || upload.type === this.contentType;
        const matchesStatus = !this.status || upload.status === this.status;
        return matchesSearch && matchesType && matchesStatus;
      });
    },
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        draft: "草稿",
        rejected: "未通过",
        approved: "已通过",
        pending: "审核中",
      };
      return statusMap[status] || status;
    },

    editUpload(upload) {
      // 实现编辑功能
      console.log("编辑投稿:", upload);
    },
    async deleteUpload(upload) {
      if (!upload.id) return;
      if (!confirm("确定要删除该投稿吗？")) return;
      try {
        await videoApi.deleteVideo(upload.id);
        this.uploads = this.uploads.filter((item) => item.id !== upload.id);
        this.$message && this.$message.success
          ? this.$message.success("删除成功")
          : alert("删除成功");
      } catch (e) {
        this.$message && this.$message.error
          ? this.$message.error("删除失败")
          : alert("删除失败");
        console.error("删除投稿失败:", e);
      }
    },
    async fetchUploads() {
      try {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        if (!userInfo || !userInfo.id) {
          this.uploads = [];
          return;
        }
        const res = await videoApi.getUserVideos(userInfo.id);
        console.log("投稿历史返回：", res.data);
        const list = Array.isArray(res.data)
          ? res.data
          : Array.isArray(res.data.data)
          ? res.data.data
          : [];
        this.uploads = list.map((item) => ({
          id: item.id,
          title: limitTextLength(item.title),
          cover: item.image || item.cover || "",
          type: item.type || "video",
          status: item.status || "approved",
          time: formatDate(item.time || item.createTime || ""),
          views: formatCount(item.views) || 0,
          likes: formatCount(item.likeCount) || item.likes || 0,
          comments: formatCount(item.comments) || item.commentCount || 0,
        }));
      } catch (e) {
        console.error("获取投稿历史失败:", e);
        this.uploads = [];
      }
    },
    goToVideo(upload) {
      goToVideo(this.$router, upload.id);
    },
  },
  mounted() {
    this.fetchUploads();
  },
};
</script>

<style scoped>
.history-view {
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

/* 筛选器样式 */
.filters {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.search-box {
  flex: 1;
  position: relative;
}

.search-box input {
  width: 100%;
  padding: 8px 12px;
  padding-left: 36px;
  border: 1px solid #e3e5e7;
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
  gap: 8px;
}

.filter-group select {
  padding: 8px 12px;
  border: 1px solid #e3e5e7;
  border-radius: 4px;
  background: white;
  color: #61666d;
  font-size: 14px;
}

/* 投稿列表样式 */
.upload-list {
  margin-top: 16px;
}

.empty-state {
  text-align: center;
  padding: 48px;
  color: #61666d;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 16px;
}

.upload-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.upload-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.upload-item:hover {
  background: #f0f2f4;
}

.upload-cover {
  position: relative;
  width: 160px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
}

.upload-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-type {
  position: absolute;
  top: 8px;
  right: 8px;
  padding: 2px 6px;
  border-radius: 2px;
  font-size: 12px;
  color: white;
}

.upload-type.video {
  background: rgba(0, 174, 236, 0.8);
}

.upload-type.audio {
  background: rgba(82, 196, 26, 0.8);
}

.upload-type.article {
  background: rgba(250, 173, 20, 0.8);
}

.upload-info {
  flex: 1;
}

.upload-title {
  margin: 0 0 8px;
  font-size: 16px;
  color: #18191c;
  text-align: left;
}

.upload-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  font-size: 14px;
  color: #61666d;
}

.upload-status {
  padding: 2px 6px;
  border-radius: 2px;
  font-size: 12px;
}

.upload-status.published {
  background: #e6f7ff;
  color: #00aeec;
}

.upload-status.draft {
  background: #f5f5f5;
  color: #61666d;
}

.upload-status.review {
  background: #fff7e6;
  color: #faad14;
}

.upload-status.rejected {
  background: #fff1f0;
  color: #ff4d4f;
}

.upload-stats {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #61666d;
}

.upload-stats i {
  margin-right: 4px;
}

.upload-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  border: 1px solid #e3e5e7;
  border-radius: 4px;
  background: white;
  color: #61666d;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  background: #f4f4f4;
  color: #00aeec;
  border-color: #00aeec;
}
</style>
