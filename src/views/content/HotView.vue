<template>
  <div class="hot-page">
    <NavBar />
    <div class="hot-container">
      <!-- 分类导航 -->
      <div class="category-nav">
        <div
          class="nav-item"
          :class="{ active: currentCategory === '综合热门' }"
          @click="currentCategory = '综合热门'"
        >
          <i class="bi bi-fire"></i>
          综合热门
        </div>
        <div
          class="nav-item"
          :class="{ active: currentCategory === '每周必看' }"
          @click="currentCategory = '每周必看'"
        >
          <i class="bi bi-calendar-week"></i>
          每周必看
        </div>
        <div
          class="nav-item"
          :class="{ active: currentCategory === '入站必刷' }"
          @click="currentCategory = '入站必刷'"
        >
          <i class="bi bi-star"></i>
          入站必刷
        </div>
        <div
          class="nav-item"
          :class="{ active: currentCategory === '排行榜' }"
          @click="currentCategory = '排行榜'"
        >
          <i class="bi bi-trophy"></i>
          排行榜
        </div>
        <div
          class="nav-item"
          :class="{ active: currentCategory === '全站音乐榜' }"
          @click="currentCategory = '全站音乐榜'"
        >
          <i class="bi bi-music-note-beamed"></i>
          全站音乐榜
        </div>
      </div>

      <!-- 提示文本 -->
      <div class="tip-text">
        各个领域中新奇好玩的优质内容都在这里~
        <a href="#" class="rules-link">投稿规则</a>
      </div>

      <!-- 视频列表 -->
      <div class="video-list">
        <div v-for="video in videos" :key="video.id" class="video-item">
          <div class="video-cover">
            <img :src="video.cover" :alt="video.title" />
            <div class="video-info-overlay">
              <span class="play-count">
                <i class="bi bi-play-fill"></i>
                {{ formatNumber(video.playCount) }}
              </span>
              <span class="comment-count">
                <i class="bi bi-chat-fill"></i>
                {{ formatNumber(video.commentCount) }}
              </span>
            </div>
          </div>
          <div class="video-details">
            <div class="video-title">{{ video.title }}</div>
            <div class="video-uploader">
              <img
                :src="video.uploaderAvatar"
                :alt="video.uploader"
                class="uploader-avatar"
              />
              <span class="uploader-name">{{ video.uploader }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/navBar/NavBar.vue";

export default {
  name: "HotView",
  components: {
    NavBar,
  },
  data() {
    return {
      currentCategory: "综合热门",
      videos: [
        {
          id: 1,
          title: "《明日方舟》EP - Heavenly Me",
          cover:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          playCount: 395700,
          commentCount: 5584,
          uploader: "明日方舟",
          uploaderAvatar: "http://113.45.69.13:9000/image/laoba.jpg",
          tag: "6万点赞",
        },
        {
          id: 2,
          title: "【同人动画自制】你还会为这16年前的乐队感动吗【轻音少女】早安，明天见",
          cover:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          playCount: 145700,
          commentCount: 163,
          uploader: "阿姨们不要深夜",
          uploaderAvatar: "http://113.45.69.13:9000/image/arknight_w.jpg",
          tag: "评论过万",
        },
        {
          id: 3,
          title: "波奇酱: 能不能放过我这一次！🤔💥⚡",
          cover:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          playCount: 209700,
          commentCount: 92,
          uploader: "波奇酱",
          uploaderAvatar: "http://113.45.69.13:9000/image/laoba.jpg",
          tag: "百万播放",
        },
        {
          id: 4,
          title: "【同人动画自制】你还会为这16年前的乐队感动吗【轻音少女】早安，明天见",
          cover:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          playCount: 145700,
          commentCount: 163,
          uploader: "阿姨们不要深夜",
          uploaderAvatar: "http://113.45.69.13:9000/image/arknight_w.jpg",
          tag: "评论过万",
        },
        {
          id: 5,
          title: "【同人动画自制】你还会为这16年前的乐队感动吗【轻音少女】早安，明天见",
          cover:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          playCount: 145700,
          commentCount: 163,
          uploader: "阿姨们不要深夜",
          uploaderAvatar: "http://113.45.69.13:9000/image/arknight_w.jpg",
          tag: "评论过万",
        },
      ],
    };
  },
  methods: {
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + "万";
      }
      return num.toString();
    },
  },
};
</script>

<style scoped>
.hot-page {
  background-color: #f4f5f7;
  min-height: 100vh;
}

.hot-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.category-nav {
  display: flex;
  gap: 32px;
  margin-bottom: 16px;
  padding: 16px 0;
  border-bottom: 1px solid #e3e5e7;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 16px;
  color: #61666d;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.nav-item:hover {
  color: #00aeec;
  background: rgba(0, 174, 236, 0.1);
}

.nav-item.active {
  color: #00aeec;
  font-weight: 500;
}

.nav-item i {
  font-size: 18px;
}

.tip-text {
  color: #61666d;
  font-size: 14px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rules-link {
  color: #00aeec;
  text-decoration: none;
  font-size: 13px;
}

.rules-link:hover {
  color: #00a0d8;
}

.video-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.video-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.video-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-cover {
  position: relative;
  padding-top: 56.25%; /* 16:9 aspect ratio */
  overflow: hidden;
}

.video-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-info-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 8px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: #fff;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.video-info-overlay span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.video-details {
  padding: 12px;
}

.video-title {
  font-size: 14px;
  color: #18191c;
  margin-bottom: 8px;
  line-height: 1.4;
  height: 40px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.video-uploader {
  display: flex;
  align-items: center;
  gap: 6px;
}

.uploader-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
}

.uploader-name {
  font-size: 13px;
  color: #61666d;
}
</style>
