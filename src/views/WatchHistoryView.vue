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
import HistoryTabs from "@/components/history/HistoryTabs.vue";
import HistoryCard from "@/components/history/HistoryCard.vue";
import HistoryTimeline from "@/components/history/HistoryTimeline.vue";

const mockHistory = [
  {
    id: 1,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:10:35",
    title: "辐射避难所",
    author: "zjsj2018",
    time: "04-30 23:57",
    category: "命运冠位",
  },
  {
    id: 2,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:17:36",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
    time: "04-30 23:57",
    category: "命运契约",
  },
  {
    id: 3,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
    time: "04-30 23:57",
    category: "命运契约",
  },
  {
    id: 4,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 5,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 6,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 7,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 8,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 9,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
  {
    id: 10,
    cover: "http://113.45.69.13:9000/image/f00e3360ca0bd6f65002dd45afa1d6fbcb1e3b1c.jpg",
    duration: "00:15:20",
    title: "命运之翼！CG首曝！",
    author: "命运契约",
  },
];

export default {
  name: "WatchHistoryView",
  components: {
    VideoBar,
    HistoryTabs,
    HistoryCard,
    HistoryTimeline,
  },
  data() {
    return {
      recordHistory: true,
      search: "",
      activeTab: "综合",
      historyList: mockHistory,
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
      // 按时间分组（这里只做静态分组示例）
      return [
        { label: "近一周" },
        // 可扩展更多分组
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
