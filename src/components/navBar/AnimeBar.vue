<template>
  <nav
    class="navbar navbar-expand-lg"
    :style="{ backgroundImage: `url(${currentBackground})` }"
  >
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
    <div class="carousel">
      <div
        class="card"
        v-for="(image, index) in images"
        :key="index"
        :class="{ active: currentIndex === index }"
        @mouseenter="setBackground(image, index)"
        @mouseleave="resetBackground"
      >
        <div class="body">
          <img :src="image" alt="" />
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import axios from "axios";
import HotSearchList from "@/components/navBar/search/HotSearchList.vue";
import NavBarRightMenu from "@/components/navBar/NavBarRightMenu.vue";
import LeftNavMenu from "@/components/navBar/NavBarLeftMenu.vue";
export default {
  name: "AnimeBar",
  components: {
    HotSearchList,
    NavBarRightMenu,
    LeftNavMenu,
  },
  props: {
    url: {
      type: String,
      required: true,
    },
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
      isCardVisible: false,
      isAvatarVisible: true,
      historyTags: [
        "译之空",
        "bilibili icon",
        "icon图标",
        "明日方舟",
        "妈妈妈妈你看我有",
      ],

      images: [],
      currentBackground: "",
      currentIndex: 0,
      timer: null,
      isHovered: false,
    };
  },
  computed: {
    hotSearchColumns() {
      // 只取前10个，分成两列
      const items = this.hotSearchItems.slice(0, 10);
      const col1 = items.slice(0, 5);
      const col2 = items.slice(5, 10);
      return [col1, col2];
    },
  },
  mounted() {
    this.currentBackground = this.url;
    this.startAutoSwitch();
    this.getImages();
    this.fetchNavbarDynamics();
  },
  beforeUnmount() {
    this.stopAutoSwitch();
  },
  methods: {
    async fetchNavbarDynamics() {
      try {
        const res = await axios.get("http://127.0.0.1:8081/api/activity/right");
        this.navbarDynamics = res.data;
        console.log(this.navbarDynamics, "navbarDynamics");
      } catch (e) {
        console.error("获取动态失败", e);
      }
    },
    async getImages() {
      const url = "http://localhost:8081/getAllCarousel";
      const token = localStorage.getItem("token");
      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.images = response.data.map((item) => item.url);
        console.log(this.images, "images");
      } catch (error) {
        console.error("获取轮播图失败:", error);
      }
    },

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

    //随时间切换
    startAutoSwitch() {
      this.timer = setInterval(() => {
        if (!this.isHovered) {
          this.switchToNext();
        }
      }, 5000);
    },
    stopAutoSwitch() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },
    switchToNext() {
      this.currentIndex = (this.currentIndex + 1) % this.images.length;
      this.currentBackground = this.images[this.currentIndex];
    },
    setBackground(image, index) {
      this.isHovered = true;
      this.currentBackground = image;
      this.currentIndex = index;
    },
    resetBackground() {
      this.isHovered = false;
    },
  },
};
</script>

<style scoped>
/* 导航栏容器样式 */
.navbar {
  padding: 0.5rem 1rem;
  height: 700px;
  position: relative;
  overflow: hidden;
  background-size: cover;
  background-position: center;
  transition: background-image 0.5s ease; /* 背景图切换动画 */
}

/* 底部轮播图容器 */
.carousel {
  position: absolute;
  bottom: 10px; /* 距离底部距离 */
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  gap: 20px; /* 卡片之间的间距 */
  z-index: 1;
}

/* 轮播图卡片样式 */
.card {
  width: 200px; /* 卡片宽度 - 可调整 */
  height: 120px; /* 卡片高度 - 可调整 */
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease; /* 所有属性变化动画 */
  opacity: 0.7;
  border: 2px solid transparent;
  position: relative; /* 为进度条定位 */
}

/* 卡片悬浮和激活状态 */
.card:hover,
.card.active {
  transform: translateY(-5px); /* 上移距离 - 可调整 */
  opacity: 1;
  border-color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 激活状态添加底部进度条 */
.card.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  height: 3px; /* 进度条高度 - 可调整 */
  background-color: #00a1d6; /* 进度条颜色 - 可调整 */
  animation: progress 5s linear; /* 进度条动画 - 对应自动切换时间 */
}

/* 进度条动画 */
@keyframes progress {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

/* 卡片内容容器 */
.card .body {
  width: 100%;
  height: 100%;
}

/* 卡片图片样式 */
.card img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 图片填充方式：保持比例填充 */
}

/* 导航内容容器 */
.container-fluid {
  position: relative;
  z-index: 2;
}

.navbar::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200px;
  background: linear-gradient(
    to bottom,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.3) 20%,
    rgba(255, 255, 255, 0.6) 40%,
    rgba(255, 255, 255, 0.8) 60%,
    rgba(255, 255, 255, 0.9) 80%,
    rgba(255, 255, 255, 1) 100%
  );
  pointer-events: none;
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
