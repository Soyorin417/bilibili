<template>
  <div class="video-container">
    <video
      ref="videoPlayer"
      class="video-player"
      controls
      :src="videoUrl"
      @play="onPlay"
      @timeupdate="onTimeUpdate"
      @seeked="onSeek"
    ></video>

    <div class="danmaku-display-area" v-show="localVisible" ref="displayArea">
      <div
        v-for="danmaku in activeDanmakus"
        :key="danmaku.id"
        class="danmaku-item"
        :class="'type-' + danmaku.type"
        :style="{
          top: danmaku.style.top,
          color: danmaku.style.color,
          'animation-duration': danmaku.style.animationDuration,
          'font-size': danmaku.style.fontSize,
        }"
        @animationend="removeDanmaku(danmaku.id)"
      >
        {{ danmaku.text }}
      </div>
    </div>
  </div>
</template>

<script>
import { parseDanmakuXml } from "../danmaku/danmakuParser";

export default {
  name: "DanmakuDisplay",
  props: {
    url: {
      type: String,
      required: true,
    },
    videoId: {
      type: String,
      required: true,
    },
    isVisible: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      danmakus: [],
      activeDanmakus: [],
      currentTime: 0,
      nextIndex: 0,
      duration: 8,
      localVisible: this.isVisible,
    };
  },
  async mounted() {
    try {
      const response = await fetch(
        `http://121.36.211.155:9000/danmaku/bv${this.videoId}.xml`
      );
      const xmlText = await response.text();
      this.danmakus = parseDanmakuXml(xmlText);
    } catch (error) {
      console.error("获取弹幕数据失败:", error);
    }
  },
  computed: {
    videoUrl() {
      return this.url ? encodeURI(this.url) : "";
    },
  },
  methods: {
    onSeek() {
      // 找到第一个时间 >= currentTime 的弹幕索引
      const idx = this.danmakus.findIndex((d) => d.time >= this.currentTime);
      this.nextIndex = idx >= 0 ? idx : this.danmakus.length;
      // 清空当前屏幕弹幕
      this.activeDanmakus = [];
    },
    onTimeUpdate() {
      if (this.$refs.videoPlayer) {
        this.currentTime = this.$refs.videoPlayer.currentTime;
        this.updateActiveDanmakus();
      }
    },
    checkVideoStatus() {
      if (this.$refs.videoPlayer) {
        this.danmakuEnabled = !this.$refs.videoPlayer.ended;
      }
    },
    removeDanmaku(id) {
      const idx = this.activeDanmakus.findIndex((d) => d.id === id);
      if (idx !== -1) {
        this.activeDanmakus.splice(idx, 1);
      }
    },
    // 检查弹幕是否应该显示
    shouldShowDanmaku(danmaku) {
      // 放宽时间范围，从1.0秒改为0.5秒
      return Math.abs(danmaku.time - this.currentTime) < 0.5;
    },
    // 更新活跃弹幕
    updateActiveDanmakus() {
      if (!this.danmakus.length) return;

      // 获取当前时间附近的弹幕
      const currentDanmakus = this.danmakus.filter((danmaku) =>
        this.shouldShowDanmaku(danmaku)
      );

      // 处理每个弹幕
      currentDanmakus.forEach((danmaku) => {
        // 检查是否已经显示过这个弹幕
        const isAlreadyShown = this.activeDanmakus.some(
          (active) => active.text === danmaku.text && active.time === danmaku.time
        );
        if (isAlreadyShown) return;

        if (danmaku.isAdvanced) {
          // 高级弹幕 - 直接显示
          const id = Date.now() + Math.random();
          const activeDanmaku = {
            id,
            text: danmaku.text,
            type: "advanced",
            time: danmaku.time,
            isSelf: danmaku.isSelf,
            style: {
              top: `${danmaku.position.y}%`,
              left: `${danmaku.position.x}%`,
              color: danmaku.position.color || danmaku.color || "#fff",
              animationDuration: `${danmaku.position.duration || 8}s`,
              fontSize: `${danmaku.position.size || danmaku.fontSize || 24}px`,
              transform: `scale(${danmaku.position.scaleX || 1}, ${
                danmaku.position.scaleY || 1
              }) rotate(${danmaku.position.rotation || 0}deg)`,
              opacity: danmaku.position.alpha || 1,
              fontFamily: danmaku.position.fontFamily || "inherit",
              whiteSpace: "pre-wrap",
              textAlign: "center",
              transformOrigin: "center center",
              textDecoration: danmaku.isSelf ? "underline" : "none",
            },
          };
          this.activeDanmakus.push(activeDanmaku);
        } else if (danmaku.type === 1) {
          // 滚动弹幕 - 直接显示
          const id = Date.now() + Math.random();
          const activeDanmaku = {
            id,
            text: danmaku.text,
            type: danmaku.type,
            time: danmaku.time,
            isSelf: danmaku.isSelf,
            style: {
              top: `${Math.random() * 80}%`,
              color: danmaku.color || "#fff",
              animationDuration: `${this.duration}s`,
              fontSize: `${danmaku.fontSize || 24}px`,
              textDecoration: danmaku.isSelf ? "underline" : "none",
            },
          };
          this.activeDanmakus.push(activeDanmaku);
        } else {
          // 固定弹幕（顶部或底部）- 直接显示
          const id = Date.now() + Math.random();
          const isTop = danmaku.type === 5;
          const activeDanmaku = {
            id,
            text: danmaku.text,
            type: danmaku.type,
            time: danmaku.time,
            isSelf: danmaku.isSelf,
            style: {
              top: isTop ? `${Math.random() * 20}%` : `${80 + Math.random() * 20}%`,
              color: danmaku.color || "#fff",
              animationDuration: `${this.duration}s`,
              fontSize: `${danmaku.fontSize || 24}px`,
              textDecoration: danmaku.isSelf ? "underline" : "none",
            },
          };
          this.activeDanmakus.push(activeDanmaku);
        }
      });

      // 清理已经过期的弹幕
      this.activeDanmakus = this.activeDanmakus.filter((danmaku) => {
        const danmakuTime = this.danmakus.find((d) => d.text === danmaku.text)?.time || 0;
        return this.currentTime - danmakuTime < this.duration;
      });
    },
    // 添加新弹幕
    addNewDanmaku(danmakuData) {
      console.log("Adding new danmaku:", danmakuData);
      const { content, type } = danmakuData;
      const newDanmaku = {
        text: content,
        time: this.currentTime,
        type: type === "scroll" ? 1 : 5,
        color: "#ffffff",
        fontSize: 24,
        isSelf: true, // 标记是自己发送的弹幕
      };

      // 添加到弹幕列表
      this.danmakus.push(newDanmaku);

      // 立即显示弹幕
      const id = Date.now() + Math.random();
      const activeDanmaku = {
        id,
        text: content,
        type: newDanmaku.type,
        time: this.currentTime,
        isSelf: true,
        style: {
          top: type === "scroll" ? `${Math.random() * 80}%` : `${Math.random() * 20}%`,
          color: "#ffffff",
          animationDuration: `${this.duration}s`,
          fontSize: "24px",
          textDecoration: "underline", // 添加下划线
        },
      };

      this.activeDanmakus.push(activeDanmaku);
    },
    getAuthorId() {
      return (
        this.videoInfo.authorId ||
        this.videoInfo.author_id ||
        this.videoInfo.author?.id ||
        this.videoInfo.author?.uid
      );
    },
    onPlay() {
      this.$emit("play");
    },
  },
  watch: {
    currentTime: {
      handler() {
        if (this.localVisible) {
          this.updateActiveDanmakus();
        }
      },
      immediate: true, // 添加immediate选项，确保组件创建时就执行一次
    },
    isVisible: {
      immediate: true,
      handler(value) {
        this.localVisible = value;
      },
    },
  },
};
</script>

<style scoped>
.video-container {
  position: relative;
  width: 100%;
  max-width: 100%;
  aspect-ratio: 16 / 9;
  overflow: hidden;
  margin: 0 auto;
  background-color: #000;
}

.video-player {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.danmaku-display-area {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  z-index: 10;
}

.danmaku-item {
  position: absolute;
  white-space: nowrap;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  will-change: transform;
  color: #fff;
  font-size: 24px;
  z-index: 20;
  max-width: 100%;
  overflow: hidden;
}

/* 滚动弹幕样式 */
.danmaku-item[class*="type-1"] {
  left: 100%;
  transform: translateX(0);
  animation-name: moveLeft;
  animation-timing-function: linear;
  animation-fill-mode: forwards;
  animation-duration: 8s;
  max-width: 100%;
}

/* 固定弹幕样式 */
.danmaku-item[class*="type-5"],
.danmaku-item[class*="type-4"] {
  left: 50%;
  transform: translateX(-50%);
  animation-name: fadeInOut;
  animation-timing-function: linear;
  animation-fill-mode: forwards;
  text-align: center;
  width: 100%;
  animation-duration: 6s;
  max-width: 100%;
  padding: 0 10px;
}

/* 高级弹幕样式 */
.danmaku-item[class*="type-advanced"] {
  animation-name: fadeInOut;
  animation-timing-function: linear;
  animation-fill-mode: forwards;
  transform-origin: center center;
  will-change: transform, opacity;
  animation-duration: 4s;
}

@keyframes moveLeft {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100vw);
  }
}

@keyframes fadeInOut {
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
