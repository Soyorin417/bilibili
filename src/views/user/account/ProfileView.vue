<template>
  <div>
    <VideoBar />
    <div class="profile-container container-fluid mt-4">
      <div class="row">
        <div class="col-md-3">
          <ProfileSidebar />
        </div>
        <div class="col-md-9">
          <!-- 个人信息头部 -->
          <div class="profile-header card mb-4">
            <div class="card-body d-flex align-items-center">
              <img class="avatar" :src="user.avatar" />
              <div class="info ms-4 flex-grow-1">
                <div class="d-flex align-items-center mb-2">
                  <span class="nickname fw-bold fs-5 me-2">{{ user.username }}</span>
                  <span class="badge bg-secondary me-2">正式会员</span>
                </div>
                <div class="d-flex align-items-center mb-2">
                  <span class="level-badge me-2">LV{{ user.level }}</span>
                  <div class="exp-bar flex-grow-1 me-2">
                    <div class="exp-inner" :style="{ width: expPercent + '%' }"></div>
                  </div>
                  <span class="exp-text">{{ user.exp }} / {{ user.nextLevelExp }}</span>
                </div>
                <div class="d-flex align-items-center text-muted small">
                  <span class="me-3"> <b>B</b> {{ user.coins }} </span>
                  <span> <b>币</b> {{ user.bCoins }} </span>
                </div>
              </div>
              <div class="btn-group ms-3">
                <button class="btn btn-outline-secondary btn-sm">修改资料</button>
                <button class="btn btn-outline-secondary btn-sm">个人空间</button>
              </div>
            </div>
          </div>

          <!-- 每日奖励 -->
          <div class="card mb-4">
            <div class="card-body">
              <div class="d-flex align-items-center mb-3">
                <i class="bi bi-gift-fill text-success fs-4 me-2"></i>
                <span class="fs-5 fw-bold">每日奖励</span>
              </div>
              <div class="d-flex justify-content-between flex-wrap">
                <div
                  v-for="task in dailyTasks"
                  :key="task.name"
                  class="reward-item text-center"
                >
                  <div class="circle mb-2">
                    <span class="exp">{{ task.exp }}</span>
                    <span class="exp-label">EXP</span>
                  </div>
                  <div class="task-name">{{ task.name }}</div>
                  <div class="task-status text-muted small">
                    <span v-if="task.completed && task.maxProgress"
                      >已获得{{ task.progress }}/{{ task.maxProgress }}</span
                    >
                    <span v-else-if="task.completed">已完成</span>
                    <span v-else>未完成</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 成就勋章 -->
          <div class="card mb-4">
            <div class="card-body">
              <div class="d-flex align-items-center mb-3">
                <i class="bi bi-star-fill text-warning fs-4 me-2"></i>
                <span class="fs-5 fw-bold">成就勋章</span>
              </div>
              <div class="no-medal text-center text-muted">
                <i class="bi bi-emoji-dizzy"></i>
                <div>你还没有领取过勋章哦！</div>
                <button class="btn btn-info btn-sm mt-2">查看勋章详情</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import ProfileSidebar from "@/components/user/ProfileSidebar.vue";
import { mapGetters } from "vuex";

export default {
  name: "ProfileView",
  components: { VideoBar, ProfileSidebar },
  data() {
    return {
      dailyTasks: [
        { name: "每日登录", exp: 5, completed: false },
        { name: "每日观看视频", exp: 5, completed: false },
        { name: "每日投币", exp: 50, completed: true, progress: 0, maxProgress: 50 },
        { name: "每日分享视频(客户端)", exp: 5, completed: false },
      ],
    };
  },

  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      return this.userInfo;
    },
    avatar() {
      return this.user.avatar;
    },
    expPercent() {
      return Math.round((this.user.exp / this.user.nextLevelExp) * 100);
    },
  },
};
</script>

<style scoped>
.profile-container {
  padding-top: 20px;
  padding-bottom: 20px;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}
.sidebar-nav .card-body {
  padding: 10px;
}
.sidebar-nav .nav-link {
  color: #333;
  padding: 0.6rem 1rem;
  border-radius: 0.25rem;
  margin-bottom: 0.25rem;
  font-size: 0.9rem;
}
.sidebar-nav .nav-link.router-link-exact-active,
.sidebar-nav .nav-link:hover {
  background-color: #00a1d6;
  color: white;
}
.sidebar-nav .card-title {
  font-weight: bold;
  color: #333;
}

/* 个人信息头部样式 */
.profile-header .avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
}
.level-badge {
  background: #ff8c42;
  color: #fff;
  border-radius: 8px;
  padding: 2px 10px;
  font-weight: bold;
  font-size: 14px;
}
.exp-bar {
  background: #eee;
  border-radius: 8px;
  height: 12px;
  width: 180px;
  overflow: hidden;
  margin-right: 8px;
}
.exp-inner {
  background: #ff8c42;
  height: 100%;
  border-radius: 8px;
  transition: width 0.3s;
}
.exp-text {
  font-size: 13px;
  color: #888;
}
.reward-item {
  width: 120px;
  margin-bottom: 16px;
}
.circle {
  width: 60px;
  height: 60px;
  background: #1cc8ee;
  border-radius: 50%;
  color: #fff;
  margin: 0 auto 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.circle .exp {
  font-size: 20px;
  font-weight: bold;
  line-height: 1;
}
.circle .exp-label {
  font-size: 12px;
  line-height: 1;
}
.task-name {
  font-size: 15px;
  margin-bottom: 2px;
}
.no-medal img {
  display: block;
  margin: 0 auto;
}
</style>
