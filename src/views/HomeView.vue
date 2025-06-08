<template>
  <div>
    <NavBar> </NavBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="card-body">
          <TopNav />
          <div class="custom-layout mt-3">
            <div class="left-section">
              <div
                id="carouselExampleIndicators"
                class="carousel slide"
                data-bs-ride="carousel"
              >
                <div class="carousel-indicators">
                  <button
                    v-for="(item, index) in loop_videoCards"
                    :key="index"
                    type="button"
                    data-bs-target="#carouselExampleIndicators"
                    :data-bs-slide-to="index"
                    :class="{ active: index === 0 }"
                    :aria-label="'Slide ' + (index + 1)"
                  ></button>
                </div>
                <div class="carousel-inner">
                  <div
                    class="carousel-item active"
                    v-for="(item, index) in loop_videoCards"
                    :key="index"
                  >
                    <img
                      :src="item.url"
                      class="d-block w-100 slide_img rounded"
                      alt="Slide 1"
                    />
                  </div>
                </div>
                <button
                  class="carousel-control-prev"
                  type="button"
                  data-bs-target="#carouselExampleIndicators"
                  data-bs-slide="prev"
                >
                  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Previous</span>
                </button>
                <button
                  class="carousel-control-next"
                  type="button"
                  data-bs-target="#carouselExampleIndicators"
                  data-bs-slide="next"
                >
                  <span class="carousel-control-next-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Next</span>
                </button>
              </div>
            </div>
            <div class="right-section">
              <div class="row row-cols-3">
                <router-link
                  v-for="(item, index) in videoCards"
                  :key="index"
                  class="col custom-link"
                  :to="`/video/${item.id}`"
                >
                  <div v-if="!loadedVideos[index]" class="placeholder-card">
                    <div class="placeholder-image"></div>
                    <div class="placeholder-content">
                      <div class="placeholder-title"></div>
                      <div class="placeholder-text"></div>
                      <div class="placeholder-text short"></div>
                    </div>
                  </div>
                  <VideoCard
                    v-else
                    :url="item.image"
                    :playCount="item.views"
                    :comment="item.comments"
                    :time="item.time"
                    :title="item.title"
                    :message="item.author"
                    :duration="item.duration"
                    class="mb-3"
                  />
                </router-link>
              </div>
            </div>
          </div>
          <div class="row row-cols-5">
            <router-link
              v-for="(item, index) in otherVideos"
              :key="index"
              class="col custom-link"
              :to="`/video/${item.id}`"
            >
              <div
                v-if="!loadedVideos[videoCards.length + index]"
                class="placeholder-card"
              >
                <div class="placeholder-image"></div>
                <div class="placeholder-content">
                  <div class="placeholder-title"></div>
                  <div class="placeholder-text"></div>
                  <div class="placeholder-text short"></div>
                </div>
              </div>
              <VideoCard
                v-else
                class="mb-3"
                :url="item.image"
                :playCount="item.views"
                :comment="item.comments"
                :time="item.time"
                :duration="item.duration"
                :title="item.title"
                :message="item.author"
              />
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoCard from "@/components/content/homeView/VideoCard.vue";
import NavBar from "@/components/navBar/NavBar.vue";
import TopNav from "@/components/navBar/TopNav.vue";
import { videoApi } from "@/api/content/video";
import { carouselApi } from "@/api/admin/carousel";
import { formatDateType } from "@/utils/date";
import { parseVideoCard } from "@/utils/videoCardParser";

export default {
  name: "HomeView",
  components: {
    VideoCard,
    NavBar,
    TopNav,
  },

  data() {
    return {
      videoInfos: [],
      videoCards: [],
      otherVideos: [],
      loop_videoCards: [],
      showPlaceholder: true,
      loadedVideos: [],
    };
  },
  created() {
    // 初始化所有视频为未加载状态
    const totalVideos = this.videoCards.length + this.otherVideos.length;
    this.loadedVideos = Array(totalVideos).fill(false);
  },
  methods: {
    async getVideoInfos() {
      try {
        const response = await videoApi.getVideoCards();
        console.log("获取到的视频数据:", response);
        if (response.data && Array.isArray(response.data)) {
          const videoInfosWithComments = response.data.map((video) => {
            return parseVideoCard(video);
          });

          this.videoInfos = videoInfosWithComments;
          this.videoCards = this.videoInfos.slice(0, 6);
          this.otherVideos = this.videoInfos.slice(6);

          this.loadedVideos = Array(this.videoInfos.length).fill(false);
          this.videoInfos.forEach((_, index) => {
            setTimeout(() => {
              this.loadedVideos[index] = true;
            }, 200 * (index + 1));
          });
        } else {
          throw new Error("Invalid video data format");
        }
      } catch (error) {
        console.error("获取视频列表失败:", error);
        alert("获取视频信息失败，请稍后再试");
      }
    },
    async getCarouselImages() {
      try {
        const response = await carouselApi.getAllCarousels();
        // 过滤出type为home的轮播图
        this.loop_videoCards = (response.data || [])
          .filter((item) => item.type === "home")
          .map((item) => ({
            id: item.id,
            url: item.url,
            title: item.title || "",
            message: item.description || "",
          }));
      } catch (error) {
        console.error("获取轮播图失败:", error);
      }
    },
  },
  mounted() {
    formatDateType,
      // 模拟网络延迟
      this.videoCards.forEach((item, index) => {
        setTimeout(() => {
          this.loadedVideos[index] = true;
        }, 200 * (index + 1)); // 每个视频卡片延迟2秒加载
      });

    // 对另一组视频也应用相同的逻辑
    this.otherVideos.forEach((item, index) => {
      setTimeout(() => {
        this.loadedVideos[this.videoCards.length + index] = true;
      }, 300 * (index + 1)); // 每个视频卡片延迟3秒加载
    });

    this.getVideoInfos();
    this.getCarouselImages();
  },
};
</script>

<style scoped>
.rounded-circle {
  width: 3em;
  height: 3em;
  display: flex;
  border-radius: 50%;
}

.tag {
  background-color: #f6f7f8;
  font-size: small;
  width: 88px; /* 调整宽度 */
  height: 35px; /* 调整高度 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 5px;
}
.tag:hover {
  background: #e3e5e7;
}
.slide_img {
  width: 390px;
  height: 390px;
}
.main_tag {
  width: 88px;
}

.page {
  width: 1700px;
}

/* 自定义 2:3 布局 */
.custom-layout {
  display: flex;
}

.left-section {
  flex: 2; /* 左侧占 2 份 */
  margin-right: 16px; /* 添加右侧间距 */
}

.right-section {
  flex: 3; /* 右侧占 3 份 */
}

.custom-link {
  text-decoration: none;
  color: inherit;
}

/* 占位卡片样式 */
.card[aria-hidden="true"] {
  width: 100% !important;
  max-width: 100% !important;
  margin-bottom: 1rem;
}

.card[aria-hidden="true"] .card-img-top {
  height: 160px !important;
  width: 100% !important;
  background-color: #e9ecef !important;
}

.card[aria-hidden="true"] .card-body {
  padding: 0.5rem !important;
  width: 100% !important;
}

.card[aria-hidden="true"] .card-title {
  margin-bottom: 0.5rem !important;
  width: 100% !important;
}

.card[aria-hidden="true"] .card-text {
  margin-bottom: 0.25rem !important;
  width: 100% !important;
}

.card[aria-hidden="true"] .placeholder {
  width: 100% !important;
}

.placeholder-card {
  width: 320px;
  height: 220px;
  overflow: hidden;
  background-color: #f0f2f5;
  border-radius: 4px;
}

.placeholder-image {
  width: 100%;
  height: 160px;
  background-color: #e9ecef;
  margin-bottom: 12px;
}

.placeholder-content {
  padding: 0 8px;
}

.placeholder-title {
  width: 100%;
  height: 16px;
  background-color: #e9ecef;
  margin-bottom: 12px;
  border-radius: 2px;
}

.placeholder-text {
  width: 70%;
  height: 12px;
  background-color: #e9ecef;
  margin-bottom: 8px;
  border-radius: 2px;
}

.placeholder-text.short {
  width: 50%;
}
</style>
