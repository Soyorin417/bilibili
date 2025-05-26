<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid nav-flex position-absolute top-0 start-0">
      <div class="flex-shrink-0">
        <LeftNavMenu :tags="tags" :fill="currentColor" />
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
import NavBarRightMenu from "@/components/navBar/NavBarRightMenu.vue";
import LeftNavMenu from "@/components/navBar/NavBarLeftMenu.vue";
import axios from "axios";

export default {
  name: "NavBar",
  components: {
    HotSearchList,
    NavBarRightMenu,
    LeftNavMenu,
  },
  data() {
    return {
      currentColor: "#ffffff",

      searchQuery: "",
      isSearchFocused: false,
      searchHistory: [
        "缘之空",
        "bilibili icon",
        "icon图标",
        "明日方舟",
        "妈妈妈妈你看我有",
      ],
      historyTags: [
        "缘之空",
        "bilibili icon",
        "icon图标",
        "明日方舟",
        "妈妈妈妈你看我有",
      ],
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
      navbarDynamics: [],
    };
  },
  mounted() {
    this.fetchNavbarDynamics();
  },
  methods: {
    async fetchNavbarDynamics() {
      try {
        const res = await axios.get("http://127.0.0.1:8081/api/activity/right");
        this.navbarDynamics = res.data;
      } catch (e) {
        console.error("获取动态失败", e);
      }
    },
    handleSearch() {
      if (this.searchQuery.trim()) {
        if (!this.historyTags.includes(this.searchQuery)) {
          this.historyTags.unshift(this.searchQuery);
          if (this.historyTags.length > 10) {
            this.historyTags.pop();
          }
        }
        this.$router.push({
          path: "/search",
          query: { keyword: this.searchQuery },
        });
      }
    },
    clearHistory() {
      this.historyTags = [];
    },
    setSearchQuery(query) {
      this.searchQuery = query;
      this.handleSearch();
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
  height: 180px;
  background-image: url("http://113.45.69.13:9000/image/4da345730163c365225ed6c489a069a89a82cdd7.png");
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
