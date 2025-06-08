<template>
  <div>
    <NavBar />

    <div class="profile-container mt-2">
      <SpaceTopBar :userId="userId" />
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
            <div class="dynamic-card-center mt-3">
              <ActivityCard
                class="mt-3"
                v-for="post in posts"
                :key="post.id"
                :post="post"
                :userInfo="userInfo"
                @click="goToVideo(post.video.id)"
              />
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
              <div class="text-muted small">UID {{ userInfo.id }}</div>
              <div class="text-muted small">
                注册时间：{{ formatDate(userInfo.register_time) }}
              </div>
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
import { formatDate, formatCount, limitTextLength } from "@/utils/date";
import ActivityCard from "@/components/activity/ActivityCard.vue";
import { dynamicApi } from "@/api/user/dynamic";

export default {
  name: "DynamicView",
  components: { NavBar, SpaceTopBar, ActivityCard },
  data() {
    return {
      videos: [],
      posts: [],
      userInfo: {},
      userId: null,
    };
  },
  mounted() {
    this.userId = this.$route.params.userId;
    console.log("URL里的userId:", this.userId);
    if (this.userId) {
      this.fetchDynamics();
    }
  },
  methods: {
    formatDate,
    async fetchDynamics() {
      try {
        const response = await dynamicApi.getUserDynamics(this.userId);
        console.log("API Response:", response);
        if (response.status === 200) {
          const dynamicData = Array.isArray(response.data) ? response.data : [];

          this.posts = dynamicData.map((video) => ({
            id: video.id,
            user_id: video.authorId,
            username: video.authorName || "用户名",
            avatar: video.avatar || "默认头像url",
            time: video.time,
            content: limitTextLength(video.title || video.description || ""),
            video: {
              id: video.id,
              image: video.image || "",
              title: video.title || "无标题",
              duration: video.duration || "00:00",
              playCount: formatCount(video.views || 0),
              commentCount: formatCount(video.comments || 0),
            },
            comments: formatCount(video.comments) || 0,
            likeCount: video.likeCount || 0,
            shared: video.shareCount || 0,
            coinCount: video.coinCount || 0,
            description: video.description || "",
            status: video.status || "",
            liked: false,
            is_image: true,
          }));
          console.log("处理后的动态数据:", this.posts);
        } else {
          console.error("获取动态列表失败: 无效的响应状态码", response.status);
          this.posts = [];
        }
        this.userInfo = JSON.parse(localStorage.getItem("userInfo")) || {}; // Ensure userInfo is loaded
      } catch (error) {
        console.error("获取动态列表失败:", error);
        this.posts = [];
      }
    },
    goToVideo(id) {
      console.log("DynamicView: Received view-video event with ID:", id);
      if (id) {
        this.$router.push(`/video/${id}`);
      } else {
        console.warn("DynamicView: Cannot navigate, received invalid video ID:", id);
      }
    },
  },
  watch: {
    "$route.params.userId"(newId) {
      this.userId = newId;
      this.fetchDynamics();
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
.dynamic-card-center {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.dynamic-card-center .post-card {
  width: 800px;
  max-width: 95vw;
  margin-bottom: 24px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  transform: scale(1.08);
  transition: box-shadow 0.3s, transform 0.3s;
}
.fw-bold {
  text-align: left;
}
.text-muted {
  text-align: left;
}
</style>
