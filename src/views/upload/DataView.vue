<template>
  <div class="data-view">
    <VideoBar />
    <div class="container">
      <!-- 左侧导航 -->
      <DataNav currentPage="data" />

      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 时间范围选择 -->
        <div class="time-range">
          <button
            v-for="range in timeRanges"
            :key="range"
            :class="{ active: currentRange === range }"
            @click="currentRange = range"
          >
            {{ range }}
          </button>
        </div>

        <!-- 数据概览卡片 -->
        <div class="overview-cards">
          <div class="overview-card" v-for="stat in stats" :key="stat.title">
            <div class="stat-title">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-change" :class="stat.trend">
              <i :class="'bi bi-arrow-' + (stat.trend === 'up' ? 'up' : 'down')"></i>
              {{ stat.change }}
            </div>
          </div>
        </div>

        <!-- 图表区域 -->
        <div class="charts-section">
          <!-- 播放量趋势图 -->
          <div class="chart-card">
            <div class="chart-header">
              <h3>播放量趋势</h3>
              <div class="chart-legend">
                <span class="legend-item">
                  <i class="bi bi-circle-fill" style="color: #00aeec"></i>
                  播放量
                </span>
              </div>
            </div>
            <div class="chart-container">
              <canvas ref="viewsChart"></canvas>
            </div>
          </div>

          <!-- 互动数据图 -->
          <div class="chart-card">
            <div class="chart-header">
              <h3>互动数据</h3>
              <div class="chart-legend">
                <span class="legend-item">
                  <i class="bi bi-circle-fill" style="color: #ff4d4f"></i>
                  点赞
                </span>
                <span class="legend-item">
                  <i class="bi bi-circle-fill" style="color: #52c41a"></i>
                  投币
                </span>
                <span class="legend-item">
                  <i class="bi bi-circle-fill" style="color: #faad14"></i>
                  收藏
                </span>
              </div>
            </div>
            <div class="chart-container">
              <canvas ref="interactionChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import DataNav from "@/components/navBar/DataNav.vue";
import { Chart, registerables } from "chart.js";
Chart.register(...registerables);

export default {
  name: "DataView",
  components: {
    VideoBar,
    DataNav,
  },
  data() {
    return {
      currentRange: "近7天",
      timeRanges: ["今日", "近7天", "近30天", "近90天"],
      stats: [
        {
          title: "总播放量",
          value: "12.5万",
          change: "15.2%",
          trend: "up",
        },
        {
          title: "平均播放量",
          value: "2.3万",
          change: "8.7%",
          trend: "up",
        },
        {
          title: "互动率",
          value: "5.6%",
          change: "2.1%",
          trend: "down",
        },
        {
          title: "粉丝增长",
          value: "1.8万",
          change: "12.3%",
          trend: "up",
        },
      ],
      viewsChart: null,
      interactionChart: null,
    };
  },
  mounted() {
    this.initCharts();
    window.addEventListener("resize", this.onWindowResize);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.onWindowResize);
    if (this.viewsChart) this.viewsChart.destroy();
    if (this.interactionChart) this.interactionChart.destroy();
  },
  methods: {
    initCharts() {
      this.initViewsChart();
      this.initInteractionChart();
    },
    initViewsChart() {
      const ctx = this.$refs.viewsChart.getContext("2d");
      this.viewsChart = new Chart(ctx, {
        type: "line",
        data: {
          labels: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
          datasets: [
            {
              label: "播放量",
              data: [12000, 19000, 15000, 25000, 22000, 30000, 28000],
              borderColor: "#00aeec",
              backgroundColor: "rgba(0, 174, 236, 0.1)",
              tension: 0.4,
              borderWidth: 2,
              pointRadius: 0,
              pointHoverRadius: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            tooltip: {
              mode: "index",
              intersect: false,
            },
          },
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: "rgba(0, 0, 0, 0.05)",
              },
              ticks: {
                callback: function (value) {
                  return value.toLocaleString() + "次";
                },
              },
            },
            x: {
              grid: {
                display: false,
              },
            },
          },
        },
      });
    },
    initInteractionChart() {
      const ctx = this.$refs.interactionChart.getContext("2d");
      this.interactionChart = new Chart(ctx, {
        type: "line",
        data: {
          labels: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
          datasets: [
            {
              label: "点赞",
              data: [1200, 1900, 1500, 2500, 2200, 3000, 2800],
              borderColor: "#ff4d4f",
              backgroundColor: "rgba(255, 77, 79, 0.1)",
              tension: 0.4,
              borderWidth: 2,
              pointRadius: 0,
              pointHoverRadius: 4,
            },
            {
              label: "投币",
              data: [800, 1200, 1000, 1500, 1300, 2000, 1800],
              borderColor: "#52c41a",
              backgroundColor: "rgba(82, 196, 26, 0.1)",
              tension: 0.4,
              borderWidth: 2,
              pointRadius: 0,
              pointHoverRadius: 4,
            },
            {
              label: "收藏",
              data: [1000, 1500, 1200, 1800, 1600, 2200, 2000],
              borderColor: "#faad14",
              backgroundColor: "rgba(250, 173, 20, 0.1)",
              tension: 0.4,
              borderWidth: 2,
              pointRadius: 0,
              pointHoverRadius: 4,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            tooltip: {
              mode: "index",
              intersect: false,
            },
          },
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: "rgba(0, 0, 0, 0.05)",
              },
              ticks: {
                callback: function (value) {
                  return value.toLocaleString() + "次";
                },
              },
            },
            x: {
              grid: {
                display: false,
              },
            },
          },
        },
      });
    },
    onWindowResize() {
      if (this.viewsChart) this.viewsChart.resize();
      if (this.interactionChart) this.interactionChart.resize();
    },
  },
};
</script>

<style scoped>
.data-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 20px;
}

.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

/* 时间范围选择器 */
.time-range {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}

.time-range button {
  padding: 6px 12px;
  border: 1px solid #e3e5e7;
  border-radius: 4px;
  background: white;
  color: #61666d;
  cursor: pointer;
  transition: all 0.3s ease;
}

.time-range button.active {
  background: #00aeec;
  color: white;
  border-color: #00aeec;
}

/* 数据概览卡片 */
.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.overview-card {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  text-align: center;
}

.stat-title {
  color: #61666d;
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #18191c;
  margin-bottom: 8px;
}

.stat-change {
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.stat-change.up {
  color: #52c41a;
}

.stat-change.down {
  color: #ff4d4f;
}

/* 图表区域 */
.charts-section {
  display: grid;
  gap: 24px;
}

.chart-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  color: #18191c;
}

.chart-legend {
  display: flex;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #61666d;
}

.chart-container {
  height: 300px;
  position: relative;
}
</style>
