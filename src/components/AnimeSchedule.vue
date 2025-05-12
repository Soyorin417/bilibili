<template>
  <div class="schedule-section">
    <div class="section-header">
      <div class="title-wrapper">
        <alarm-clock
          theme="multi-color"
          size="32"
          :fill="['#65c4eb', '#54e2e2', '#ffffff', '#54e2e2']"
          :strokeWidth="3"
          strokeLinecap="square"
        />
        <h3 class="section-title">新番时间表</h3>
      </div>

      <div class="week-nav">
        <a
          v-for="day in weekDays"
          :key="day.value"
          :class="['week-day', { active: currentDay === day.value }]"
          @click="handleDayChange(day.value)"
        >
          {{ day.label }}
        </a>
      </div>
      <div class="header-actions">
        <a class="view-all" href="#">查看全部 ></a>
        <button class="refresh-btn" @click="refreshData" :disabled="isLoading">
          <span v-if="isLoading">加载中...</span>
          <span v-else>刷新</span>
        </button>
      </div>
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
      <button @click="refreshData">重试</button>
    </div>

    <div v-else-if="isLoading" class="loading-state">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <div v-else class="anime-schedule-list">
      <div v-if="currentDayAnimes.length === 0" class="no-data">当前日期暂无更新</div>
      <div v-else v-for="anime in currentDayAnimes" :key="anime.id" class="schedule-card">
        <div class="card-image">
          <img :src="anime.url" :alt="anime.title" @error="handleImageError" />
          <span class="score">{{ anime.score }}</span>
        </div>
        <div class="card-info">
          <h4 class="title">{{ anime.title }}</h4>
          <p class="subtitle">更新至{{ anime.episode }}话</p>
          <p class="description">{{ anime.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { AlarmClock } from "@icon-park/vue-next";
import axios from "axios";

export default {
  name: "AnimeSchedule",
  components: {
    AlarmClock,
  },
  data() {
    return {
      currentDay: "Monday",
      isLoading: false,
      error: null,
      weekDays: [
        { label: "最近更新", value: "recent" },
        { label: "周一", value: "Monday" },
        { label: "周二", value: "Tuesday" },
        { label: "周三", value: "Wednesday" },
        { label: "周四", value: "Thursday" },
        { label: "周五", value: "Friday" },
        { label: "周六", value: "Saturday" },
        { label: "周日", value: "Sunday" },
      ],
      scheduleData: {
        Monday: [],
        Tuesday: [],
        Wednesday: [],
        Thursday: [],
        Friday: [],
        Saturday: [],
        Sunday: [],
      },
    };
  },
  methods: {
    async getAnimeList() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.get("http://localhost:8081/getAllAnimeSchedule");
        const animeList = response.data;

        // 按星期几对数据进行分组
        const groupedData = {};
        animeList.forEach((anime) => {
          const day = anime.time.charAt(0).toUpperCase() + anime.time.slice(1);
          if (!groupedData[day]) {
            groupedData[day] = [];
          }
          groupedData[day].push({
            id: anime.id,
            url: anime.url,
            title: anime.title,
            episode: anime.episode.toString(),
            score: anime.score.toString(),
            time: anime.time,
            description: anime.description,
          });
        });

        this.scheduleData = { ...this.scheduleData, ...groupedData };
        console.log("Anime schedule data updated:", this.scheduleData);
      } catch (error) {
        console.error("Error fetching anime schedule:", error);
        this.error = "获取数据失败，请稍后重试";
      } finally {
        this.isLoading = false;
      }
    },

    handleDayChange(day) {
      this.currentDay = day;
    },

    async refreshData() {
      await this.getAnimeList();
    },

    handleImageError(event) {
      event.target.src = "https://via.placeholder.com/240x336?text=图片加载失败";
    },
  },
  computed: {
    currentDayAnimes() {
      if (this.currentDay === "recent") {
        // 获取所有动漫并按更新时间排序
        const allAnimes = Object.values(this.scheduleData).flat();
        return allAnimes.sort((a, b) => b.episode - a.episode).slice(0, 8);
      }
      return this.scheduleData[this.currentDay] || [];
    },
  },
  mounted() {
    this.getAnimeList();
  },
};
</script>

<style scoped>
.schedule-section {
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
  position: relative;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.refresh-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  background: #00a1d6;
  color: white;
  cursor: pointer;
  transition: all 0.3s;
}

.refresh-btn:hover {
  background: #0091c2;
}

.refresh-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.error-message {
  text-align: center;
  color: #ff4d4f;
  padding: 20px;
  background: #fff2f0;
  border-radius: 4px;
  margin: 10px 0;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #00a1d6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 16px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.week-nav {
  display: flex;
  gap: 20px;
  margin: 0 20px;
}

.week-day {
  cursor: pointer;
  color: #666;
  text-decoration: none;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s;
}

.week-day.active {
  color: #00a1d6;
  background: rgba(0, 161, 214, 0.1);
}

.week-day:hover {
  color: #00a1d6;
}

.view-all {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.view-all:hover {
  color: #00a1d6;
}

.anime-schedule-list {
  display: flex;
  gap: 15px;
  overflow-x: auto;
  padding: 10px 0;
}

.schedule-card {
  flex: 0 0 240px;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.schedule-card:hover {
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
  white-space: nowrap;
}

.subtitle {
  font-size: 12px;
  color: #999;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.title-wrapper {
  display: flex;
  align-items: center;
  gap: 4px;
}

.description {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
