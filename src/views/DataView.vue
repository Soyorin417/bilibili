<template>
  <div class="data-view">
    <VideoBar />
    <div class="container">
      <!-- 左侧导航 -->
      <div class="upload-nav">
        <div class="nav-header">
          <h3>数据中心</h3>
        </div>
        <div class="nav-items">
          <div
            class="nav-item"
            :class="{ active: currentView === 'home' }"
            @click="currentView = 'home'"
          >
            <i class="bi bi-house"></i>
            <span>首页</span>
          </div>
          <div
            class="nav-item"
            :class="{ active: currentView === 'content' }"
            @click="currentView = 'content'"
          >
            <i class="bi bi-file-earmark"></i>
            <span>内容管理</span>
          </div>
          <div
            class="nav-item"
            :class="{ active: currentView === 'upload' }"
            @click="$router.push('/upload')"
          >
            <i class="bi bi-cloud-upload"></i>
            <span>视频投稿</span>
          </div>
          <div class="nav-item active">
            <i class="bi bi-bar-chart"></i>
            <span>数据统计</span>
          </div>
        </div>
      </div>

      <!-- 主内容区 -->
      <div class="main-content">
        <div class="stats-header">
          <h2>数据概览</h2>
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
        </div>

        <!-- 数据卡片 -->
        <div class="stats-cards">
          <div class="stat-card" v-for="stat in stats" :key="stat.title">
            <div class="stat-title">{{ stat.title }}</div>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-change" :class="stat.trend">
              {{ stat.change }}
              <i :class="'bi bi-arrow-' + (stat.trend === 'up' ? 'up' : 'down')"></i>
            </div>
          </div>
        </div>

        <!-- Three.js 容器 -->
        <div class="visualization-container">
          <div ref="threeContainer" class="three-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as THREE from "three";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";
import VideoBar from "@/components/navBar/VideoBar.vue";
import { videoInfos } from "@/data/videoInfos";
import { markRaw } from "vue";

export default {
  name: "DataView",
  components: {
    VideoBar,
  },
  data() {
    return {
      currentView: "stats",
      currentRange: "近7天",
      timeRanges: ["今日", "近7天", "近30天", "近90天"],
      stats: [
        {
          title: "播放量",
          value: "12.5万",
          change: "+15.2%",
          trend: "up",
        },
        {
          title: "互动量",
          value: "2.3万",
          change: "+8.7%",
          trend: "up",
        },
        {
          title: "粉丝数",
          value: "5.6万",
          change: "-2.1%",
          trend: "down",
        },
        {
          title: "点赞数",
          value: "1.8万",
          change: "+12.3%",
          trend: "up",
        },
      ],
      scene: null,
      camera: null,
      renderer: null,
      controls: null,
      bars: [],
      animationFrameId: null,
    };
  },
  mounted() {
    this.initThree();
    this.startAnimation();
    window.addEventListener("resize", this.onWindowResize, false);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.onWindowResize);
    this.stopAnimation();
    if (this.renderer) {
      this.renderer.dispose();
    }
  },
  methods: {
    initThree() {
      // Create scene
      this.scene = markRaw(new THREE.Scene());
      this.scene.background = new THREE.Color(0xf0f0f0);

      // Create camera
      this.camera = markRaw(
        new THREE.PerspectiveCamera(
          75,
          this.$refs.threeContainer.clientWidth / this.$refs.threeContainer.clientHeight,
          0.1,
          1000
        )
      );
      this.camera.position.set(0, 5, 10);

      // Create renderer
      this.renderer = markRaw(new THREE.WebGLRenderer({ antialias: true }));
      this.renderer.setSize(
        this.$refs.threeContainer.clientWidth,
        this.$refs.threeContainer.clientHeight
      );
      this.$refs.threeContainer.appendChild(this.renderer.domElement);

      // Add controls
      this.controls = markRaw(new OrbitControls(this.camera, this.renderer.domElement));
      this.controls.enableDamping = true;

      // Add lights
      const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
      this.scene.add(ambientLight);

      const directionalLight = new THREE.DirectionalLight(0xffffff, 0.6);
      directionalLight.position.set(10, 10, 10);
      this.scene.add(directionalLight);

      // Create data visualization
      this.createDataVisualization();
    },

    createDataVisualization() {
      // 清除现有的柱状图
      this.bars.forEach((bar) => this.scene.remove(bar));
      this.bars = [];

      // 获取视频数据
      const data = videoInfos.slice(0, 10); // 只显示前10个视频
      const maxViews = Math.max(...data.map((video) => parseInt(video.views)));

      // 创建柱状图
      data.forEach((video, index) => {
        const views = parseInt(video.views);
        const height = (views / maxViews) * 5; // 最高5个单位

        const geometry = new THREE.BoxGeometry(0.5, height, 0.5);
        const material = new THREE.MeshPhongMaterial({
          color: new THREE.Color(0x00aeec),
          transparent: true,
          opacity: 0.8,
        });

        const bar = new THREE.Mesh(geometry, material);
        bar.position.x = index - data.length / 2; // 居中排列
        bar.position.y = height / 2; // 从底部向上生长

        this.scene.add(bar);
        this.bars.push(bar);

        // 添加标签（使用HTML元素）
        const label = document.createElement("div");
        label.className = "bar-label";
        label.textContent = video.views;
        label.style.position = "absolute";
        label.style.color = "#333";
        label.style.fontSize = "12px";
        this.$refs.threeContainer.appendChild(label);
      });
    },

    startAnimation() {
      const animate = () => {
        this.animationFrameId = requestAnimationFrame(animate);
        if (this.controls && this.renderer && this.scene && this.camera) {
          this.controls.update();
          this.renderer.render(this.scene, this.camera);
        }
      };
      animate();
    },

    stopAnimation() {
      if (this.animationFrameId) {
        cancelAnimationFrame(this.animationFrameId);
        this.animationFrameId = null;
      }
    },

    onWindowResize() {
      if (this.camera && this.renderer) {
        this.camera.aspect =
          this.$refs.threeContainer.clientWidth / this.$refs.threeContainer.clientHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(
          this.$refs.threeContainer.clientWidth,
          this.$refs.threeContainer.clientHeight
        );
      }
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

/* 左侧导航样式 */
.upload-nav {
  width: 200px;
  background: white;
  border-radius: 8px;
  padding: 16px 0;
}

.nav-header {
  padding: 0 16px 16px;
  border-bottom: 1px solid #f4f4f4;
}

.nav-header h3 {
  margin: 0;
  font-size: 16px;
  color: #18191c;
}

.nav-items {
  padding: 8px 0;
}

.nav-item {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #61666d;
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-item:hover {
  background: #f4f4f4;
  color: #00aeec;
}

.nav-item.active {
  color: #00aeec;
  background: #f4f4f4;
}

.nav-item i {
  font-size: 18px;
}

/* 主内容区样式 */
.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

.stats-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.stats-header h2 {
  margin: 0;
  font-size: 20px;
  color: #18191c;
}

.time-range {
  display: flex;
  gap: 8px;
}

.time-range button {
  padding: 6px 12px;
  border: 1px solid #ddd;
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

/* 数据卡片样式 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
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

/* Three.js 容器样式 */
.visualization-container {
  margin-top: 24px;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.three-container {
  width: 100%;
  height: 400px;
  position: relative;
}

.bar-label {
  pointer-events: none;
  text-align: center;
}
</style>
