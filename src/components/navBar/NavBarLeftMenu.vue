<template>
  <ul class="navbar-nav me-1">
    <li class="nav-item">
      <a class="nav-link" href="#">
        <BilibiliIcon class="me-1" :fill="fill" />
        <span :style="{ color: fill }">首页</span>
      </a>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/Anime`" :style="{ color: fill }">
        <span :style="{ color: fill }">番剧</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/Broadcast`" :style="{ color: fill }">
        <span :style="{ color: fill }">直播</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/GameCenter`" :style="{ color: fill }">
        <span :style="{ color: fill }">游戏中心</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/Purchase`" :style="{ color: fill }">
        <span :style="{ color: fill }">会员购</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/Caricature`" :style="{ color: fill }">
        <span :style="{ color: fill }">漫画</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/Events`" :style="{ color: fill }">
        <span :style="{ color: fill }">赛事</span>
      </router-link>
    </li>
    <li class="nav-item">
      <router-link class="nav-link" :to="`/PDD`" :style="{ color: fill }">
        <span :style="{ color: fill }">拼多多</span>
      </router-link>
    </li>

    <li class="nav-item">
      <a class="nav-link" href="#">
        <download theme="outline" size="20" :fill="fill" class="me-1" />
        <span :style="{ color: fill }">下载客户端</span>
      </a>
    </li>
  </ul>
</template>

<script>
import { Download } from "@icon-park/vue-next";
import BilibiliIcon from "@/components/utils/ui/BilibiliIcon.vue";
export default {
  name: "LeftNavMenu",
  props: {
    tags: {
      type: Array,
      required: true,
    },
    fill: {
      type: String,
      default: "#000",
    },
  },
  components: {
    Download,
    BilibiliIcon,
  },
};
</script>
<style scoped>
.nav-link {
  margin: 0 0.1rem;
  font-weight: 400;
  font-size: small;
  color: white;
  display: inline-block;
  transition: transform 0.3s ease;
}

.nav-link:hover {
  transform: translateY(-5px);
  color: white;
}
</style>
