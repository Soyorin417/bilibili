<template>
  <div>
    <NavBar />

    <div class="profile-container mt-2">
      <SpaceTopBar />
      <div class="row mt-4">
        <!-- 主内容区 -->
        <div class="col-lg-9 col-md-8">
          <!-- 视频区 -->
          <div>
            <div class="d-flex align-items-center mb-2">
              <span class="fw-bold fs-5 me-3">视频 · 48</span>
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
              <div class="col-md-3 mb-4" v-for="video in videos" :key="video.id">
                <div class="card">
                  <img :src="video.cover" class="card-img-top" />
                  <div class="card-body p-2">
                    <div class="video-title text-truncate mb-1">{{ video.title }}</div>
                    <div class="d-flex text-muted small">
                      <span class="me-2"
                        ><i class="bi bi-play-fill"></i> {{ video.play }}</span
                      >
                      <span class="me-2"
                        ><i class="bi bi-chat"></i> {{ video.comment }}</span
                      >
                      <span><i class="bi bi-clock"></i> {{ video.duration }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 右侧卡片区 -->
        <div class="col-lg-3 col-md-4">
          <div class="card mb-3">
            <div class="card-body">
              <div class="fw-bold mb-2 d-flex justify-content-between align-items-center">
                直播间
                <button class="btn btn-outline-primary btn-sm">+ 关注</button>
              </div>
              <div class="text-muted small mb-2">主播不在，关注后可收到开播通知哦~</div>
              <a href="#" class="text-info small">前往TA的直播间 &gt;</a>
            </div>
          </div>
          <div class="card">
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
export default {
  name: "SpaceView",
  components: { NavBar, SpaceTopBar },
  data() {
    return {
      videos: Array.from({ length: 8 }).map((_, i) => ({
        id: i + 1,
        cover: "http://113.45.69.13:9000/image/amiya.jpg", // 占位图
        title: `示例视频${i + 1}`,
        play: Math.floor(Math.random() * 1000),
        comment: Math.floor(Math.random() * 10),
        duration: "03:20",
      })),
    };
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
</style>
