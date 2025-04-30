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
            <img :src="anime.image" :alt="anime.title" />
            <span class="score">{{ anime.score }}</span>
          </div>
          <div class="card-info">
            <h4 class="title">{{ anime.title }}</h4>
            <p class="subtitle">{{ anime.subtitle }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AnimeRanking",
  data() {
    return {
      rankingList: [
        {
          id: 1,
          image:
            "http://113.45.69.13:9000/image/41a63442ba47c07f3edeb8169b4d0cc217812e7f.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "我独自升级 第二季",
          subtitle: "超影·更新至第2话",
          score: "9.7",
        },
        {
          id: 2,
          image:
            "http://113.45.69.13:9000/image/d9d6284e0919ecfda41981c1f9119f993db62935.jpg@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "刺客信条：王朝",
          subtitle: "超影·更新至第2话",
          score: "9.9",
        },
        {
          id: 3,
          image:
            "http://113.45.69.13:9000/image/5d07b0a76dcbf7766df4bb498edfae15d52cbbc2.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "打了300年的史莱姆",
          subtitle: "超影·更新至第2话",
          score: "9.7",
        },
        {
          id: 4,
          image:
            "http://113.45.69.13:9000/image/9dc89cedb3d03174de8bfd24cceab74ce6b4ce0b.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "假面骑士布布",
          subtitle: "超影·更新至第2话",
          score: "9.9",
        },
        {
          id: 5,
          image:
            "http://113.45.69.13:9000/image/5ce66f68d54052e3a305524b9b06a76977e974fc.jpg@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "地缚少年花子君 第二季",
          subtitle: "超影·更新至第2话",
          score: "9.8",
        },
        {
          id: 6,
          image:
            "http://113.45.69.13:9000/image/23fabc7d3b53912eb53672ffb6b5379d77b97980.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          title: "香格里拉边境 第二季",
          subtitle: "超影·更新至第2话",
          score: "9.4",
        },
      ],
    };
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
