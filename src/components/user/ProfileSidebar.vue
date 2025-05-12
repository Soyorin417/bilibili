<template>
  <div class="sidebar-nav card">
    <div class="card-body">
      <h5 class="card-title text-center mb-3">个人中心</h5>
      <ul class="nav flex-column">
        <li class="nav-item">
          <router-link class="nav-link" to="/profile">首页</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/vip">大会员</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/points">会员积分</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/message">我的信息</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/face">我的头像</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/livefansmedalwall"
            >粉丝勋章</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/achievement">成就勋章</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/security">账号安全</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/blacklist">黑名单管理</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/coin">我的硬币</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/watch-history">我的记录</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/verify">实名认证</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/invite">邀请注册</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/profile/notify">通知设置</router-link>
        </li>
        <li class="nav-item mt-3 border-top pt-3">
          <router-link class="nav-link" to="/profile/space">个人空间</router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProfileSidebar",
};
</script>

<style scoped>
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
</style>
