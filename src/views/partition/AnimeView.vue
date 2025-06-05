<template>
  <div>
    <AnimeBar></AnimeBar>
    <div class="anime-container">
      <div class="main-content">
        <div class="left-content">
          <div class="category-section">
            <div class="category-group">
              <div class="card">
                <div class="card-body">
                  <h3>番剧索引</h3>
                  <div class="category-links">
                    <div class="row row-cols-2">
                      <div class="col">
                        <a href="#">语音人数</a>
                      </div>
                      <div class="col">
                        <a href="#">最高评分</a>
                      </div>
                      <div class="col">
                        <a href="#">更新时间</a>
                      </div>
                      <div class="col">
                        <a href="#">播放数量</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="category-group">
              <div class="card">
                <div class="card-body">
                  <h3>类型风格</h3>
                  <div class="category-links">
                    <div class="row row-cols-lg-5">
                      <div class="col">
                        <a href="#">原创</a>
                      </div>
                      <div class="col">
                        <a href="#">小说改</a>
                      </div>
                      <div class="col">
                        <a href="#">特摄</a>
                      </div>
                      <div class="col">
                        <a href="#">热血</a>
                      </div>
                      <div class="col">
                        <a href="#">奇幻</a>
                      </div>
                      <div class="col">
                        <a href="#">漫画改</a>
                      </div>
                      <div class="col">
                        <a href="#">游戏改</a>
                      </div>
                      <div class="col">
                        <a href="#">布袋戏</a>
                      </div>
                      <div class="col">
                        <a href="#">穿越</a>
                      </div>
                      <div class="col">
                        <a href="#">战斗</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="category-group">
              <div class="card">
                <div class="card-body">
                  <h3>首播时间</h3>
                  <div class="category-links">
                    <div class="row row-cols-4">
                      <div class="col">
                        <a href="#">2025</a>
                      </div>
                      <div class="col">
                        <a href="#">2024</a>
                      </div>
                      <div class="col">
                        <a href="#">2023</a>
                      </div>
                      <div class="col">
                        <a href="#">2022</a>
                      </div>
                      <div class="col">
                        <a href="#">2021</a>
                      </div>
                      <div class="col">
                        <a href="#">2020</a>
                      </div>
                      <div class="col">
                        <a href="#">2019</a>
                      </div>
                      <div class="col">
                        <a href="#">2018</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="category-group">
              <div class="card">
                <div class="card-body">
                  <h3>热搜</h3>
                  <div class="category-links">
                    <div class="row g-2">
                      <div class="col-6">
                        <a href="#">阿波连同学第二季</a>
                      </div>
                      <div class="col-6">
                        <a href="#">德浪刀是怎好的</a>
                      </div>
                      <div class="col-6">
                        <a href="#">魔女与猫</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <AnimeSchedule />
      <AnimeRanking />

      <!-- 猜你喜欢 -->
      <div class="recommend-section">
        <h3 class="section-title">猜你喜欢</h3>
        <div class="anime-list">
          <div v-for="anime in animeList" :key="anime.id" class="anime-card">
            <div class="card-image">
              <img :src="anime.url" :alt="anime.alt" />
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
    <!-- Category Navigation -->
  </div>
</template>

<script>
import AnimeBar from "@/components/navBar/AnimeBar.vue";
import AnimeSchedule from "@/components/content/anime/AnimeSchedule.vue";
import AnimeRanking from "@/components/content/anime/AnimeRanking.vue";
import { animeApi } from '@/api/content/anime';
export default {
  name: "AnimeView",
  components: {
    AnimeBar,
    AnimeSchedule,
    AnimeRanking,
  },
  methods: {
    async getAnimeList() {
      try {
        const response = await animeApi.getAllAnime();
      this.animeList = response.data;
      } catch (error) {
        console.error('获取动漫列表失败:', error);
      }
    },
  },
  mounted() {
    this.getAnimeList();
  },
  data() {
    return {
      animeList: [],
    };
  },
};
</script>

<style scoped>
.anime-container {
  width: 100%;
  max-width: 1560px;
  margin: 0 auto;
  padding: 20px;
}

.main-content {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.left-content {
  flex: 1;
}

.right-content {
  width: 300px;
}

.category-section {
  display: flex;
  gap: 20px;
  margin: 20px 0;
  padding: 0;
  background: transparent;
}

.category-group {
  flex: 1;
  min-width: 0;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.category-group:nth-child(1) {
  background: #fff5ff;
}

.category-group:nth-child(2) {
  background: #f0fcf6;
}

.category-group:nth-child(3) {
  background: #f2f8fe;
}

.category-group:nth-child(4) {
  background: #fff1f1;
}

.card {
  border: none;
  box-shadow: none;
  height: 100%;
  background: transparent;
}

.card-body {
  padding: 20px;
  background: transparent;
}

.card-body h3 {
  font-size: 16px;
  margin-bottom: 15px;
  color: #333;
  position: relative;
  padding-left: 10px;
}

.card-body h3::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 16px;
  background-color: #00a1d6;
  border-radius: 2px;
}

.category-links {
  margin-top: 15px;
}

.category-links .row {
  margin: -6px;
}

.category-links .col {
  padding: 6px;
}

.category-links a {
  display: block;
  color: #666;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.category-links a:hover {
  color: #00a1d6;
}

/* 推荐区域样式 */
.recommend-section {
  margin-top: 40px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.anime-list {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.anime-card {
  flex: 0 0 calc((100% - 90px) / 7); /* 7个卡片等宽，减去间隔的总宽度 */
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
  padding-top: 140%; /* 保持图片比例 */
  overflow: hidden;
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

.card-info .title {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 4px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-info .subtitle {
  font-size: 12px;
  color: #999;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.schedule-ranking-container {
  display: flex;
  gap: 20px;
  margin: 20px 0;
}

.schedule-section {
  flex: 1;
}

.ranking-section {
  flex: 1;
}
</style>
