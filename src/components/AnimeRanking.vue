<template>
  <div class="ranking-section">
    <div class="section-header">
      <div class="title-wrapper">
        <i class="fire-icon">🔥</i>
        <h3 class="section-title">番剧热播榜</h3>
      </div>
      <a class="view-all" href="#">查看全部 ></a>
    </div>
    <div class="ranking-list">
      <div v-for="(anime, index) in rankingList" :key="anime.id" class="ranking-item">
        <div class="rank-number" :class="{ 'top-three': index < 3 }">{{ index + 1 }}</div>
        <div class="anime-card">
          <div class="card-image">
            <img :src="anime.url" :alt="anime.title" />
            <span class="score">{{ anime.score }}</span>
          </div>
          <div class="card-info">
            <h4 class="title">{{ anime.title }}</h4>
            <p class="subtitle">{{ anime.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "AnimeRanking",
  data() {
    return {
      rankingList: [],
    };
  },
  mounted() {
    this.getAnimeRanking();
  },
  methods: {
    async getAnimeRanking() {
      const response = await axios.get("http://localhost:8081/getAllAnime");
      this.rankingList = response.data.slice(0, 5);
    },
  },
};
</script>

<style scoped>
.ranking-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.title-wrapper {
  display: flex;
  align-items: center;
  gap: 4px;
}

.fire-icon {
  font-size: 20px;
  line-height: 1;
  margin-top: -2px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.view-all {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.view-all:hover {
  color: #00a1d6;
}

.ranking-list {
  display: flex;
  gap: 15px;
  overflow-x: auto;
  padding: 10px 0;
}

.ranking-item {
  flex: 0 0 240px;
  position: relative;
  padding-top: 10px;
}

.rank-number {
  position: absolute;
  top: -5px;
  left: 8px;
  z-index: 1;
  font-size: 20px;
  font-weight: bold;
  color: #999;
  background: #fff;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.rank-number.top-three {
  color: #fff;
  background: #ff6b6b;
}

.anime-card {
  height: 100%;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.anime-card:hover {
  transform: translateY(-5px);
}

.card-image {
  position: relative;
  padding-top: 140%;
}

.card-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.score {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: #ffb11b;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
}

.card-info {
  padding: 12px;
}

.title {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 4px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.subtitle {
  font-size: 12px;
  color: #999;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
