<template>
  <div class="user-card-mini">
    <!-- 用户头像 -->
    <div class="user-header">
      <img class="avatar" :src="avatar" @click="clickAvatar" />
      <div class="nickname">{{ user.username || "未登录" }}</div>
      <div class="coins">
        硬币: <span>{{ user.coin || 0 }}</span> &nbsp; B币:
        <span>{{ user.bcoin || 0 }}</span>
      </div>
    </div>

    <!-- 等级进度 -->
    <div class="level-row">
      <span class="level">LV{{ user.level || 0 }}</span>
      <span class="exp">
        当前成长 {{ user.exp || 0 }}，距离升级 Lv.{{ (user.level || 0) + 1 }} 还需
        {{ (user.nextExp || 0) - (user.exp || 0) }}
      </span>
    </div>

    <!-- 用户统计信息 -->
    <div class="stats-row">
      <div>
        <div class="stat-num">{{ user.following || 0 }}</div>
        <div class="stat-label">关注</div>
      </div>
      <div>
        <div class="stat-num">{{ user.fans || 0 }}</div>
        <div class="stat-label">粉丝</div>
      </div>
      <div>
        <div class="stat-num">{{ user.dynamic || 0 }}</div>
        <div class="stat-label">动态</div>
      </div>
    </div>

    <!-- 会员入口 -->
    <div class="vip-row">
      <div class="vip-tip">您的大会员五一特惠…</div>
      <button class="vip-btn">会员中心</button>
    </div>

    <!-- 菜单列表 -->
    <div class="menu-list">
      <router-link to="/profile" class="menu-item nav-link">
        <i class="bi bi-person me-2"></i> 个人中心
      </router-link>
      <router-link to="/upload" class="menu-item nav-link">
        <i class="bi bi-file-earmark-play me-2"></i> 投稿管理
      </router-link>
      <router-link to="/user/recommend" class="menu-item nav-link">
        <i class="bi bi-file-earmark-play me-2"></i> 推荐服务
      </router-link>
    </div>

    <!-- 登录 / 退出按钮 -->
    <button class="logout-btn" v-if="isLogin" @click="logout">
      <i class="bi bi-box-arrow-right me-2"></i>退出登录
    </button>
    <button class="logout-btn" v-else @click="login">登录</button>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { goToUserProfile } from "@/utils/navigationUtils";

export default {
  name: "UserProfileCardMini",

  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      return this.userInfo; // 这里返回的是 Vuex 中存储的 userInfo
    },
    avatar() {
      return this.user.avatar; // 返回头像
    },
  },

  methods: {
    async clickAvatar() {
      await goToUserProfile(this.$router, this.userInfo.id);
    },
    logout() {
      this.$emit("logout"); // 告诉父组件你要登出
    },
    login() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.user-card-mini {
  width: 320px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px #e3e5e7;
  padding: 24px 20px 16px 20px;
  font-family: "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
}
.user-header {
  text-align: center;
  margin-bottom: 12px;
}
.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  margin-bottom: 8px;
  object-fit: cover;
}
.nickname {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 4px;
}
.coins {
  color: #888;
  font-size: 13px;
}
.level-row {
  text-align: center;
  margin-bottom: 10px;
  color: #6c8cff;
  font-size: 13px;
}
.level {
  font-weight: bold;
  margin-right: 8px;
}
.exp {
  color: #888;
}
.stats-row {
  display: flex;
  justify-content: space-around;
  margin: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 12px;
}
.stat-num {
  font-size: 18px;
  font-weight: bold;
  color: #222;
  text-align: center;
}
.stat-label {
  font-size: 13px;
  color: #888;
  text-align: center;
}
.vip-row {
  background: #fff0f0;
  border-radius: 8px;
  padding: 8px 12px;
  margin: 12px 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.vip-tip {
  color: #ff6699;
  font-size: 13px;
}
.vip-btn {
  background: #ff6699;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 4px 12px;
  font-size: 13px;
  cursor: pointer;
}
.menu-list {
  margin: 16px 0 8px 0;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 0;
  color: #222;
  font-size: 15px;
  cursor: pointer;
  border-bottom: 1px solid #f5f5f5;
}
.menu-item:last-child {
  border-bottom: none;
}
.iconfont {
  margin-right: 8px;
  color: #6c8cff;
}
.logout-btn {
  width: 100%;
  background: #f5f5f5;
  color: #888;
  border: none;
  border-radius: 6px;
  padding: 8px 0;
  font-size: 15px;
  cursor: pointer;
  margin-top: 8px;
  transition: background 0.2s;
}
.logout-btn:hover {
  background: #e3e5e7;
}
</style>
