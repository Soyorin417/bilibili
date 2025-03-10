<template>
  <div class="container-fluid mt-3">
    <div class="page mx-auto">
      <div class="card-body">
        <div class="row">
          <div class="col-1">
            <div class="row">
              <div class="col-6">
                <IconButton icon="WindmillTwo" color="#ff9212" label="动态" />
              </div>
              <div class="col-5">
                <IconButton icon="Fire" color="#f07775" label="热门" />
              </div>
              <div class="col-1"></div>
            </div>
          </div>
          <div class="col-9">
            <div class="row">
              <div v-for="(tag, index) in tags" :key="index" class="col-1">
                <div
                  v-for="(item, i) in tag"
                  :key="i"
                  class="card tag"
                  :class="{ 'mt-1': i > 0 }"
                >
                  <div class="card-body">{{ item }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧空白区域 -->
          <div class="col-2">
            <div class="row">
              <div v-for="(tag, index) in main_tags" :key="index" class="col-4">
                <div v-for="(item, i) in tag" :key="i" :class="{ 'mt-4': i > 0 }">
                  <div class="main_tag">
                    <TagButton :icon="item[0]" :label="item[1]" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="custom-layout mt-3">
          <div class="left-section">
            <div
              id="carouselExampleIndicators"
              class="carousel slide"
              data-bs-ride="carousel"
            >
              <div class="carousel-indicators">
                <button
                  type="button"
                  data-bs-target="#carouselExampleIndicators"
                  data-bs-slide-to="0"
                  class="active"
                  aria-current="true"
                  aria-label="Slide 1"
                ></button>
                <button
                  type="button"
                  data-bs-target="#carouselExampleIndicators"
                  data-bs-slide-to="1"
                  aria-label="Slide 2"
                ></button>
                <button
                  type="button"
                  data-bs-target="#carouselExampleIndicators"
                  data-bs-slide-to="2"
                  aria-label="Slide 3"
                ></button>
              </div>
              <div class="carousel-inner">
                <div class="carousel-item active">
                  <img
                    src="../assets/mygo.jpg"
                    class="d-block w-100 slide_img rounded"
                    alt="Slide 1"
                  />
                </div>
                <div class="carousel-item">
                  <img
                    src="../assets/game.jpg"
                    class="d-block w-100 slide_img rounded"
                    alt="Slide 2"
                  />
                </div>
                <div class="carousel-item">
                  <img
                    src="../assets/arknights.webp"
                    class="d-block w-100 slide_img rounded"
                    alt="Slide 3"
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
              <div v-for="(videoCard, index) in videoCards" :key="index" class="col">
                <VideoCard
                  v-for="(item, i) in videoCard"
                  :key="i"
                  :url="item.url"
                  :title="item.title"
                  :message="item.message"
                  :class="{ 'mt-4': i > 0 }"
                  class="mb-3"
                />
              </div>
            </div>
          </div>
        </div>
        <div class="row row-cols-5">
          <div v-for="(otherVideo, index) in otherVideos" :key="index" class="col">
            <VideoCard
              class="mb-3"
              v-for="(item, i) in otherVideo"
              :key="i"
              :url="item.url"
              :title="item.title"
              :message="item.message"
              :class="{ 'mt-4': i > 0 }"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TagButton from "@/components/TagButton.vue";
import IconButton from "../components/IconButton.vue";
import VideoCard from "@/components/VideoCard.vue";

export default {
  name: "HomeView",
  components: {
    IconButton,
    TagButton,
    VideoCard,
  },
  data() {
    return {
      tags: [
        ["番剧", "电影"],
        ["国创", "电视剧"],
        ["综艺", "纪录片"],
        ["动画", "游戏"],
        ["鬼畜", "音乐"],
        ["舞蹈", "影视"],
        ["娱乐", "知识"],
        ["数码科技", "资讯"],
        ["美食", "小剧场"],
        ["汽车", "时尚美妆"],
        ["体育运动", "动物"],
        ["vlog", "更多"],
      ],
      main_tags: [
        [
          ["Message", "专栏"],
          ["Monitor", "直播"],
        ],
        [
          ["Flag", "活动"],
          ["BookOpen", "课堂"],
        ],
        [
          ["Comments", "社区中心"],
          ["Music", "新歌热榜"],
        ],
      ],
      videoCards: [
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/background.webp"), title: "视频3", message: "描述3" },
          { url: require("@/assets/lucy.png"), title: "视频4", message: "描述4" },
        ],
        [
          { url: require("@/assets/mygo.jpg"), title: "视频5", message: "描述5" },
          { url: require("@/assets/mygo.webp"), title: "视频6", message: "描述6" },
        ],
      ],

      otherVideos: [
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/background.webp"), title: "视频3", message: "描述3" },
          { url: require("@/assets/lucy.png"), title: "视频4", message: "描述4" },
        ],
        [
          { url: require("@/assets/mygo.jpg"), title: "视频5", message: "描述5" },
          { url: require("@/assets/mygo.webp"), title: "视频6", message: "描述6" },
        ],
        [
          { url: require("@/assets/zc.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/ew.png"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/ew.png"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
        [
          { url: require("@/assets/aibunala.jpg"), title: "视频1", message: "描述1" },
          { url: require("@/assets/arknights.webp"), title: "视频2", message: "描述2" },
        ],
      ],
    };
  },
};
</script>

<style scoped>
.rounded-circle {
  width: 3em;
  height: 3em;
  display: flex;
  align-items: center;
  justify-content: center;
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

.text-center {
  text-align: center;
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
</style>
