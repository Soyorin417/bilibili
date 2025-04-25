<template>
  <div :key="$route.params.id">
    <VideoBar></VideoBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="row">
          <div class="col-lg-9 mt-4 text-start">
            <h4 class="title-wrapper">
              {{ videoInfo.title }}
            </h4>
            <div class="stats-wrapper d-flex align-items-center">
              <play-two
                theme="outline"
                size="16"
                fill="#666666"
                :strokeWidth="2"
                class="me-1 icon-fix"
              />
              <span class="me-3 stats-text">1891.4万</span>
              <comment-one
                theme="outline"
                size="16"
                fill="#666666"
                :strokeWidth="2"
                class="me-1 icon-fix"
              />
              <span class="me-3 stats-text">12.3万</span>
              <span class="me-3 stats-text">2024-03-10</span>
              <span v-if="!videoInfo.show_right" class="ms-2">
                <forbid theme="outline" size="16" fill="#fc0404" class="me-1 icon-fix" />
                <span class="copyright-text">未经作者授权，禁止转载</span>
              </span>
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
              @video-click="handleVideoClick"
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
import { danmakuPool } from "@/data/danmakuPool";
import { userInfo } from "@/data/userInfoData";
import { videoInfos } from "@/data/videoInfos";
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";
import DanmakuList from "@/components/video/DanmakuList.vue";
import DanmakuControl from "@/components/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/video/DanmakuDisplay.vue";
import axios from "axios";

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
      currentTime: 0,
      currentDanmakuType: "scroll",
      danmakuList: [],
      danmakuPool,
      userInfo,
      videoInfos,
      recommendedVideos: [],
      activeDanmaku: [],
      lastCheckTime: 0,
      checkInterval: 50,
      videoInfo: {},
    };
  },
  watch: {
    $route: {
      immediate: true,
      handler(to) {
        if (to.params.id) {
          // 重置数据
          this.danmakuList = [];
          this.activeDanmaku = [];
          if (this.$refs.videoPlayer) {
            this.$refs.videoPlayer.currentTime = 0;
            this.$refs.videoPlayer.pause();
          }
          // 加载新数据
          this.loadVideoData(to.params.id);
          this.selectDanmakuList();
        }
      },
    },
  },
  methods: {
    loadVideoData(id) {
      // 首先从 videoInfos 中查找
      let video = this.videoInfos.find((v) => v.id === parseInt(id));

      if (video) {
        this.videoInfo = {
          ...video,
          id: parseInt(id),
        };

        // 重置视频播放器状态
        if (this.$refs.videoPlayer) {
          this.$refs.videoPlayer.currentTime = 0;
          this.$refs.videoPlayer.pause();
        }

        // 清空当前弹幕
        this.activeDanmaku = [];

        // 更新推荐视频列表
        this.refreshRecommendedVideos();
      } else {
        console.error(`Video with id ${id} not found`);
      }
    },

    handleVideoClick(videoId) {
      // 使用 replace 而不是 push，这样可以替换当前历史记录
      this.$router.replace({
        name: "VideoView",
        params: { id: videoId },
      });
    },

    refreshRecommendedVideos() {
      const currentId = this.videoInfo.id;
      // 确保 videoInfos 是数组且有数据
      if (Array.isArray(this.videoInfos) && this.videoInfos.length > 0) {
        this.recommendedVideos = this.videoInfos.filter(
          (video) => video.id !== currentId
        );
        console.log("推荐视频数量:", this.recommendedVideos.length);
      } else {
        console.error("videoInfos 不是有效的数组或为空");
      }
    },

    async selectDanmakuList() {
      console.log("selectDanmakuList called");
      console.log("videoInfo:", this.videoInfo);
      let danmakuId = this.videoInfo.id;
      console.log("danmakuId:", danmakuId);

      // 根据视频ID加载对应的弹幕数据
      const danmakuInfo = danmakuPool.find((d) => d.id === danmakuId);
      console.log("Found danmaku info:", danmakuInfo);

      if (danmakuInfo) {
        try {
          // 使用axios获取远程数据
          console.log("Fetching from URL:", danmakuInfo.url);
          const response = await axios.get(danmakuInfo.url, {
            transformResponse: [
              (data) => {
                // 不自动转换响应数据
                return data;
              },
            ],
          });
          console.log("Raw response data:", response.data);

          let data;
          try {
            // 尝试解析JSON
            if (typeof response.data === "string") {
              data = JSON.parse(response.data);
            } else {
              data = response.data;
            }
            console.log("Parsed data:", data);
          } catch (parseError) {
            console.error("Error parsing JSON:", parseError);
            // 如果是JavaScript文件，尝试提取变量
            const text = response.data;
            if (typeof text === "string" && text.includes("danmakuList")) {
              // 尝试提取danmakuList的值
              const match = text.match(/danmakuList\s*=\s*(\[[\s\S]*?\]);/);
              if (match && match[1]) {
                try {
                  data = { danmakuList: JSON.parse(match[1]) };
                  console.log("Extracted data from JS:", data);
                } catch (e) {
                  console.error("Error parsing extracted data:", e);
                  throw new Error("Could not parse danmaku data");
                }
              }
            }
          }

          // 检查data是否为数组，如果不是但data.danmakuList是数组则使用它
          if (Array.isArray(data)) {
            this.danmakuList = data;
          } else if (data && Array.isArray(data.danmakuList)) {
            this.danmakuList = data.danmakuList;
          } else {
            throw new Error(
              "Invalid danmaku data format: expected an array or object with danmakuList array"
            );
          }

          console.log(
            "Successfully loaded danmaku list:",
            this.danmakuList.length,
            "items"
          );
        } catch (error) {
          console.error("加载弹幕数据失败:", error);
          console.error("Error details:", {
            message: error.message,
            stack: error.stack,
            response: error.response,
            rawData: error.response?.data,
          });
          this.danmakuList = [];
        }
      } else {
        console.warn(`未找到ID为 ${danmakuId} 的弹幕数据`);
        this.danmakuList = [];
      }
    },

    is_follow() {
      this.userInfo.is_follow = !this.userInfo.is_follow;
    },
    timeToSeconds(timeStr) {
      const parts = timeStr.split(":");
      return parseInt(parts[0]) * 60 + parseInt(parts[1]);
    },
    onTimeUpdate() {
      if (!this.$refs.videoPlayer) return;

      const video = this.$refs.videoPlayer;
      this.currentTime = video.currentTime;

      if (!video.paused && this.isDanmakuVisible) {
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
        this.danmakuEnabled = !video.ended;
      }
    },
    checkDanmakuTiming() {
      const currentTime = this.$refs.videoPlayer.currentTime;
      const targetTime = Math.floor(currentTime * 10) / 10;

      this.danmakuList.forEach((d) => {
        const danmakuTime = this.timeToSeconds(d.time);
        if (Math.abs(danmakuTime - targetTime) < 0.3) {
          this.showNewDanmaku(d.content, d.type || "scroll");
        }
      });
    },
    showNewDanmaku(content, type = "scroll") {
      const danmaku = {
        content,
        type,
        timestamp: Date.now(),
      };

      this.activeDanmaku = [...this.activeDanmaku, danmaku];

      if (this.activeDanmaku.length > 100) {
        this.activeDanmaku = this.activeDanmaku.slice(-100);
      }
    },
    sendDanmaku({ content, type }) {
      if (!content.trim()) return;

      const minutes = Math.floor(this.currentTime / 60);
      const seconds = Math.floor(this.currentTime % 60);
      const timeStr = `${minutes
        .toString()
        .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;

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

      this.danmakuList.unshift(newDanmaku);
      this.showNewDanmaku(content, type);
    },
    toggleDanmaku(value) {
      this.showDanmaku = value;
    },
  },
  computed: {
    videoId() {
      return this.id;
    },
    isDanmakuVisible() {
      return this.showDanmaku && this.danmakuEnabled;
    },
  },
  created() {
    // 确保 videoInfos 正确初始化
    if (!Array.isArray(this.videoInfos)) {
      console.error("videoInfos 初始化失败");
      this.videoInfos = [];
    }
    console.log("初始化 videoInfos 长度:", this.videoInfos.length);
  },
  mounted() {
    console.log("VideoView mounted");
    this.loadVideoData(this.videoId);
    this.selectDanmakuList();
  },
  beforeUnmount() {
    this.activeDanmaku = [];
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

.left-align {
  text-align: left !important;
}

.stats-text {
  font-size: 13px;
  color: #666666;
}

.copyright-text {
  font-size: 12px;
  color: #666666;
}

.title-wrapper {
  margin: 0;
  padding: 0;
  line-height: 1.5;
  font-size: 24px;
  font-weight: 600;
  text-align: left;
  /* 添加以下属性来优化中文显示 */
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue",
    Arial, "Noto Sans", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
  letter-spacing: 0.2px;
}

.stats-wrapper {
  margin-top: 12px;
  margin-bottom: 12px;
  line-height: 1;
}

.icon-fix {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  vertical-align: middle;
}
h4 {
  font-family: "PingFang SC", "Microsoft YaHei", "Helvetica", "Arial", sans-serif;
  font-size: 20px; /* 保持适中的大小 */
  font-weight: 600; /* 加点粗体 */
  line-height: 1.4; /* 行高正常一点 */
  letter-spacing: 0.5px; /* 字距微微拉开，中英文更自然 */
}
</style>
