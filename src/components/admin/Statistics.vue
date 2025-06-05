<template>
  <div>
    <h4>数据统计</h4>
    <div class="stats-grid">
      <div class="stat-card">
        <h5>总用户数</h5>
        <div class="stat-value">{{ stats.totalUsers }}</div>
      </div>
      <div class="stat-card">
        <h5>总视频数</h5>
        <div class="stat-value">{{ stats.totalVideos }}</div>
      </div>
      <div class="stat-card">
        <h5>待审核</h5>
        <div class="stat-value">{{ stats.pendingReviews }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import adminApi from "@/api/admin/admin";

export default {
  name: "StatisticsPanel",
  data() {
    return {
      stats: {
        totalUsers: 0,
        totalVideos: 0,
        pendingReviews: 0,
      },
    };
  },
  methods: {
    async fetchStats() {
      try {
        const [userCount, videoCount, pendingVideos] = await Promise.all([
          adminApi.getUserCount(),
          adminApi.getVideoCount(),
          adminApi.getPendingVideos(),
        ]);

        this.stats = {
          totalUsers: userCount.data || 0,
          totalVideos: videoCount.data || 0,
          pendingReviews: pendingVideos.data?.length || 0,
        };
      } catch (error) {
        console.error("获取统计数据失败:", error);
        this.$message.error("获取统计数据失败");
      }
    },
  },
  mounted() {
    this.fetchStats();
  },
};
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-card h5 {
  color: #666;
  margin-bottom: 10px;
  font-size: 16px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
}
</style>
