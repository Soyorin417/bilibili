<template>
  <div>
    <VideoBar />
    <div class="watch-history-view">
      <!-- 顶部栏 -->
      <div class="header">
        <i class="bi bi-clock-history"></i>
        <span class="title">历史记录</span>
        <div class="header-actions">
          <el-switch v-model="recordHistory" active-text="记录观看历史" />
          <el-input
            v-model="search"
            placeholder="搜索或输入up主/昵称"
            class="search-input"
          />
          <el-button>清除全部</el-button>
          <el-button>批量管理</el-button>
        </div>
      </div>

      <!-- Tabs -->
      <HistoryTabs :activeTab="activeTab" @change="activeTab = $event" />

      <div class="main-content">
        <!-- 时间线和内容区 -->
        <div class="timeline-content">
          <HistoryTimeline :groups="groupedHistory" />
          <div class="history-list">
            <HistoryCard v-for="item in filteredHistory" :key="item.id" :item="item" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import HistoryTabs from "@/components/user/history/HistoryTabs.vue";
import HistoryCard from "@/components/user/history/HistoryCard.vue";
import HistoryTimeline from "@/components/user/history/HistoryTimeline.vue";
import { getWatchHistory } from "@/api/user/watchApi";
import { parseVideoCard } from "@/utils/videoCardParser";

export default {
  name: "WatchHistoryView",
  components: {
    VideoBar,
    HistoryTabs,
    HistoryCard,
    HistoryTimeline,
  },
  async created() {
    const res = await getWatchHistory();
    const rawList = res.data || [];
    this.historyList = rawList.map(parseVideoCard);
  },
  data() {
    return {
      recordHistory: true,
      search: "",
      activeTab: "综合",
    };
  },
  computed: {
    filteredHistory() {
      // 根据activeTab和search过滤
      let list = this.historyList;
      if (this.activeTab !== "综合") {
        list = list.filter((item) => item.category === this.activeTab);
      }
      if (this.search) {
        list = list.filter(
          (item) => item.title.includes(this.search) || item.author.includes(this.search)
        );
      }
      return list;
    },
    groupedHistory() {
      return [
        {
          label: "近一周",
          items: this.historyList.filter((item) => {
            const itemTime = new Date(item.time);
            const now = new Date();
            const diff = (now - itemTime) / (1000 * 3600 * 24);
            return diff <= 7;
          }),
        },
      ];
    },
  },
};
</script>

<style scoped>
.watch-history-view {
  padding: 20px;

  min-height: 100vh;
  width: 100%;
  max-width: 1560px;
  margin: 0 auto;
  padding: 20px;
}
.header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}
.header .title {
  font-size: 22px;
  font-weight: bold;
  margin-right: 24px;
}
.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
}
.search-input {
  width: 220px;
}
.main-content {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  min-height: 600px;
}
.timeline-content {
  display: flex;
  flex-direction: row;
}
.history-list {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  flex: 1;
}
</style>
