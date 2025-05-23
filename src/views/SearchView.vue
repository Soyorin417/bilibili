<template>
  <div class="search-view">
    <VideoBar />
    <div class="search-container">
      <!-- 分类导航 -->
      <div class="category-nav">
        <div class="nav-items">
          <router-link to="/search?type=all" class="nav-item active">综合</router-link>
          <router-link to="/search?type=video" class="nav-item">
            视频
            <span class="count">99+</span>
          </router-link>
          <router-link to="/search?type=bangumi" class="nav-item">
            番剧
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=movie" class="nav-item">
            影视
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=live" class="nav-item">
            直播
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=article" class="nav-item">
            专栏
            <span class="count">31</span>
          </router-link>
          <router-link to="/search?type=user" class="nav-item">
            用户
            <span class="count">0</span>
          </router-link>
        </div>
      </div>

      <!-- 二级筛选 -->
      <div class="sub-filters">
        <div class="filter-tags">
          <span class="filter-tag active">综合排序</span>
          <span class="filter-tag">最多播放</span>
          <span class="filter-tag">最新发布</span>
          <span class="filter-tag">最多收藏</span>
          <span class="filter-tag">最多弹幕</span>
        </div>
        <div class="more-filters">
          更多筛选
          <i class="bi bi-chevron-down"></i>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results">
        <div v-if="loading" class="loading">
          <div class="spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="results.length === 0" class="no-results">
          <i class="bi bi-search"></i>
          <p>没有找到相关内容</p>
        </div>
        <div v-else class="results-grid">
          <div v-for="(item, index) in results" :key="index" class="result-card">
            <router-link :to="`/video/${item.id}`" class="video-link" target="_blank">
              <div class="video-thumbnail">
                <img :src="item.image" :alt="item.title" />
                <span class="video-duration">{{ item.duration }}</span>
                <div class="video-stats">
                  <span><i class="bi bi-play-fill"></i> {{ item.views }}</span>
                  <span><i class="bi bi-chat-fill"></i> {{ item.danmaku }}</span>
                </div>
              </div>
              <div class="video-info">
                <h3 class="video-title">{{ item.title }}</h3>
                <div class="video-meta">
                  <div class="uploader-info">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      xmlns:xlink="http://www.w3.org/1999/xlink"
                      viewBox="0 0 24 24"
                      width="16"
                      height="16"
                      class="bili-video-card__info--author-ico mr_2"
                      data-v-603f41ad=""
                    >
                      <!--[-->
                      <path
                        d="M6.15 8.24805C6.5642 8.24805 6.9 8.58383 6.9 8.99805L6.9 12.7741C6.9 13.5881 7.55988 14.248 8.3739 14.248C9.18791 14.248 9.8478 13.5881 9.8478 12.7741L9.8478 8.99805C9.8478 8.58383 10.1836 8.24805 10.5978 8.24805C11.012 8.24805 11.3478 8.58383 11.3478 8.99805L11.3478 12.7741C11.3478 14.41655 10.01635 15.748 8.3739 15.748C6.73146 15.748 5.4 14.41655 5.4 12.7741L5.4 8.99805C5.4 8.58383 5.73578 8.24805 6.15 8.24805z"
                        fill="currentColor"
                      ></path>
                      <path
                        d="M12.6522 8.99805C12.6522 8.58383 12.98795 8.24805 13.4022 8.24805L15.725 8.24805C17.31285 8.24805 18.6 9.53522 18.6 11.123C18.6 12.71085 17.31285 13.998 15.725 13.998L14.1522 13.998L14.1522 14.998C14.1522 15.4122 13.8164 15.748 13.4022 15.748C12.98795 15.748 12.6522 15.4122 12.6522 14.998L12.6522 8.99805zM14.1522 12.498L15.725 12.498C16.4844 12.498 17.1 11.8824 17.1 11.123C17.1 10.36365 16.4844 9.74804 15.725 9.74804L14.1522 9.74804L14.1522 12.498z"
                        fill="currentColor"
                      ></path>
                      <path
                        d="M12 4.99805C9.48178 4.99805 7.283 5.12616 5.73089 5.25202C4.65221 5.33949 3.81611 6.16352 3.72 7.23254C3.60607 8.4998 3.5 10.171 3.5 11.998C3.5 13.8251 3.60607 15.4963 3.72 16.76355C3.81611 17.83255 4.65221 18.6566 5.73089 18.7441C7.283 18.8699 9.48178 18.998 12 18.998C14.5185 18.998 16.7174 18.8699 18.2696 18.74405C19.3481 18.65655 20.184 17.8328 20.2801 16.76405C20.394 15.4973 20.5 13.82645 20.5 11.998C20.5 10.16965 20.394 8.49877 20.2801 7.23205C20.184 6.1633 19.3481 5.33952 18.2696 5.25205C16.7174 5.12618 14.5185 4.99805 12 4.99805zM5.60965 3.75693C7.19232 3.62859 9.43258 3.49805 12 3.49805C14.5677 3.49805 16.8081 3.62861 18.3908 3.75696C20.1881 3.90272 21.6118 5.29278 21.7741 7.09773C21.8909 8.3969 22 10.11405 22 11.998C22 13.88205 21.8909 15.5992 21.7741 16.8984C21.6118 18.7033 20.1881 20.09335 18.3908 20.23915C16.8081 20.3675 14.5677 20.498 12 20.498C9.43258 20.498 7.19232 20.3675 5.60965 20.2392C3.81206 20.0934 2.38831 18.70295 2.22603 16.8979C2.10918 15.5982 2 13.8808 2 11.998C2 10.1153 2.10918 8.39787 2.22603 7.09823C2.38831 5.29312 3.81206 3.90269 5.60965 3.75693z"
                        fill="currentColor"
                      ></path>
                      <!--]-->
                    </svg>
                    <span class="uploader-name">{{ item.uploader }}</span>
                    <span class="upload-time">{{ item.date }}</span>
                  </div>
                </div>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import { mapGetters } from "vuex";
import axios from "axios";

export default {
  name: "SearchView",
  components: {
    VideoBar,
  },
  data() {
    return {
      keyword: "",
      loading: true,
      totalResults: 0,
      currentFilter: "all",
      results: [],
      videoInfos: [],
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  created() {
    this.keyword = this.$route.query.keyword || "";
    this.fetchSearchResults();
  },
  methods: {
    async fetchSearchResults() {
      this.loading = true;
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      try {
        const response = await axios.get("http://127.0.0.1:8081/video/getAllVideo", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // 处理成功响应
        this.videoInfos = response.data;
        console.log("videoInfos:", this.videoInfos[0].image);
        this.results = this.videoInfos.map((video) => ({
          id: video.id,
          title: video.title,
          image: video.image,
          duration: video.duration || "0:00",
          uploader: video.uploader,
          uploaderAvatar: video.uploaderAvatar,
          views: this.formatNumber(video.views || 0),
          danmaku: this.formatNumber(video.danmaku_count || 0),
          date: this.formatDate(video.time),
        }));

        // 获取每个视频的互动状态
        if (this.userInfo && this.userInfo.id) {
          await Promise.all(
            this.videoInfos.map(async (video) => {
              try {
                const actionResponse = await axios.get(
                  `http://127.0.0.1:8081/video/action/status/${video.id}?userUid=${this.userInfo.id}`,
                  {
                    headers: {
                      Authorization: `Bearer ${token}`,
                    },
                  }
                );
                if (actionResponse.data) {
                  // 更新视频的互动状态
                  video.isLiked = actionResponse.data.isLiked;
                  video.isCollected = actionResponse.data.isCollected;
                  video.isCoined = actionResponse.data.isCoined;
                  video.isShared = actionResponse.data.isShared;
                }
              } catch (error) {
                console.error(`获取视频 ${video.id} 的互动状态失败:`, error);
              }
            })
          );
        }

        this.totalResults = this.results.length;
      } catch (error) {
        console.error("获取视频列表失败:", error);
        alert("获取视频信息失败，请稍后再试");
      } finally {
        this.loading = false;
      }
    },

    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + "万";
      }
      return num.toString();
    },

    formatDate(dateString) {
      if (!dateString) return "";
      const date = new Date(dateString);
      const now = new Date();
      const diff = now - date;

      // 小于24小时
      if (diff < 24 * 60 * 60 * 1000) {
        const hours = Math.floor(diff / (60 * 60 * 1000));
        return `${hours}小时前`;
      }
      // 小于30天
      if (diff < 30 * 24 * 60 * 60 * 1000) {
        const days = Math.floor(diff / (24 * 60 * 60 * 1000));
        return `${days}天前`;
      }
      // 大于30天
      return `${date.getMonth() + 1}-${date.getDate()}`;
    },
  },
  watch: {
    "$route.query": {
      handler() {
        this.fetchSearchResults();
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
.search-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.search-container {
  max-width: 1700px;
  margin: 0 auto;
  padding: 16px;
}

.category-nav {
  background: #fff;
  padding: 0 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.nav-items {
  display: flex;
  align-items: center;
  height: 46px;
}

.nav-item {
  position: relative;
  padding: 0 16px;
  color: #61666d;
  text-decoration: none;
  font-size: 14px;
  display: flex;
  align-items: center;
  height: 100%;
}

.nav-item.active {
  color: #00aeec;
  font-weight: 500;
}

.nav-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 16px;
  right: 16px;
  height: 2px;
  background-color: #00aeec;
}

.count {
  margin-left: 4px;
  font-size: 12px;
  color: #9499a0;
}

.sub-filters {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.filter-tags {
  display: flex;
  gap: 24px;
}

.filter-tag {
  color: #61666d;
  font-size: 14px;
  cursor: pointer;
  padding: 6px 0;
}

.filter-tag:hover {
  color: #00aeec;
}

.filter-tag.active {
  color: #00aeec;
  font-weight: 500;
}

.more-filters {
  color: #61666d;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.result-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-link {
  text-decoration: none;
  color: inherit;
}

.video-thumbnail {
  position: relative;
  padding-top: 56.25%;
}

.video-thumbnail::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(to bottom, transparent, rgba(0, 0, 0, 0.7));
  pointer-events: none;
}

.video-thumbnail img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.video-stats {
  position: absolute;
  bottom: 8px;
  left: 8px;
  font-size: 12px;
  display: flex;
  gap: 12px;
  color: #fff;
  z-index: 1;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  padding: 2px 4px;
  border-radius: 4px;
  font-size: 12px;
  color: #fff;
  z-index: 1;
}

.video-info {
  padding: 8px;
}

.video-title {
  font-size: 15px;
  line-height: 1.5;
  height: 22px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  font-weight: 500;
  text-align: left;
}

.video-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #9499a0;
}

.uploader-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.uploader-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #9499a0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #00aeec;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 12px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #9499a0;
}

.no-results i {
  font-size: 48px;
  margin-bottom: 12px;
  display: block;
}
</style>
