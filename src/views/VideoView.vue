<template>
  <div>
    <VideoBar></VideoBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="row">
          <!-- 左侧区域：视频播放器和视频信息 -->
          <div class="col-lg-9 mt-4">
            <div class="row mb-3">
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
            <div class="video-player-container" id="videoStatus">
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
                <div class="danmaku-display-area" v-show="isDanmakuVisible">
                  <div
                    v-for="(item, index) in activeDanmaku"
                    :key="index"
                    :style="item.style"
                    :class="[
                      'danmaku-item',
                      item.type === 'scroll' ? 'danmaku-running' : 'danmaku-fixed',
                    ]"
                  >
                    {{ item.content }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 弹幕控制条 -->
            <div class="danmaku-control-bar mt-2">
              <input
                v-model="danmakuText"
                class="danmaku-input me-2"
                placeholder="发个友善的弹幕吧~"
                @keyup.enter="sendDanmaku"
              />

              <!-- 弹幕类型选择 -->
              <div class="danmaku-type-selector me-2">
                <select v-model="currentDanmakuType" class="form-select form-select-sm">
                  <option value="scroll">滚动</option>
                  <option value="fixed">固定</option>
                </select>
              </div>

              <button class="btn btn-sm btn-primary" @click="sendDanmaku">发送</button>
              <div class="form-check form-switch ms-2">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="showDanmaku"
                  id="danmakuSwitch"
                />
                <label class="form-check-label" for="danmakuSwitch">弹幕</label>
              </div>
            </div>

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
            <div class="danmaku-container mb-3" :class="{ expanded: is_show }">
              <div
                class="danmaku-header d-flex justify-content-between align-items-center p-1"
              >
                <div class="d-flex align-items-center">
                  <h6 class="mb-0 ms-1">弹幕列表</h6>
                </div>
                <div>
                  <small class="text-secondary me-1" @click="is_show = !is_show">
                    <drop-down-list theme="outline" size="18" fill="#000000" />
                  </small>
                </div>
              </div>
              <div v-if="is_show" class="danmaku-list">
                <table class="table table-sm table-hover borderless">
                  <thead class="thead-light">
                    <tr class="header-row">
                      <th scope="col" width="10%">时间</th>
                      <th scope="col" width="70%">弹幕内容</th>
                      <th scope="col" width="20%">发送时间</th>
                    </tr>
                  </thead>
                  <tbody class="small">
                    <tr v-for="(item, index) in danmakuList" :key="index">
                      <td>{{ item.time }}</td>
                      <td class="text-truncate" style="max-width: 160px">
                        {{ item.content }}
                      </td>
                      <td>{{ item.sendTime }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div v-if="is_show" class="danmaku-footer text-center">
                <small
                  ><a href="#" class="text-decoration-none text-secondary"
                    >查看历史弹幕</a
                  ></small
                >
              </div>
            </div>

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
import { PlayTwo, CommentOne, Forbid, DropDownList } from "@icon-park/vue-next";
import { danmakuList } from "@/data/danmakuData";
import { userInfo } from "@/data/userInfoData";
import { videoInfo } from "@/data/videoData";
import { recommendedVideos } from "@/data/recommendedVideoData";
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";

export default {
  components: {
    VideoBar,
    PlayTwo,
    CommentOne,
    Forbid,
    DropDownList,
    RecommendedVideos,
    AuthorInfo,
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
      showDanmaku: true, // 用户控制的弹幕开关
      danmakuEnabled: true, // 视频状态控制的弹幕显示
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

      this.currentTime = this.$refs.videoPlayer.currentTime;

      // 查找当前时间点附近的弹幕
      this.checkDanmakuTiming();
      console.log("当前时间：", this.currentTime);
    },
    // 检查是否有弹幕需要显示
    checkDanmakuTiming() {
      if (!this.isDanmakuVisible) return;

      const currentTimeInt = Math.floor(this.currentTime);
      console.log("当前视频时间：", currentTimeInt);

      this.danmakuList.forEach((danmaku) => {
        const danmakuTime = this.timeToSeconds(danmaku.time);
        console.log("检查弹幕时间：", danmakuTime, "弹幕内容：", danmaku.content);

        if (Math.abs(danmakuTime - currentTimeInt) < 0.3) {
          console.log("触发弹幕显示：", danmaku);
          this.showNewDanmaku(danmaku.content, danmaku.type);
        }
      });
    },

    checkVideoStatus() {
      const video = this.$refs.videoPlayer;
      if (video) {
        // 当视频在播放时隐藏弹幕，暂停时显示弹幕
        this.danmakuEnabled = !(video.currentTime > 0 && !video.paused);
      }
    },

    // 显示新弹幕
    showNewDanmaku(content, type = "fixed") {
      console.log("开始创建新弹幕：", { content, type });

      const trackPosition = Math.floor(Math.random() * 80) + 10;
      console.log("弹幕位置：", trackPosition);

      let style = {};
      if (type === "scroll") {
        style = {
          top: `${trackPosition}%`,
          color: this.getRandomColor(),
          right: "-100%",
          transform: "translateX(0)",
          whiteSpace: "nowrap",
        };
      } else {
        style = {
          top: `${trackPosition}%`,
          color: this.getRandomColor(),
          left: "50%",
          transform: "translateX(-50%)",
          animationName: "danmakuFade",
          animationDuration: "5s",
        };
      }
      console.log("弹幕样式：", style);

      const newDanmaku = {
        content,
        style,
        type,
        key: Date.now(),
      };
      console.log("新弹幕：", newDanmaku);

      this.activeDanmaku.push(newDanmaku);
      console.log("当前活跃弹幕列表：", this.activeDanmaku);

      setTimeout(
        () => {
          const index = this.activeDanmaku.findIndex((d) => d.key === newDanmaku.key);
          if (index !== -1) {
            console.log("移除弹幕：", newDanmaku);
            this.activeDanmaku.splice(index, 1);
            console.log("移除后的弹幕列表：", this.activeDanmaku);
          }
        },
        type === "scroll" ? 14000 : 5000
      );
    },
    // 获取随机颜色
    getRandomColor() {
      const colors = [
        "#ffffff", // 白色
        "#ff9999", // 浅红色
        "#99ff99", // 浅绿色
        "#9999ff", // 浅蓝色
        "#ffff99", // 浅黄色
      ];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    // 发送弹幕
    sendDanmaku() {
      if (!this.danmakuText.trim()) return;
      console.log("发送弹幕：", this.danmakuText);
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
        content: this.danmakuText,
        sendTime: `${month}-${day} ${hours}:${mins}`,
        type: this.currentDanmakuType,
      };

      // 添加到弹幕列表
      this.danmakuList.unshift(newDanmaku);

      // 显示新发送的弹幕
      this.showNewDanmaku(this.danmakuText);

      // 清空输入框
      this.danmakuText = "";
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
};
</script>

<style scoped>
.page {
  max-width: 1500px;
}

.video-player-container {
  position: relative;
  background-color: #000;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 20px;
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

/* 弹幕显示区域 */
.danmaku-display-area {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  z-index: 9999;
}

.danmaku-item {
  position: absolute;
  white-space: nowrap;
  font-size: 20px;
  font-weight: 500;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
  will-change: transform;
  user-select: none;
}

/* 滚动弹幕动画 */
@keyframes running {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-3000px);
  }
}

/* 固定弹幕动画 */
@keyframes danmakuFade {
  0% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.8);
  }
  20% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  80% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  100% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(1.2);
  }
}

.danmaku-running {
  animation: running 12s linear forwards;
  position: absolute;
  right: -100%;
}

.danmaku-fixed {
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  animation: danmakuFade 5s forwards;
}

/* 弹幕控制条 */
.danmaku-control-bar {
  display: flex;
  align-items: center;
  padding: 8px;
  background-color: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 20px;
}

.danmaku-input {
  flex: 1;
  height: 30px;
  border-radius: 15px;
  border: 1px solid #ddd;
  padding: 0 15px;
  font-size: 13px;
  color: #333;
  background-color: #fff;
}

.danmaku-type-selector select {
  height: 30px;
  border-radius: 4px;
  font-size: 12px;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
}

.form-check-label {
  color: white;
  font-size: 13px;
}

/* 弹幕列表样式 */
.danmaku-container {
  border: 1px solid #eee;
  border-radius: 3px;
  overflow: hidden;
  background-color: #fff;
  font-size: 10px;
  height: auto;
  min-height: 30px;
}

.danmaku-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
  padding: 2px 4px !important;
  min-height: 28px;
  display: flex;
  align-items: center;
}

.danmaku-header h6 {
  font-size: 13px; /* 缩小标题字体 */
  color: #333;
  margin-bottom: 0;
}

.danmaku-list {
  height: 700px; /* 固定高度   */
  overflow-y: auto;
  max-height: none;
}

.danmaku-list table {
  margin-bottom: 0;
  border: none;
}

.danmaku-list th {
  font-size: 12px; /* 缩小表头字体 */
  font-weight: 600;
  color: #555;
  padding: 1px 3px; /* 减小padding */
  border: none;
  background-color: #f8f9fa;
}

.danmaku-list td {
  font-size: 12px; /* 缩小单元格字体 */
  padding: 1px 3px; /* 减小padding */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border: none;
  line-height: 1.2; /* 减小行高 */
}

.danmaku-footer {
  background-color: #f8f9fa;
  border-top: 1px solid #eee;
  font-size: 9px; /* 缩小底部字体 */
  padding: 1px !important; /* 减小padding */
}

.borderless th,
.borderless td,
.borderless thead th,
.borderless tbody + tbody {
  border: none !important; /* 确保移除所有边框 */
}

.table-hover > tbody > tr:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.header-row {
  border: none !important; /* 移除表头边框 */
  background-color: #f8f9fa;
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

.author-info {
  background-color: white;
  border-radius: 6px;
  margin-bottom: 5px;
}

.author-avatar img {
  width: 46px;
  height: 46px;
  border: 1px solid #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.author-details h6 {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 0;
}

.author-details .text-muted {
  font-size: 15px;
}

.author-details .btn-sm {
  font-size: 12px;
  margin-bottom: 0.5rem;
}
.author-details .el-icon {
  font-size: 15px;
  margin-left: 4px;
  color: #666;
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

/* 当列表显示时，容器才应用固定高度 */
.danmaku-container.expanded {
  height: 634px; /* 列表高度 + header/footer 空间 */
}
</style>
