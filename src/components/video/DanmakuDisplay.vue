<template>
  <div class="danmaku-display-area" v-show="isVisible" ref="displayArea">
    <div
      v-for="item in visibleDanmaku"
      :key="item.key"
      :style="getDanmakuStyle(item)"
      :class="[
        'danmaku-item',
        item.type === 'scroll' ? 'danmaku-scroll' : 'danmaku-fixed',
      ]"
    >
      {{ item.content }}
    </div>
  </div>
</template>

<script>
export default {
  name: "DanmakuDisplay",
  props: {
    isVisible: {
      type: Boolean,
      default: true,
    },
    activeDanmaku: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      visibleDanmaku: [],
      maxVisibleCount: 50,
      tracks: [],
      trackCount: 10,
      animationFrame: null,
      danmakuQueue: [],
      lastProcessedIndex: -1,
    };
  },
  watch: {
    activeDanmaku: {
      handler(newVal) {
        // 只处理新增的弹幕
        if (newVal.length > this.lastProcessedIndex + 1) {
          const newDanmaku = newVal.slice(this.lastProcessedIndex + 1);
          this.danmakuQueue.push(...newDanmaku);
          this.lastProcessedIndex = newVal.length - 1;
        }
      },
    },
  },
  methods: {
    getDanmakuStyle(item) {
      const style = {
        transform:
          item.type === "scroll"
            ? `translate3d(${item.position}px, ${item.track * 40}px, 0)`
            : `translate3d(-50%, ${item.track * 40}px, 0)`,
        color: item.color,
      };

      if (item.type === "fixed") {
        style.left = "50%";
      }

      return style;
    },
    getRandomColor() {
      const colors = ["#ffffff", "#ff9999", "#99ff99", "#9999ff", "#ffff99"];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    findAvailableTrack() {
      const now = Date.now();
      for (let i = 0; i < this.trackCount; i++) {
        if (!this.tracks[i] || now - this.tracks[i] > 2000) {
          this.tracks[i] = now;
          return i;
        }
      }
      return Math.floor(Math.random() * this.trackCount);
    },
    handleResize() {
      if (this.$refs.displayArea) {
        const containerWidth = this.$refs.displayArea.clientWidth;
        this.visibleDanmaku.forEach((item) => {
          if (item.type === "scroll") {
            const elapsed = Date.now() - item.startTime;
            const progress = elapsed / item.duration;
            item.position = containerWidth * (1 - progress);
          }
        });
      }
    },
    updateDanmaku() {
      const now = Date.now();
      const containerWidth = this.$refs.displayArea?.clientWidth || 1000;

      // 更新现有弹幕位置
      this.visibleDanmaku = this.visibleDanmaku.filter((item) => {
        if (item.type === "scroll") {
          const elapsed = now - item.startTime;
          const duration = item.duration;

          if (elapsed >= duration) {
            return false;
          }

          const progress = elapsed / duration;
          item.position = containerWidth * (1 - progress);
          return true;
        } else {
          return now - item.startTime < 5000;
        }
      });

      // 添加新弹幕
      while (
        this.visibleDanmaku.length < this.maxVisibleCount &&
        this.danmakuQueue.length > 0
      ) {
        const danmaku = this.danmakuQueue.shift();
        if (danmaku) {
          const track = this.findAvailableTrack();
          this.visibleDanmaku.push({
            ...danmaku,
            key: now + Math.random(),
            track,
            startTime: now,
            position: containerWidth,
            color: this.getRandomColor(),
            duration: Math.min(12000, 8000 + danmaku.content.length * 200),
          });
        }
      }

      this.animationFrame = requestAnimationFrame(this.updateDanmaku);
    },
    initDanmakuQueue() {
      // 初始化时添加现有的弹幕到队列
      if (this.activeDanmaku.length > 0) {
        this.danmakuQueue.push(...this.activeDanmaku);
        this.lastProcessedIndex = this.activeDanmaku.length - 1;
      }
    },
  },
  mounted() {
    this.initDanmakuQueue();
    this.updateDanmaku();
    window.addEventListener("resize", this.handleResize);
  },
  beforeUnmount() {
    if (this.animationFrame) {
      cancelAnimationFrame(this.animationFrame);
    }
    window.removeEventListener("resize", this.handleResize);
  },
};
</script>

<style scoped>
/* 弹幕显示区域 */
.danmaku-display-area {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  transform: translateZ(0);
  backface-visibility: hidden;
  z-index: 2;
}

.danmaku-item {
  position: absolute;
  font-size: 24px;
  white-space: nowrap;
  will-change: transform;
  pointer-events: none;
  z-index: auto;

  /* 增强可读性 */
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.8), -1px -1px 3px rgba(0, 0, 0, 0.8);

  /* 优化渲染性能 */
  contain: content;
  backface-visibility: hidden;
  transform: translateZ(0);
}

.danmaku-running {
  animation: danmaku-scroll var(--duration) linear forwards;
  will-change: transform;
}

.danmaku-fixed {
  animation: danmakuFade 5s ease-out forwards;
}

@keyframes danmaku-scroll {
  from {
    transform: translateX(100vw);
  }
  to {
    transform: translateX(-100%);
  }
}

@keyframes danmakuFade {
  0% {
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
</style>
