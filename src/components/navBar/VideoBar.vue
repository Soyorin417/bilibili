<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid position-absolute top-0 start-0">
      <div class="flex-shrink-0">
        <LeftNavMenu :fill="currentColor" />
      </div>
      <div class="flex-grow-1 d-flex justify-content-center">
        <HotSearchList
          :search-history="searchHistory"
          :history-tags="historyTags"
          :searchQuery="searchQuery"
          :isSearchFocused="isSearchFocused"
          :hot-search-items="hotSearchItems"
          @update:searchQuery="searchQuery = $event"
          @update:isSearchFocused="isSearchFocused = $event"
          @search="handleSearch"
          @clearHistory="clearHistory"
          @setSearchQuery="setSearchQuery"
        />
      </div>
      <div class="flex-shrink-0">
        <NavBarRightMenu :navbar-dynamics="navbarDynamics" :fill="currentColor" />
      </div>
    </div>
  </nav>
</template>

<script>
import HotSearchList from "@/components/navBar/search/HotSearchList.vue";
import LeftNavMenu from "@/components/navBar/NavBarLeftMenu.vue";
import NavBarRightMenu from "@/components/navBar/NavBarRightMenu.vue";
import axios from "axios";
export default {
  name: "VideoBar",
  computed: {
    hotSearchColumns() {
      const col1 = this.hotSearchItems.slice(0, 5);
      const col2 = this.hotSearchItems.slice(5, 10);
      return [col1, col2];
    },
  },
  components: {
    HotSearchList,
    NavBarRightMenu,
    LeftNavMenu,
  },
  mounted() {
    this.fetchNavbarDynamics();
  },
  data() {
    return {
      navbarDynamics: [],
      currentColor: "#000",

      // 搜索相关
      searchQuery: "",
      isSearchFocused: false,
      searchHistory: ["一等情事", "bilibili icon", "icon图标", "明日方舟", "小祥"],
      historyTags: [],
      hotSearchItems: [
        { title: "TES战胜NIP", tag: "热", tagType: "hot-tag-red" },
        { title: "中国队将不参加今年LCG杯", tag: "热", tagType: "hot-tag-red" },
        { title: "特朗普准备降低关税的背后", tag: "热", tagType: "hot-tag-red" },
        { title: "Zmiikk 我更喜欢你", tag: "热", tagType: "hot-tag-red" },
        { title: "万年前灭绝的恐龙复活", tag: "", tagType: "" },
        { title: "刘国梁辞职", tag: "", tagType: "" },
        { title: "如何拍出车展高级感", tag: "", tagType: "" },
        { title: "上海车展惊现攻略", tag: "", tagType: "" },
        { title: "AI少女漫展只在一瞬间", tag: "新", tagType: "hot-tag-new" },
        { title: "本科新增游戏艺术设计专业", tag: "", tagType: "" },
      ],
    };
  },
  methods: {
    handleSearchFocus() {
      this.isSearchFocused = true;
    },
    handleSearchBlur() {
      setTimeout(() => {
        this.isSearchFocused = false;
      }, 200);
    },
    handleSearch() {
      if (this.searchQuery.trim()) {
        // 添加到搜索历史和标签
        if (!this.searchHistory.includes(this.searchQuery)) {
          this.searchHistory.unshift(this.searchQuery);
          this.historyTags.unshift(this.searchQuery);
          // 限制历史记录数量
          if (this.searchHistory.length > 10) {
            this.searchHistory.pop();
            this.historyTags.pop();
          }
        }
        console.log("搜索:", this.searchQuery);
        this.$router.push({
          path: "/search",
          query: { keyword: this.searchQuery },
        });
      }
    },
    clearHistory() {
      this.searchHistory = [];
      this.historyTags = [];
    },
    setSearchQuery(query) {
      this.searchQuery = query;
      this.handleSearch();
    },
    formatHotTitle(title) {
      if (!title) return "";
      return title.length > 10 ? title.slice(0, 10) + "..." : title;
    },
    async fetchNavbarDynamics() {
      try {
        const res = await axios.get("http://127.0.0.1:8081/api/activity/right");
        this.navbarDynamics = res.data;
        console.log(this.navbarDynamics, "navbarDynamics");
      } catch (e) {
        console.error("获取动态失败", e);
      }
    },
  },
};
</script>

<style scoped>
.navbar {
  padding: 0.5rem 1rem;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 60px;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.nav-flex {
  display: flex;
  align-items: center;
}
.flex-grow-1 {
  flex-grow: 1;
  display: flex;
  justify-content: center;
}
.flex-shrink-0 {
  flex-shrink: 0;
}
</style>
