<template>
  <div>
    <NavBar />

    <div class="profile-container mt-2">
      <SpaceTopBar :collect-count="videos.length" />
      <div class="row mt-4">
        <!-- 主内容区 -->
        <div class="col-lg-9 col-md-8">
          <!-- 视频区 -->
          <div>
            <div class="d-flex align-items-center mb-2">
              <span class="fw-bold fs-5 me-3">视频 · {{ videos.length }}</span>
              <button type="button" class="btn btn-info me-2">最新发布</button>
              <button type="button" class="btn btn-outline-secondary me-2">
                最多播放
              </button>
              <button type="button" class="btn btn-outline-secondary">最多收藏</button>
              <button type="button" class="btn btn-outline-secondary ms-auto">
                <i class="bi bi-play-fill"></i>播放全部
              </button>
              <button type="button" class="btn btn-outline-secondary ms-2">
                查看更多<i class="bi bi-chevron-right"></i>
              </button>
            </div>
            <div class="row mt-4">
              <div
                class="col-2-4 mb-2"
                v-for="video in videos.slice(0, 10)"
                :key="video.videoId"
              >
                <div
                  class="card no-border video-card-hover"
                  @click="goToVideo(video.videoId)"
                  style="cursor: pointer"
                >
                  <div class="image-wrapper">
                    <img :src="video.image" class="card-img-top video-img" />
                    <div class="img-info-bar">
                      <span class="me-2">
                        <i class="bi bi-play-fill"></i>
                        {{ formatNumber(video.playCount) }}
                      </span>
                      <span class="me-2">
                        <i class="bi bi-chat"></i> {{ formatNumber(video.commentCount) }}
                      </span>
                      <span class="ms-auto">
                        {{ video.duration || "00:00" }}
                      </span>
                    </div>
                  </div>
                  <div class="card-body p-2">
                    <div class="video-title text-truncate mb-1">{{ video.title }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 右侧卡片区 -->
        <div class="col-lg-3 col-md-4">
          <div class="card mb-3 profile-card">
            <div class="card-body">
              <div class="fw-bold mb-2 d-flex justify-content-between align-items-center">
                直播间
                <button class="btn btn-outline-primary btn-sm">+ 关注</button>
              </div>
              <div class="text-muted small mb-2">主播不在，关注后可收到开播通知哦~</div>
              <a href="#" class="text-info small">前往TA的直播间 &gt;</a>
            </div>
          </div>
          <div class="card profile-card">
            <div class="card-body">
              <div class="fw-bold mb-2">个人资料</div>
              <div class="text-muted small">UID 596505986</div>
              <div class="text-muted small">01-01</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/navBar/NavBar.vue";
import SpaceTopBar from "@/components/user/SpaceTopBar.vue";
import axios from "axios";
import { mapGetters } from "vuex";
export default {
  name: "CollectionView",

  components: { NavBar, SpaceTopBar },
  data() {
    return {
      videos: [],
    };
  },
  async created() {
    try {
      const userId = this.user.id;
      const response = await axios.get(`http://localhost:8081/collect/list/${userId}`);
      console.log(response, "res");
      if (response.status === 200) {
        const collects = Array.isArray(response.data)
          ? response.data
          : response.data.data || [];
        this.videos = collects.map((video) => ({
          ...video,
          playCount: video.views || 0,
          commentCount: video.comments || 0,
          duration: video.duration || "00:00",
        }));
      }
    } catch (error) {
      console.error("获取收藏列表失败:", error);
    }
  },
  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      return this.userInfo;
    },
  },
  methods: {
    goToVideo(id) {
      this.$router.push(`/video/${id}`);
    },
    formatNumber(num) {
      const n = Number(num) || 0;
      if (n >= 100000000) {
        return (n / 100000000).toFixed(1).replace(/\.0$/, "") + "亿";
      } else if (n >= 10000) {
        return (n / 10000).toFixed(1).replace(/\.0$/, "") + "万";
      }
      return n.toString();
    },
  },
};
</script>

<style scoped>
.profile-container {
  padding-top: 20px;
  padding-bottom: 20px;
  width: 100%;
  max-width: 1700px;
  margin: 0 auto;
  padding: 20px;
}
.space-header .avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}
.video-title {
  font-size: 14px;
  font-weight: 500;
}
.card {
  width: 100%;
  transition: transform 0.2s;
  border: none;
  box-shadow: none;
}

.card-img-top {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
  border-radius: 12px !important;
}
.card-body {
  height: 80px;
  overflow: hidden;
}
.col-2-4 {
  flex: 0 0 20%;
  max-width: 20%;
  padding-right: 10px;
  padding-left: 10px;
}
.profile-card {
  width: 100%;
  height: 160px;
  min-height: 160px;
  max-height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  box-sizing: border-box;
  border: 1px solid #e3e6eb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  background: #fff;
}
.image-wrapper {
  position: relative;
  width: 100%;
  height: 160px;
}
.img-info-bar {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 32px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0));
  color: #fff;
  display: flex;
  align-items: center;
  padding: 0 8px;
  font-size: 13px;
  z-index: 2;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}
.img-info-bar i {
  color: #fff;
  font-size: 15px;
  vertical-align: middle;
}
.no-border {
  border: none !important;
  box-shadow: none !important;
}
.video-img {
  border-radius: 12px !important;
}
</style>
