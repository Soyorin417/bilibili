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
          left: danmaku.style.left,
          color: danmaku.style.color,
          'animation-duration': danmaku.style.animationDuration,
          'font-size': danmaku.style.fontSize,
          'text-decoration': danmaku.style.textDecoration,
          'text-decoration-color': danmaku.style.textDecorationColor,
          'text-decoration-thickness': danmaku.style.textDecorationThickness,
          'text-decoration-style': danmaku.style.textDecorationStyle,
          transform: danmaku.style.transform,
          opacity: danmaku.style.opacity,
          'font-family': danmaku.style.fontFamily,
        }"
        @animationend="removeDanmaku(danmaku.id)"
      >
        {{ danmaku.text }}
      </div>
    </div>
  </div>
</template>

<script>
import { parseDanmakuXml } from "@/utils/danmakuParser";
import { danmakuApi } from "@/api/content/danmaku";
import danmuApi from "@/api/danmu";
import { convertDbDanmuToFrontend } from "@/utils/convertDbDanmuToFrontend";

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
    currentTime: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      danmakus: [],
      activeDanmakus: [],
      nextIndex: 0,
      duration: 8,
      localVisible: this.isVisible,
      localCurrentTime: this.currentTime,
    };
  },
  async mounted() {
    try {
      let xmlDanmaku = [];
      // 1. 尝试获取本地弹幕XML
      try {
        const responseData = await danmakuApi.getDanmakuById(this.videoId);
        console.log("XML弹幕响应:", responseData);
        if (responseData.data && responseData.data.url) {
          const url = responseData.data.url;
          console.log("XML弹幕URL:", url);
          const response = await fetch(url);
          const xmlText = await response.text();
          xmlDanmaku = parseDanmakuXml(xmlText);
          console.log("解析到的XML弹幕:", xmlDanmaku);
        }
      } catch (xmlError) {
        console.warn("XML弹幕解析失败，将使用数据库弹幕:", xmlError);
      }

      // 2. 获取数据库弹幕
      const dbResponse = await danmuApi.getDanmuList(this.videoId);
      console.log("数据库弹幕响应:", dbResponse.data);

      // 3. 转换数据库弹幕格式
      const dbDanmaku = (dbResponse.data || []).map((d) => convertDbDanmuToFrontend(d));
      console.log("转换后的数据库弹幕:", dbDanmaku);

      // 4. 合并弹幕数据
      this.danmakus = [...xmlDanmaku, ...dbDanmaku];

      // 5. 按时间排序
      this.danmakus.sort((a, b) => a.time - b.time);
      console.log("合并后的弹幕总数:", this.danmakus.length);
    } catch (error) {
      console.error("获取弹幕数据失败:", error);
      this.danmakus = [];
    }

    this.$nextTick(() => {
      if (this.$refs.videoPlayer) {
        this.$refs.videoPlayer.addEventListener("error", (e) => {
          console.error("视频播放错误:", e);
        });
        this.$refs.videoPlayer.addEventListener("timeupdate", () => {
          this.onTimeUpdate();
        });
        this.$refs.videoPlayer.addEventListener("seeking", () => {
          this.onSeek();
        });
        this.$refs.videoPlayer.addEventListener("seeked", () => {
          this.onSeek();
        });
      }
    });
  },
  computed: {
    videoUrl() {
      console.log("原始URL:", this.url);
      const finalUrl = this.url;
      console.log("处理后的URL:", finalUrl);
      return finalUrl;
    },
  },
  methods: {
    onSeek() {
      const idx = this.danmakus.findIndex((d) => d.time >= this.localCurrentTime);
      this.nextIndex = idx >= 0 ? idx : this.danmakus.length;
      this.activeDanmakus = [];
    },
    onTimeUpdate() {
      if (this.$refs.videoPlayer) {
        this.localCurrentTime = this.$refs.videoPlayer.currentTime;
        this.$emit("update:currentTime", this.localCurrentTime);
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
    shouldShowDanmaku(danmaku) {
      // 放宽时间范围，从1.0秒改为0.5秒
      return Math.abs(danmaku.time - this.localCurrentTime) < 0.5;
    },
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
        return this.localCurrentTime - danmakuTime < this.duration;
      });
    },
    addNewDanmaku(danmaku) {
      console.log("添加新弹幕:", danmaku);

      // 创建新的活动弹幕
      const id = Date.now() + Math.random();
      const activeDanmaku = {
        id,
        text: danmaku.text,
        type: danmaku.mode,
        time: danmaku.timeInVideo,
        isSelf: danmaku.isSelf,
        style: {
          color: danmaku.fontColor || "#fff",
          fontSize: `${danmaku.fontSize || 24}px`,
          textDecoration: danmaku.isSelf ? "underline" : "none",
        },
      };

      // 根据弹幕类型设置不同的样式
      if (danmaku.mode === 1) {
        // 滚动弹幕
        activeDanmaku.style = {
          ...activeDanmaku.style,
          top: `${danmaku.track * 10 + 5}%`,
          animationDuration: `${this.duration}s`,
        };
      } else if (danmaku.mode === 4) {
        // 顶部弹幕
        activeDanmaku.style = {
          ...activeDanmaku.style,
          top: `${danmaku.track * 10 + 5}%`,
          left: "50%",
          transform: "translateX(-50%)",
        };
      } else if (danmaku.mode === 5) {
        // 底部弹幕
        activeDanmaku.style = {
          ...activeDanmaku.style,
          top: `${danmaku.track * 10 + 5}%`,
          left: "50%",
          transform: "translateX(-50%)",
        };
      }

      // 立即添加到活动弹幕列表
      this.activeDanmakus.push(activeDanmaku);

      // 同时添加到弹幕列表（用于后续播放）
      this.danmakus.push(danmaku);
      // 按时间排序
      this.danmakus.sort((a, b) => a.timeInVideo - b.timeInVideo);
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
    handleNewDanmaku(danmaku) {
      // 添加到弹幕池
      this.danmakus.push(danmaku);
      this.danmakus.sort((a, b) => a.time - b.time);

      // 如果当前时间接近，立即显示
      if (Math.abs(danmaku.time - this.localCurrentTime) < 0.5) {
        const activeDanmaku = {
          id: Date.now(),
          text: danmaku.text,
          type: danmaku.type,
          time: danmaku.time,
          isSelf: danmaku.isSelf,
          style: {
            color: danmaku.color || "#ffffff",
            fontSize: `${danmaku.fontSize || 24}px`,
            animationDuration: `${this.duration}s`,
          },
        };
        this.activeDanmakus.push(activeDanmaku);
      }
    },
    updateVisibility(isVisible) {
      this.localVisible = isVisible;
      if (!isVisible) {
        this.activeDanmakus = [];
      }
    },
    clearCurrentDanmaku() {
      this.activeDanmakus = [];
    },
  },
  watch: {
    currentTime: {
      handler(value) {
        this.localCurrentTime = value;
        if (this.localVisible) {
          this.updateActiveDanmakus();
        }
      },
      immediate: true,
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
  pointer-events: none;
  user-select: none;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  will-change: transform;
  z-index: 1;
  text-decoration: inherit;
  text-decoration-color: inherit;
  text-decoration-thickness: inherit;
  text-decoration-style: inherit;
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
