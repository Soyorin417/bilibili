<template>
  <div>
    <VideoBar></VideoBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="row">
          <!-- 左侧区域：视频播放器和视频信息 -->
          <div class="col-lg-9 mt-4">
            <div class="row">
              <div class="col-8">
                <h4 class="d-flex">
                  {{ videoInfo.title }}
                </h4>
                <div class="d-flex align-items-center video-stats">
                  <play-two
                    theme="outline"
                    size="18"
                    fill="#666666"
                    :strokeWidth="2"
                    class="me-1"
                  />
                  <span class="me-3">{{ videoInfo.views }}</span>
                  <comment-one
                    theme="outline"
                    size="18"
                    fill="#666666"
                    :strokeWidth="2"
                    class="me-1"
                  />
                  <span class="me-3">{{ videoInfo.comments }}</span>
                  <span class="me-3">{{ videoInfo.time }}</span>
                  <span v-if="!videoInfo.show_right" class="ms-2">
                    <forbid theme="outline" size="18" fill="#fc0404" class="me-1" />
                    <span class="text-danger">未经作者授权，禁止转载</span>
                  </span>
                </div>
              </div>
            </div>

            <!-- 视频播放器 -->
            <div class="video-player-container mt-4" id="videoStatus">
              <div class="video-player bg-dark position-relative">
                <video
                  class="w-100"
                  controls
                  :poster="videoInfo.image"
                  :src="videoInfo.video_url"
                  ref="videoPlayer"
                  @timeupdate="onTimeUpdate"
                  @play="checkVideoStatus"
                  @pause="checkVideoStatus"
                  @seeking="checkVideoStatus"
                >
                  <source :src="videoInfo.video_url" type="video/mp4" />
                </video>

                <!-- 弹幕显示区域 -->
                <DanmakuDisplay
                  :isVisible="isDanmakuVisible"
                  :activeDanmaku="activeDanmaku"
                />
              </div>
            </div>

            <!-- 弹幕控制条 -->
            <DanmakuControl @send-danmaku="sendDanmaku" @toggle-danmaku="toggleDanmaku" />

            <!-- 视频互动区域 -->
            <div class="video-actions d-flex align-items-center mt-3 pb-3 border-bottom">
              <div class="d-flex align-items-center me-4">
                <button class="btn btn-outline-secondary me-2">
                  <i class="bi bi-hand-thumbs-up"></i> 30.0万
                </button>
                <button class="btn btn-outline-secondary">
                  <i class="bi bi-hand-thumbs-down"></i> 8.3万
                </button>
              </div>
              <button class="btn btn-outline-secondary me-3">
                <i class="bi bi-star"></i> 收藏 7.4万
              </button>
              <button class="btn btn-outline-secondary">
                <i class="bi bi-share"></i> 分享 3.2万
              </button>
            </div>

            <!-- 视频简介 -->
            <div class="video-info mt-3">
              <div class="d-flex">
                <div class="user-avatar me-3">
                  <img
                    src="http://113.45.69.13:9000/image/lucy_moon.jpg"
                    class="rounded-circle"
                    width="48"
                    height="48"
                    alt="user avatar"
                  />
                </div>
                <div>
                  <h6 class="mb-0">陈暖暖</h6>
                  <small class="text-muted">发布于 2023-03-02 17:17:30</small>
                  <p class="mt-2">这是一个神奇的视频，希望大家喜欢！</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧区域：推荐视频 -->
          <div class="col-lg-3">
            <!-- 作者信息区域 -->
            <AuthorInfo
              :videoInfo="videoInfo"
              :userInfo="userInfo"
              @toggle-follow="is_follow"
            />

            <!-- 弹幕列表 -->
            <DanmakuList :danmakuList="danmakuList" class="mb-1" />

            <!-- 推荐视频列表 -->
            <RecommendedVideos
              :recommendedVideos="recommendedVideos"
              :to="'/video/' + videoInfo.id"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import { PlayTwo, CommentOne, Forbid } from "@icon-park/vue-next";
import { danmakuList } from "@/data/danmakuData";
import { userInfo } from "@/data/userInfoData";
import { videoInfo } from "@/data/videoData";
import { recommendedVideos } from "@/data/recommendedVideoData";
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";
import DanmakuList from "@/components/video/DanmakuList.vue";
import DanmakuControl from "@/components/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/video/DanmakuDisplay.vue";

export default {
  components: {
    VideoBar,
    PlayTwo,
    CommentOne,
    Forbid,
    RecommendedVideos,
    AuthorInfo,
    DanmakuList,
    DanmakuControl,
    DanmakuDisplay,
  },
  props: {
    id: {
      type: [String, Number],
      required: true,
    },
  },
  data() {
    return {
      is_show: false,
      showDanmaku: true,
      danmakuEnabled: true,
      danmakuText: "",
      activeDanmaku: [],
      currentTime: 0,
      currentDanmakuType: "scroll",
      danmakuList,
      userInfo,
      videoInfo,
      recommendedVideos,
      danmakuDisplayList: [],
      danmakuInterval: null,
      lastCheckTime: 0,
      checkInterval: 50,
      maxDanmakuCount: 50,
      animationFrame: null,
      danmakuStartTime: {},
    };
  },
  watch: {
    // 监听路由参数变化
    id: {
      immediate: true,
      handler(newId) {
        console.log("Video ID changed:", newId);
        this.loadVideoData(newId);
      },
    },
  },
  methods: {
    loadVideoData(id) {
      // 从推荐视频列表中查找对应ID的视频
      const video = recommendedVideos.find((v) => v.id === parseInt(id));
      if (video) {
        // 更新视频信息
        this.videoInfo = {
          ...this.videoInfo,
          ...video,
          id: parseInt(id),
        };
      }
      // 重置视频播放器
      if (this.$refs.videoPlayer) {
        this.$refs.videoPlayer.currentTime = 0;
        this.$refs.videoPlayer.pause();
      }
      // 清空弹幕列表
      this.activeDanmaku = [];
    },
    is_follow() {
      this.userInfo.is_follow = !this.userInfo.is_follow;
    },
    // 时间格式化为秒
    timeToSeconds(timeStr) {
      const parts = timeStr.split(":");
      return parseInt(parts[0]) * 60 + parseInt(parts[1]);
    },
    // 视频时间更新事件
    onTimeUpdate() {
      if (!this.$refs.videoPlayer) return;

      const video = this.$refs.videoPlayer;
      this.currentTime = video.currentTime;

      // 只在视频播放时检查弹幕
      if (!video.paused) {
        const now = Date.now();
        if (now - this.lastCheckTime >= this.checkInterval) {
          this.checkDanmakuTiming();
          this.lastCheckTime = now;
        }
      }
    },
    checkVideoStatus() {
      const video = this.$refs.videoPlayer;
      if (video) {
        // 保持弹幕始终显示，只在视频真正结束时才隐藏
        this.danmakuEnabled = !video.ended;
      }
    },
    // 检查是否有弹幕需要显示
    checkDanmakuTiming() {
      const currentTime = this.$refs.videoPlayer.currentTime;

      // 使用二分查找优化匹配
      const targetTime = Math.floor(currentTime * 10) / 10; // 精确到0.1秒
      const index = this.danmakuList.findIndex(
        (d) => Math.abs(this.timeToSeconds(d.time) - targetTime) < 0.3
      );

      if (index !== -1) {
        const danmaku = this.danmakuList[index];
        this.showNewDanmaku(danmaku.content, danmaku.type || "scroll");
      }
    },
    // 获取可用轨道
    getAvailableTrack() {
      const maxTracks = 10; // 最大轨道数
      const trackOccupancy = new Array(maxTracks).fill(0); // 记录每个轨道的占用情况

      // 计算每个轨道的占用情况
      this.activeDanmaku.forEach((danmaku) => {
        if (danmaku.type === "scroll") {
          trackOccupancy[danmaku.track]++;
        }
      });

      // 优先选择占用最少的轨道
      let minOccupancy = Infinity;
      let bestTrack = 0;

      for (let i = 0; i < maxTracks; i++) {
        if (trackOccupancy[i] < minOccupancy) {
          minOccupancy = trackOccupancy[i];
          bestTrack = i;
        }
      }

      return bestTrack;
    },
    // 获取随机颜色
    getRandomColor() {
      const colors = [
        "#ffffff", // 白色
        "#ff9999", // 浅红色
        "#99ff99", // 浅绿色
        "#9999ff", // 浅蓝色
        "#ffff99", // 浅黄色
        "#ffcc99", // 浅橙色
        "#cc99ff", // 浅紫色
      ];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    sendDanmaku({ content, type }) {
      // 获取当前视频时间
      const minutes = Math.floor(this.currentTime / 60);
      const seconds = Math.floor(this.currentTime % 60);
      const timeStr = `${minutes
        .toString()
        .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;

      // 创建新弹幕
      const now = new Date();
      const month = (now.getMonth() + 1).toString().padStart(2, "0");
      const day = now.getDate().toString().padStart(2, "0");
      const hours = now.getHours().toString().padStart(2, "0");
      const mins = now.getMinutes().toString().padStart(2, "0");

      const newDanmaku = {
        time: timeStr,
        content: content,
        sendTime: `${month}-${day} ${hours}:${mins}`,
        type: type,
      };

      // 添加到弹幕列表
      this.danmakuList.unshift(newDanmaku);

      // 立即显示新发送的弹幕
      this.showNewDanmaku(content, type);
    },
    toggleDanmaku(value) {
      this.showDanmaku = value;
    },
    throttle(func, limit) {
      let inThrottle;
      return function (...args) {
        if (!inThrottle) {
          func.apply(this, args);
          inThrottle = true;
          setTimeout(() => (inThrottle = false), limit);
        }
      };
    },
    updateDanmaku() {
      const now = Date.now();
      this.activeDanmaku = this.activeDanmaku.filter((danmaku) => {
        const duration = danmaku.type === "scroll" ? 8000 : 5000;
        return now - danmaku.key < duration;
      });

      this.animationFrame = requestAnimationFrame(this.updateDanmaku);
    },
    showNewDanmaku(content, type = "scroll") {
      // 创建新弹幕对象
      const danmaku = {
        content,
        type,
      };

      // 添加到活动弹幕列表
      this.activeDanmaku = [...this.activeDanmaku, danmaku];

      // 限制活动弹幕数量
      if (this.activeDanmaku.length > 100) {
        this.activeDanmaku = this.activeDanmaku.slice(-100);
      }
    },
  },
  computed: {
    videoId() {
      return this.id;
    },
    // 最终的弹幕显示状态
    isDanmakuVisible() {
      return this.showDanmaku && this.danmakuEnabled;
    },
  },
  mounted() {
    console.log("加载视频ID:", this.videoId);
  },
  beforeUnmount() {
    if (this.animationFrame) {
      cancelAnimationFrame(this.animationFrame);
    }
  },
};
</script>

<style scoped lang="less">
.page {
  max-width: 1500px;
}

.video-player-container {
  position: relative;
  background-color: #000;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 20px;
  z-index: 1;
}

.video-player {
  width: 100%;
  aspect-ratio: 16/9;
  position: relative;
}

.video-player video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: relative;
  z-index: 1;
}

.video-player::before {
  content: "";
  display: block;
  padding-top: 56.25%;
}

.video-player video,
.video-player img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.recommended-thumbnail {
  width: 140px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.video-duration {
  font-size: 11px;
  padding: 0px 3px;
  border-radius: 2px;
}

.recommended-video-title {
  font-size: 13px;
  line-height: 1.3;
  margin-bottom: 4px;
}

.recommended-video-info {
  width: calc(100% - 150px);
}

.video-stats {
  font-size: 13px;
  margin-bottom: 12px;
}

.video-title {
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 8px;
}

.recommended-video-item {
  margin-bottom: 12px;
}

/* 视频互动区域 */
.video-actions {
  margin-top: 20px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}
</style>
