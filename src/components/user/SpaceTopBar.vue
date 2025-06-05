<template>
  <nav class="space-topbar">
    <div class="nav-list">
      <router-link to="/profile/space" class="nav-item" exact>
        <home theme="outline" size="22" fill="#0eb350" :strokeWidth="3" />
        <span>首页</span>
      </router-link>
      <router-link to="/profile/dynamic" class="nav-item">
        <windmill-two theme="outline" size="22" fill="#ff69b4" />
        <span>动态</span>
      </router-link>
      <router-link to="/profile/video" class="nav-item">
        <i class="bi bi-tv icon" style="color: #00b5e5"></i>
        <span>投稿</span>
        <span class="count">48</span>
      </router-link>
      <router-link to="/profile/list" class="nav-item">
        <i class="bi bi-stack icon" style="color: #3ac0ff"></i>
        <span>合集</span>
        <span class="count">1</span>
      </router-link>
      <router-link to="/profile/collection" class="nav-item">
        <CollectIcon :fill="fill" />
        <span>收藏</span>
        <span class="count">{{ collectCount }}</span>
      </router-link>
    </div>
    <div class="search-box">
      <input type="text" class="search-input" placeholder="搜索视频、动态" />
      <i class="bi bi-search search-icon"></i>
    </div>
  </nav>
</template>

<script>
import { WindmillTwo, Home } from "@icon-park/vue-next";
import CollectIcon from "@/components/utils/ui/CollectIcon.vue";
export default {
  name: "SpaceTopBar",
  components: { WindmillTwo, Home, CollectIcon },
  data() {
    return {
      fill: "#ffb534",
    };
  },
  props: {
    collectCount: {
      type: Number,
      default: 0,
    },
  },
};
</script>

<style scoped>
.space-topbar {
  display: flex;
  align-items: left;
  height: 48px;
  border-bottom: 1.5px solid #e3e5e7;
}
.nav-list {
  display: flex;
  align-items: left;
  height: 100%;
}
.nav-item {
  display: flex;
  align-items: center;
  margin-right: 24px;
  color: #222;
  font-size: 15px;
  text-decoration: none;
  position: relative;
  height: 100%;
  padding: 0 2px;
  transition: color 0.2s;
  line-height: 48px;
}
.nav-item:last-child {
  margin-right: 0;
}
.nav-item .icon {
  font-size: 18px;
  margin-right: 3px;
}
.nav-item span {
  margin-left: 3px;
}
.nav-item .count {
  font-size: 12px;
  color: #888;
  margin-left: 2px;
}
.nav-item.router-link-exact-active,
.nav-item.router-link-active {
  color: #00a1d6;
}
.nav-item.router-link-exact-active::after,
.nav-item.router-link-active::after {
  content: "";
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 3px;
  background: #00a1d6;
  border-radius: 2px 2px 0 0;
}
.search-box {
  margin-left: auto;
  position: relative;
  width: 210px;
}
.search-input {
  width: 100%;
  height: 32px;
  border-radius: 16px;
  border: none;
  background: #f6f7f8;
  padding: 0 32px 0 32px;
  font-size: 14px;
  color: #222;
  outline: none;
  box-shadow: none;
}
.search-icon {
  position: absolute;
  left: 12px;
  top: 8px;
  color: #bbb;
  font-size: 16px;
}
</style>
