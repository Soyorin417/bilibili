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
          <el-button @click="clearAll">清除全部</el-button>
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
            <HistoryCard
              v-for="item in filteredHistory"
              :key="item.id"
              :item="item"
              @delete="deleteOne"
            />
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
import { watchApi } from "@/api/user/watchApi";
import { parseVideoCard } from "@/utils/videoCardParser";
import { ElMessage, ElMessageBox } from "element-plus";

export default {
  name: "WatchHistoryView",
  components: {
    VideoBar,
    HistoryTabs,
    HistoryCard,
    HistoryTimeline,
  },
  async created() {
    await this.fetchHistory();
  },
  data() {
    return {
      recordHistory: true,
      search: "",
      activeTab: "综合",
      historyList: [],
    };
  },
  methods: {
    async fetchHistory() {
      const res = await watchApi.getWatchHistory();
      if (res?.data && Array.isArray(res.data)) {
        this.historyList = res.data.map(parseVideoCard);
      } else {
        console.warn("Watch history data is not an array:", res?.data);
        this.historyList = [];
      }
    },
    async deleteOne(videoId) {
      try {
        await ElMessageBox.confirm("确定要删除这条观看记录吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        });
        const res = await watchApi.deleteOne(videoId);
        if (res.data === "删除成功") {
          ElMessage.success("删除成功");
          await this.fetchHistory();
        } else {
          ElMessage.error(res.data || "删除失败");
        }
      } catch (error) {
        if (error !== "cancel") {
          console.error("删除失败:", error);
          ElMessage.error("删除失败");
        }
      }
    },
    async clearAll() {
      try {
        await ElMessageBox.confirm("确定要清空所有观看历史吗？此操作不可恢复！", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        });
        const res = await watchApi.clearAll();
        if (res.data === "已清空历史记录") {
          ElMessage.success("已清空历史记录");
          this.historyList = [];
        } else {
          ElMessage.error("清空失败");
        }
      } catch (error) {
        if (error !== "cancel") {
          console.error("清空失败:", error);
          ElMessage.error("清空失败");
        }
      }
    },
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
