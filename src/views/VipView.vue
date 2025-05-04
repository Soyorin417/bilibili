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
              <img :src="anime.image" :alt="anime.alt" />
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
    <!-- Category Navigation -->
  </div>
</template>

<script>
import AnimeBar from "@/components/navBar/AnimeBar.vue";
import AnimeSchedule from "@/components/AnimeSchedule.vue";
import AnimeRanking from "@/components/AnimeRanking.vue";

export default {
  name: "VipView",
  components: {
    AnimeBar,
    AnimeSchedule,
    AnimeRanking,
  },
  data() {
    return {
      loopUrl: [
        "http://113.45.69.13:9000/image/lucy_moon.jpg",
        "http://113.45.69.13:9000/image/c668fac037823764abe2023427cb50a2437cdf0b.jpg",
        "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
        "http://113.45.69.13:9000/image/laoba.jpg",
        "http://113.45.69.13:9000/image/arknights.webp",
      ],
      animeList: [
        {
          id: 1,
          image:
            "http://113.45.69.13:9000/image/e55865e7a58c374e0dd5c13f552146f716ff026f.jpg",
          alt: "少女乐队的呐喊",
          score: "9.7",
          title: "少女乐队的呐喊",
          subtitle: "少女乐队，摇滚万岁",
        },
        {
          id: 2,
          image:
            "http://113.45.69.13:9000/image/9d731a02054c4807cc0a01fc7768f87d0bab10c1.png",
          alt: "坂本日常",
          score: "8.8",
          title: "坂本日常",
          subtitle: "这个男主太太太帅了",
        },
        {
          id: 3,
          image:
            "http://113.45.69.13:9000/image/7f5c1d29ede597590cbfab7820a5096469b6030e.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "只有我不存在的城市",
          score: "9.7",
          title: "只有我不存在的城市",
          subtitle: "重来一次，拯救她",
        },
        {
          id: 4,
          image:
            "http://113.45.69.13:9000/image/23fabc7d3b53912eb53672ffb6b5379d77b97980.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "颂乐人偶",
          score: "6.5",
          title: "颂乐人偶",
          subtitle: "末日萝莉，绝望万岁",
        },
        {
          id: 5,
          image:
            "http://113.45.69.13:9000/image/d9d6284e0919ecfda41981c1f9119f993db62935.jpg@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "孤独摇滚",
          score: "9.9",
          title: "孤独摇滚！",
          subtitle: "女孩的摇滚梦想",
        },
        {
          id: 6,
          image:
            "http://113.45.69.13:9000/image/41a63442ba47c07f3edeb8169b4d0cc217812e7f.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "我独自升级",
          score: "9.7",
          title: "我独自升级 第二季",
          subtitle: "猎杀者王的归来！",
        },
        {
          id: 7,
          image:
            "http://113.45.69.13:9000/image/5d07b0a76dcbf7766df4bb498edfae15d52cbbc2.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "ReLIFE",
          score: "9.9",
          title: "ReLIFE",
          subtitle: "社畜重返17岁",
        },
        {
          id: 8,
          image:
            "http://113.45.69.13:9000/image/9dc89cedb3d03174de8bfd24cceab74ce6b4ce0b.png@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "摇滚乃是淑女的爱好",
          score: "9.4",
          title: "摇滚乃是淑女的爱好",
          subtitle: "填补过去，重生生活",
        },
        {
          id: 9,
          image:
            "http://113.45.69.13:9000/image/5ce66f68d54052e3a305524b9b06a76977e974fc.jpg@560w_746h_!web-ogv-anime-ranking-card.webp",
          alt: "月色真美",
          score: "9.8",
          title: "月色真美",
          subtitle: "小学生的侦探生活",
        },
      ],
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
