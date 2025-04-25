<template>
  <div>
    <NavBar> </NavBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="card-body">
          <div class="row">
            <!--左边区域-->
            <div class="col-1 mt-1">
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

            <!--中间tag区域-->
            <div class="col-9">
              <div class="row">
                <div v-for="(tag, index) in tags" :key="index" class="col-1">
                  <div
                    v-for="(item, i) in tag"
                    :key="i"
                    class="card tag border border-0"
                    :class="{ 'mt-1': i > 0 }"
                  >
                    <router-link class="card-body fw-normal nav-link" :to="item.link">{{
                      item.name
                    }}</router-link>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧空白区域 -->
            <div class="col-2 mt-1">
              <div class="row border-start">
                <div v-for="(tag, index) in main_tags" :key="index" class="col-4">
                  <div v-for="(item, i) in tag" :key="i" :class="{ 'mt-4': i > 0 }">
                    <router-link class="main_tag nav-link" :to="item[2]">
                      <TagButton :icon="item[0]" :label="item[1]" />
                    </router-link>
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
                    :url="item.url"
                    :playCount="item.playCount"
                    :comment="item.comment"
                    :time="item.time"
                    :title="item.title"
                    :message="item.message"
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
                :url="item.url"
                :playCount="item.playCount"
                :comment="item.comment"
                :time="item.time"
                :title="item.title"
                :message="item.message"
              />
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TagButton from "@/components/homeView/TagButton.vue";
import IconButton from "../components/homeView/IconButton.vue";
import VideoCard from "@/components/homeView/VideoCard.vue";
import NavBar from "@/components/navBar/NavBar.vue";

export default {
  name: "HomeView",
  components: {
    IconButton,
    TagButton,
    VideoCard,
    NavBar,
  },
  data() {
    return {
      tags: [
        [
          { name: "番剧", link: "Anime" },
          { name: "电影", link: "Movie" },
        ],
        [
          { name: "国创", link: "Original" },
          { name: "电视剧", link: "Drama" },
        ],
        [
          { name: "综艺", link: "Show" },
          { name: "纪录片", link: "Doc" },
        ],
        [
          { name: "动画", link: "Cartoon" },
          { name: "游戏", link: "Game" },
        ],
        [
          { name: "鬼畜", link: "Remix" },
          { name: "音乐", link: "Music" },
        ],
        [
          { name: "舞蹈", link: "Dance" },
          { name: "影视", link: "Video" },
        ],
        [
          { name: "娱乐", link: "Fun" },
          { name: "知识", link: "Study" },
        ],
        [
          { name: "数码科技", link: "Tech" },
          { name: "资讯", link: "News" },
        ],
        [
          { name: "美食", link: "Food" },
          { name: "小剧场", link: "Stage" },
        ],
        [
          { name: "汽车", link: "Auto" },
          { name: "时尚美妆", link: "Style" },
        ],
        [
          { name: "体育运动", link: "Sport" },
          { name: "动物", link: "Pet" },
        ],
        [
          { name: "vlog", link: "Vlog" },
          { name: "更多", link: "More" },
        ],
      ],

      main_tags: [
        [
          ["Message", "专栏", "Column"],
          ["Monitor", "直播", "Broadcast"],
        ],
        [
          ["Flag", "活动", "Activity"],
          ["BookOpen", "课堂", "Classroom"],
        ],
        [
          ["Comments", "社区中心", "CommunityCenters"],
          ["Music", "新歌热榜", "NewSongs"],
        ],
      ],
      loop_videoCards: [
        {
          id: "1",
          url:
            "http://113.45.69.13:9000/image/78845e847febc6fdf4f41eeb6306c25c23396569.jpg",
          title: "视频1",
          message: "描述1",
        },
        {
          id: "2",
          url: "http://113.45.69.13:9000/image/lucy_moon.jpg",
          title: "视频2",
          message: "描述2",
        },
        {
          id: "3",
          url: "http://113.45.69.13:9000/image/lucy_moon.jpg",
          title: "视频3",
          message: "描述3",
        },
        {
          id: "4",
          url: "http://113.45.69.13:9000/image/lucy_moon.jpg",
          title: "视频4",
          message: "描述4",
        },
      ],
      videoCards: [
        {
          id: "1",
          url:
            "http://113.45.69.13:9000/image/78845e847febc6fdf4f41eeb6306c25c23396569.jpg",
          title: "视频1",
          message: "描述1",
        },
        {
          id: "2",
          url: "http://113.45.69.13:9000/image/mingchao.jpg",
          title: "视频2",
          time: "12:20",
          message: "描述2",
        },
        {
          id: "3",
          url: "http://113.45.69.13:9000/image/fulilian.png",
          title: "视频3",
          time: "12:20",
          message: "描述3",
        },
        {
          id: "4",
          url: "http://113.45.69.13:9000/image/Florian.jpg",
          title: "视频4",
          message: "描述4",
        },
        {
          id: "5",
          url: "http://113.45.69.13:9000/image/arknight_w.jpg",
          title: "视频5",
          message: "描述5",
        },
        {
          id: "6",
          url: "http://113.45.69.13:9000/image/amiya.jpg",
          title: "视频6",
          message: "描述6",
        },
      ],

      otherVideos: [
        {
          id: "7",
          url: "http://113.45.69.13:9000/image/lucy.png",
          title: "视频7",
          message: "描述7",
        },
        {
          id: "8",
          url: "http://113.45.69.13:9000/image/lucy_moon.jpg",
          playCount: "10万",
          time: "12:20",
          title: "视频8",
          message: "描述8",
        },
        {
          id: "9",
          url: "http://113.45.69.13:9000/image/fulilian.jpg",
          playCount: "10万",
          title: "视频9",
          message: "描述9",
        },
        {
          id: "10",
          url:
            "http://113.45.69.13:9000/image/a29d12bfc0664b4a62dc5de95d42f14c65e6f303.jpg",
          playCount: "10万",
          title: "视频10",
          message: "描述10",
        },
        {
          id: "11",
          url:
            "http://113.45.69.13:9000/image/23b06d02882bcf31d6d044339796693b7d02ed2a.jpg",
          playCount: "10万",
          title: "视频11",
          message: "描述11",
        },
        {
          id: "12",
          url:
            "http://113.45.69.13:9000/image/f94ec129e83bfca078e5e84277d8a69ea932732e.jpg",
          playCount: "10万",
          title: "视频12",
          message: "描述12",
        },
        {
          id: "13",
          url:
            "http://113.45.69.13:9000/image/d1183a9170bd267f19f748ad874ad5467b4dcca2.jpg",
          title: "视频13",
          message: "描述13",
        },
        {
          id: "14",
          url:
            "http://113.45.69.13:9000/image/33e9ef5fc74dc0057dd50cf73e119bcd475d4f56.jpg",
          title: "视频14",
          message: "描述14",
        },
        {
          id: "15",
          url:
            "http://113.45.69.13:9000/image/625f17ba06fb22950a272ebbcbed4b1f5f9dcce5.jpg",
          title: "视频15",
          message: "描述15",
        },
        {
          id: "16",
          url:
            "http://113.45.69.13:9000/image/78845e847febc6fdf4f41eeb6306c25c23396569.jpg",
          title: "《一等情事》",
          message: "红宝石戒指",
        },
        {
          id: "17",
          url: "http://113.45.69.13:9000/image/lucy_moon.jpg",
          title: "视频17",
          message: "描述17",
        },
      ],
      showPlaceholder: true,
      loadedVideos: [],
    };
  },
  created() {
    // 初始化所有视频为未加载状态
    const totalVideos = this.videoCards.length + this.otherVideos.length;
    this.loadedVideos = Array(totalVideos).fill(false);
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
