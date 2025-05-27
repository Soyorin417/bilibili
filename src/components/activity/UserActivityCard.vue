<template>
  <div class="left-sidebar">
    <div class="user-info card">
      <div class="user-header d-flex">
        <div class="user-avatar" @click="$emit('avatar-click', userInfo)">
          <img :src="userInfo.avatar" alt="用户头像" />
        </div>
        <div class="user-name ms-2 mb-4">
          <div @click="$emit('username-click', userInfo)">
            {{ userInfo.username }}
          </div>
          <div>
            <span class="user-level">LV{{ userInfo.level }}</span>
          </div>
        </div>
      </div>
      <div class="user-stats">
        <div class="stat-item" @click="$emit('follow-click', userInfo)">
          <div class="stat-value">{{ userInfo.followCount || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
        <div class="stat-item" @click="$emit('fans-click', userInfo)">
          <div class="stat-value">{{ userInfo.fans }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-item" @click="$emit('dynamic-click', userInfo)">
          <div class="stat-value">{{ userInfo.dynamic }}</div>
          <div class="stat-label">动态</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserActivityCard",
  props: {
    userInfo: {
      type: Object,
      required: true,
      default: () => ({
        avatar: "",
        username: "",
        level: 0,
        followCount: 0,
        fans: 0,
        dynamic: 0,
      }),
    },
  },
  emits: [
    "avatar-click",
    "username-click",
    "follow-click",
    "fans-click",
    "dynamic-click",
  ],
};
</script>

<style scoped>
.left-sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}
.user-info {
  padding: 16px;
}
.user-header {
  text-align: center;
  margin-bottom: 16px;
  height: 50px;
}

.user-avatar {
  margin-bottom: 12px;
}

.user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.stat-item {
  text-align: center;
}
.stat-value {
  font-size: 16px;
  font-weight: 600;
  color: #18191c;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #61666d;
}

.user-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-top: 1px solid #f0f1f2;
  padding-top: 16px;
}

.user-level {
  font-size: 10px;
  color: #fff;
  background: linear-gradient(90deg, #ff6699 0%, #ff9966 100%);
  padding: 1px 1px;

  font-weight: lighter;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #18191c;
  margin-bottom: 4px;

  align-items: center;
  justify-content: center;
  gap: 8px;
}
.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 16px;
  margin-bottom: 16px;
}
</style>
