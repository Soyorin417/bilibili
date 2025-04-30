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
          @click="currentDay = day.value"
        >
          {{ day.label }}
        </a>
      </div>
      <a class="view-all" href="#">查看全部 ></a>
    </div>
    <div class="anime-schedule-list">
      <div v-for="anime in currentDayAnimes" :key="anime.id" class="schedule-card">
        <div class="card-image">
          <img :src="anime.image" :alt="anime.title" />
          <span class="score">{{ anime.score }}</span>
          <span class="time">{{ anime.time }}</span>
        </div>
        <div class="card-info">
          <h4 class="title">{{ anime.title }}</h4>
          <p class="subtitle">{{ anime.subtitle }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { AlarmClock } from "@icon-park/vue-next";
export default {
  name: "AnimeSchedule",
  components: {
    AlarmClock,
  },
  data() {
    return {
      currentDay: "周一",
      weekDays: [
        { label: "最近更新", value: "最近更新" },
        { label: "周一", value: "周一" },
        { label: "周二", value: "周二" },
        { label: "周三", value: "周三" },
        { label: "周四", value: "周四" },
        { label: "周五", value: "周五" },
        { label: "周六", value: "周六" },
        { label: "周日", value: "周日" },
      ],
      scheduleData: {
        周一: [
          {
            id: 1,
            image:
              "http://113.45.69.13:9000/image/e55865e7a58c374e0dd5c13f552146f716ff026f.jpg",
            title: "末日后宿舍",
            subtitle: "更新至第2话",
            score: "9.8",
            time: "01:40",
          },
          {
            id: 2,
            image:
              "http://113.45.69.13:9000/image/9d731a02054c4807cc0a01fc7768f87d0bab10c1.png",
            title: "直至魔女消逝",
            subtitle: "更新至第2话",
            score: "9.4",
            time: "22:30",
          },
          {
            id: 3,
            image:
              "http://113.45.69.13:9000/image/7f5c1d29ede597590cbfab7820a5096469b6030e.png@560w_746h_!web-ogv-anime-ranking-card.webp",
            title: "最强王者的第二人生",
            subtitle: "更新至第2话",
            score: "9.5",
            time: "19:00",
          },
          {
            id: 4,
            image:
              "http://113.45.69.13:9000/image/41a63442ba47c07f3edeb8169b4d0cc217812e7f.png@560w_746h_!web-ogv-anime-ranking-card.webp",
            title: "青之驱魔师 岛根篇",
            subtitle: "更新至第2话",
            score: "9.5",
            time: "18:30",
          },
        ],
        // 其他日期的数据可以按需添加
      },
    };
  },
  computed: {
    currentDayAnimes() {
      return this.scheduleData[this.currentDay] || [];
    },
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

.time {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 14px;
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
</style>
