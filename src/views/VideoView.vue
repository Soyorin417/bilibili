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
              <span class="me-3 stats-text">{{ videoInfo.view_count }}</span>
              <span class="me-3 stats-text">{{ videoInfo.time }}</span>
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
                <div class="d-flex align-items-center me-4">
                  <thumbs-up
                    @click="videoLike"
                    v-if="!videoInfo.is_like"
                    theme="filled"
                    size="32"
                    fill="#61666d"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />
                  <thumbs-up
                    @click="videoDislike"
                    v-else
                    theme="filled"
                    size="32"
                    fill="#00aeec"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />
                  {{ videoInfo.like_count }}
                </div>

                <div class="d-flex align-items-center me-4">
                  <svg
                    @click="videoCoin"
                    v-if="!videoInfo.is_coin"
                    width="28"
                    height="28"
                    viewBox="0 0 28 28"
                    xmlns="http://www.w3.org/2000/svg"
                    class="video-coin-icon video-toolbar-item-icon"
                    data-v-b72e4a72=""
                    style="color: #61666d"
                  >
                    <path
                      fill-rule="evenodd"
                      clip-rule="evenodd"
                      d="M14.045 25.5454C7.69377 25.5454 2.54504 20.3967 2.54504 14.0454C2.54504 7.69413 7.69377 2.54541 14.045 2.54541C20.3963 2.54541 25.545 7.69413 25.545 14.0454C25.545 17.0954 24.3334 20.0205 22.1768 22.1771C20.0201 24.3338 17.095 25.5454 14.045 25.5454ZM9.66202 6.81624H18.2761C18.825 6.81624 19.27 7.22183 19.27 7.72216C19.27 8.22248 18.825 8.62807 18.2761 8.62807H14.95V10.2903C17.989 10.4444 20.3766 12.9487 20.3855 15.9916V17.1995C20.3854 17.6997 19.9799 18.1052 19.4796 18.1052C18.9793 18.1052 18.5738 17.6997 18.5737 17.1995V15.9916C18.5667 13.9478 16.9882 12.2535 14.95 12.1022V20.5574C14.95 21.0577 14.5444 21.4633 14.0441 21.4633C13.5437 21.4633 13.1382 21.0577 13.1382 20.5574V12.1022C11.1 12.2535 9.52148 13.9478 9.51448 15.9916V17.1995C9.5144 17.6997 9.10883 18.1052 8.60856 18.1052C8.1083 18.1052 7.70273 17.6997 7.70265 17.1995V15.9916C7.71158 12.9487 10.0992 10.4444 13.1382 10.2903V8.62807H9.66202C9.11309 8.62807 8.66809 8.22248 8.66809 7.72216C8.66809 7.22183 9.11309 6.81624 9.66202 6.81624Z"
                      fill="currentColor"
                    ></path>
                  </svg>
                  <svg
                    @click="videoDiscoin"
                    v-else
                    width="28"
                    height="28"
                    viewBox="0 0 28 28"
                    xmlns="http://www.w3.org/2000/svg"
                    class="video-coin-icon video-toolbar-item-icon"
                    data-v-b72e4a72=""
                    style="color: #00aeec"
                  >
                    <path
                      fill-rule="evenodd"
                      clip-rule="evenodd"
                      d="M14.045 25.5454C7.69377 25.5454 2.54504 20.3967 2.54504 14.0454C2.54504 7.69413 7.69377 2.54541 14.045 2.54541C20.3963 2.54541 25.545 7.69413 25.545 14.0454C25.545 17.0954 24.3334 20.0205 22.1768 22.1771C20.0201 24.3338 17.095 25.5454 14.045 25.5454ZM9.66202 6.81624H18.2761C18.825 6.81624 19.27 7.22183 19.27 7.72216C19.27 8.22248 18.825 8.62807 18.2761 8.62807H14.95V10.2903C17.989 10.4444 20.3766 12.9487 20.3855 15.9916V17.1995C20.3854 17.6997 19.9799 18.1052 19.4796 18.1052C18.9793 18.1052 18.5738 17.6997 18.5737 17.1995V15.9916C18.5667 13.9478 16.9882 12.2535 14.95 12.1022V20.5574C14.95 21.0577 14.5444 21.4633 14.0441 21.4633C13.5437 21.4633 13.1382 21.0577 13.1382 20.5574V12.1022C11.1 12.2535 9.52148 13.9478 9.51448 15.9916V17.1995C9.5144 17.6997 9.10883 18.1052 8.60856 18.1052C8.1083 18.1052 7.70273 17.6997 7.70265 17.1995V15.9916C7.71158 12.9487 10.0992 10.4444 13.1382 10.2903V8.62807H9.66202C9.11309 8.62807 8.66809 8.22248 8.66809 7.72216C8.66809 7.22183 9.11309 6.81624 9.66202 6.81624Z"
                      fill="currentColor"
                    ></path>
                  </svg>
                  {{ videoInfo.coin_count }}
                </div>
                <div class="d-flex align-items-center me-4">
                  <star
                    @click="videoCollect"
                    v-if="!videoInfo.is_collect"
                    theme="filled"
                    size="32"
                    fill="#61666d"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />
                  <star
                    @click="videoDiscollect"
                    v-else
                    theme="filled"
                    size="32"
                    fill="#00aeec"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />
                  {{ videoInfo.collect_count }}
                </div>
                <div class="d-flex align-items-center me-4">
                  <share-two
                    @click="videoShare"
                    v-if="!videoInfo.is_share"
                    theme="filled"
                    size="32"
                    fill="#61666d"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />
                  <share-two
                    @click="videoDisshare"
                    v-else
                    theme="filled"
                    size="32"
                    fill="#00aeec"
                    :strokeWidth="3"
                    strokeLinecap="square"
                  />{{ videoInfo.share_count }}
                </div>
              </div>
            </div>

            <!--视频简介-->
            <div class="video-info mt-3">
              <p class="mt-2">{{ videoInfo.introduction }}</p>
            </div>

            <div class="divider"></div>

            <!-- 评论列表 -->
            <div class="comments-section mt-4">
              <h5 class="mb-3">评论 ({{ commentData.length }})</h5>
              <CommentInput @send-comment="handleNewComment" />
              <CommentList :comments="commentData" @like="handleLike" />
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
import {
  PlayTwo,
  CommentOne,
  Forbid,
  ThumbsUp,
  Star,
  ShareTwo,
} from "@icon-park/vue-next";
import { danmakuPool } from "@/data/danmakuPool";
import { userInfo } from "@/data/userInfoData";
import { videoInfos } from "@/data/videoInfos";
import { commentData } from "@/data/commentData";
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";
import DanmakuList from "@/components/video/DanmakuList.vue";
import DanmakuControl from "@/components/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/video/DanmakuDisplay.vue";
import CommentInput from "@/components/video/CommentInput.vue";
import CommentList from "@/components/video/CommentList.vue";
import axios from "axios";
import userData from "@/data/userData";

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
    ThumbsUp,
    Star,
    ShareTwo,
    CommentInput,
    CommentList,
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
      commentData,
      recommendedVideos: [],
      activeDanmaku: [],
      lastCheckTime: 0,
      checkInterval: 50,
      videoInfo: {},
      userData,
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
    handleNewComment(newComment) {
      this.commentData.unshift(newComment);
    },
    handleLike(item) {
      item.isLiked = !item.isLiked;
      if (item.isLiked) {
        item.likeCount++;
      } else {
        item.likeCount--;
      }
    },
    videoLike() {
      this.videoInfo.is_like = !this.videoInfo.is_like;
      this.videoInfo.like_count++;
    },
    videoDislike() {
      this.videoInfo.is_like = !this.videoInfo.is_like;
      this.videoInfo.like_count--;
    },
    videoCollect() {
      this.videoInfo.is_collect = !this.videoInfo.is_collect;
      this.videoInfo.collect_count++;
    },
    videoDiscollect() {
      this.videoInfo.is_collect = !this.videoInfo.is_collect;
      this.videoInfo.collect_count--;
    },
    videoShare() {
      this.videoInfo.is_share = !this.videoInfo.is_share;
      this.videoInfo.share_count++;
    },
    videoDisshare() {
      this.videoInfo.is_share = !this.videoInfo.is_share;
      this.videoInfo.share_count--;
    },
    videoCoin() {
      this.videoInfo.is_coin = true;
      this.videoInfo.coin_count++;
    },
    videoDiscoin() {
      this.videoInfo.is_coin = false;
      this.videoInfo.coin_count--;
    },
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
      } else {
        console.error("videoInfos 不是有效的数组或为空");
      }
    },

    async selectDanmakuList() {
      let danmakuId = this.videoInfo.id;

      // 根据视频ID加载对应的弹幕数据
      const danmakuInfo = danmakuPool.find((d) => d.id === danmakuId);

      if (danmakuInfo) {
        try {
          // 使用axios获取远程数据

          const response = await axios.get(danmakuInfo.url, {
            transformResponse: [
              (data) => {
                // 不自动转换响应数据
                return data;
              },
            ],
          });

          let data;
          try {
            // 尝试解析JSON
            if (typeof response.data === "string") {
              data = JSON.parse(response.data);
            } else {
              data = response.data;
            }
          } catch (parseError) {
            // 如果是JavaScript文件，尝试提取变量
            const text = response.data;
            if (typeof text === "string" && text.includes("danmakuList")) {
              // 尝试提取danmakuList的值
              const match = text.match(/danmakuList\s*=\s*(\[[\s\S]*?\]);/);
              if (match && match[1]) {
                try {
                  data = { danmakuList: JSON.parse(match[1]) };
                } catch (e) {
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
        } catch (error) {
          this.danmakuList = [];
        }
      } else {
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
    handleClickOutside(event) {
      // 如果点击的不是输入框，则隐藏发送按钮
      if (!event.target.closest(".form-floating")) {
        this.showButton = false;
      }
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
      this.videoInfos = [];
    }
  },
  mounted() {
    this.loadVideoData(this.videoId);
    this.selectDanmakuList();
    // 添加全局点击事件监听器
    document.addEventListener("click", this.handleClickOutside);
  },
  beforeUnmount() {
    this.activeDanmaku = [];
    // 移除全局点击事件监听器
    document.removeEventListener("click", this.handleClickOutside);
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

.video-coin-icon {
  cursor: pointer;
  transition: color 0.3s ease;
}

.divider {
  height: 1px;
  background-color: #e3e5e7;
  margin: 20px 0;
}

.comments-section {
  .comment-item {
    .comment-content {
      flex: 1;
    }
  }
  .replies-section {
    margin-left: 48px;
    .reply-item {
      .reply-content {
        flex: 1;
      }
    }
  }
}
</style>
