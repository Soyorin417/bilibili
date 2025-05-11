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
                    :playCount="item.playCount"
                    :comment="item.comment"
                    :time="item.duration"
                    :title="item.title"
                    :message="item.author"
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
                :playCount="item.playCount"
                :comment="item.comment"
                :time="item.duration"
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
import VideoCard from "@/components/homeView/VideoCard.vue";
import NavBar from "@/components/navBar/NavBar.vue";
import { videoInfos } from "@/data/videoInfos.js";
import TopNav from "@/components/navBar/TopNav.vue";
import axios from "axios";
export default {
  name: "HomeView",
  components: {
    VideoCard,
    NavBar,
    TopNav,
  },

  data() {
    return {
      loop_videoCards: [
        {
          id: "1",
          url:
            "http://113.45.69.13:9000/image/78845e847febc6fdf4f41eeb6306c25c23396569.jpg",
          title: "《一等情事》",
          message: "红宝石戒指",
        },
        {
          id: "2",
          url:
            "http://113.45.69.13:9000/image/5ab6f52da55a05594087e4631c241cf46796ea12.jpg",
          title:
            "小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥小祥",
          message: "红宝石戒指",
        },
        {
          id: "3",
          url:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          title: "【芙莉莲/静止系MAD】想不想看花海盛开",
          message: "红宝石戒指",
        },
        {
          id: "4",
          url:
            "http://113.45.69.13:9000/image/23b06d02882bcf31d6d044339796693b7d02ed2a.jpg",
          title: "三秒喜欢不上486的都是神人",
          message: "白小鱼",
        },
      ],
      videoCards: videoInfos.slice(0, 6),
      otherVideos: videoInfos.slice(6),
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
      const url = "http://127.0.0.1:8081/video/getAllVideo";
      const token = localStorage.getItem("token"); // 获取 token

      if (!token) {
        console.error("Token is missing");
        return;
      }

      this.isLoading = true; // 开始加载

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`, // 添加 Authorization header
          },
        });

        // 处理成功响应
        this.videoInfos = response.data;
        console.log(this.videoInfos, "this.videoInfos");
      } catch (error) {
        // 错误处理
        console.error("Error fetching video infos:", error);
        alert("获取视频信息失败，请稍后再试");
      } finally {
        this.isLoading = false; // 完成加载
      }
    },
  },
  mounted() {
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
