<template>
  <div :key="$route.params.id">
    <VideoBar></VideoBar>
    <div class="container-fluid mt-2">
      <div class="page mx-auto">
        <div class="row">
          <!--视频简介-->
          <div class="col-lg-9 mt-2 text-start">
            <VideoStats
              :title="videoInfo.title"
              :formatted-views="formattedViews"
              :formatted-comments="formattedComments"
              :formatted-time="formattedTime"
              :show-right="videoInfo.show_right"
            />

            <!-- 视频播放器 -->
            <div class="mt-4" id="videoStatus">
              <DanmakuDisplay
                :url="videoInfo.videoUrl"
                :videoId="videoId"
                :isVisible="isDanmakuVisible"
                :currentTime="currentTime"
                ref="danmakuDisplay"
                @play="handlePlay"
                @update:currentTime="updateCurrentTime"
              />
            </div>

            <!-- 弹幕控制条 -->
            <DanmakuControl
              :video-id="videoId"
              :current-time="currentTime"
              @send-danmaku="handleSendDanmaku"
              @toggle-danmaku="handleToggleDanmaku"
            />

            <!-- 视频互动区域 -->
            <VideoActions
              :video-id="videoId"
              :initial-like-count="videoInfo.likeCount"
              :initial-collect-count="videoInfo.collectCount"
              :initial-coin-count="videoInfo.coinCount"
              :initial-share-count="videoInfo.share_count"
            />

            <!--视频简介-->
            <div class="video-info mt-3">
              <p class="mt-2">{{ videoInfo.introduction }}</p>
            </div>

            <!-- 视频标签 -->
            <div class="video-tags mt-3">
              <div class="tags-wrapper">
                <span v-for="(tag, index) in tags" :key="index" class="tag-item">
                  {{ tag }}
                </span>
              </div>
            </div>

            <div class="divider">
              <div class="divider-line"></div>
            </div>

            <!-- 评论列表 -->
            <div class="comments-section mt-4">
              <h5 class="mb-3">评论 ({{ commentData.length }})</h5>
              <CommentList
                ref="commentList"
                :video-id="videoId"
                :user-info="userInfo"
                @like="handleLike"
                @comment-deleted="getCommentList"
                @send-reply="handleSendReply"
                @comment-added="handleCommentAdded"
              />
            </div>
          </div>

          <!-- 右侧区域：推荐视频 -->
          <div class="col-lg-3">
            <!-- 作者信息区域 -->
            <AuthorInfo
              :videoInfo="videoInfo"
              :userInfo="userInfo"
              @toggle-follow="toggleFollow"
            />

            <!-- 弹幕列表 -->
            <DanmakuList :videoId="videoId" />
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
import { mapGetters } from "vuex";
import RecommendedVideos from "@/components/content/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/content/video/AuthorInfo.vue";
import DanmakuControl from "@/components/content/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/content/video/DanmakuDisplay.vue";
import CommentList from "@/components/content/video/CommentList.vue";
import DanmakuList from "@/components/content/video/DanmakuList.vue";
import VideoActions from "@/components/content/video/VideoActions.vue";
import VideoStats from "@/components/content/video/VideoStats.vue";
import { videoApi } from "@/api/content/video";
import { parseDanmakuXml } from "@/utils/danmakuParser";
import commentApi from "@/api/message/comment";
import websocketClient from "@/utils/websocket";

export default {
  components: {
    VideoBar,
    RecommendedVideos,
    AuthorInfo,
    DanmakuControl,
    DanmakuDisplay,
    CommentList,
    DanmakuList,
    VideoActions,
    VideoStats,
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
      videoInfos: [],
      commentData: [],
      recommendedVideos: [],
      activeDanmaku: [],
      lastCheckTime: 0,
      checkInterval: 50,
      videoInfo: {},
      tags: [],
      isDanmakuVisible: true,
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
          this.getDanmakuList(to.params.id);
        }
      },
    },
  },
  methods: {
    // 获取视频列表
    async getVideoInfos() {
      if (!this.userInfo || !this.userInfo.id) {
        console.error("用户信息不完整");
        return;
      }

      this.isLoading = true;

      try {
        const response = await videoApi.getAllVideos();
        this.videoInfos = response.data;
        console.log("获取视频列表", this.videoInfos);

        // 获取每个视频的互动状态
        await Promise.all(
          this.videoInfos.map(async (video) => {
            try {
              const actionResponse = await videoApi.getVideoActionStatus(
                video.id,
                this.userInfo.id
              );
              if (actionResponse.data) {
                video.isLiked = actionResponse.data.isLiked;
                video.isCollected = actionResponse.data.isCollected;
                video.isCoined = actionResponse.data.isCoined;
                video.isShared = actionResponse.data.isShared;
              }
            } catch (error) {
              console.error(`获取视频 ${video.id} 的互动状态失败:`, error);
            }
          })
        );
      } catch (error) {
        console.error("获取视频列表失败:", error);
        alert("获取视频信息失败，请稍后再试");
      } finally {
        this.isLoading = false;
      }
    },

    // 获取视频标签
    async getVideoTags(videoId) {
      try {
        const tagsResponse = await videoApi.getVideoTags(videoId);
        if (tagsResponse.data && Array.isArray(tagsResponse.data)) {
          const tags = tagsResponse.data.map((tag) => tag.name);
          this.tags = tags;
          return tags;
        } else {
          console.warn("Tags data is not in expected format:", tagsResponse.data);
          return [];
        }
      } catch (error) {
        console.error("Error fetching tags:", error);
        return [];
      }
    },

    // 获取视频详情
    async getVideoById(videoId) {
      try {
        const response = await videoApi.getVideoById(videoId);
        if (response.data) {
          this.videoInfo = { ...response.data };
        }
      } catch (error) {
        console.error("Error fetching video info:", error);
      }
    },

    // 获取弹幕列表
    async getDanmakuList(videoId) {
      if (!videoId) {
        console.error("无效的 videoId:", videoId);
        return;
      }

      try {
        const response = await videoApi.getDanmakuList(videoId);
        if (response.data && response.data.url) {
          // 更新弹幕列表
          console.log("获取到的弹幕列表:", response.data.url);
          const xmlResponse = await fetch(response.data.url);
          const xmlText = await xmlResponse.text();
          this.danmakuList = parseDanmakuXml(xmlText);
          console.log("解析后的弹幕列表:", this.danmakuList);
        }
      } catch (error) {
        console.error("Error fetching danmaku list:", error);
      }
    },

    // 获取评论列表
    async getCommentList() {
      try {
        const response = await videoApi.getCommentList(this.videoId);
        if (response.data && Array.isArray(response.data)) {
          this.commentData = response.data.map((comment) => ({
            ...comment,
            showReplies: false,
            replyContent: "",
            replies: [],
            replyCount: comment.replyCount || 0,
            isLiked: comment.isLiked || false,
            likeCount: comment.likeCount || 0,
          }));
        } else {
          console.error("Invalid comment data format:", response.data);
          this.commentData = [];
        }
      } catch (error) {
        console.error("Error fetching comment list:", error);
        this.commentData = [];
      }
    },

    // 发送评论
    async handleNewComment(newComment) {
      try {
        if (!this.userInfo || !this.userInfo.id) {
          console.error("用户信息不完整:", this.userInfo);
          return;
        }

        const commentData = {
          videoId: parseInt(this.videoId),
          content: newComment.content,
          userUid: this.userInfo.id,
          createTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString(),
          likeCount: 0,
          replyCount: 0,
        };

        const response = await videoApi.addComment(commentData);

        if (response.data === "评论发布成功") {
          await this.getCommentList();
        }
      } catch (error) {
        console.error("=== 评论发送错误详情 ===", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
          config: {
            url: error.config?.url,
            method: error.config?.method,
            headers: error.config?.headers,
            data: error.config?.data,
          },
        });
        alert("评论发送失败，请稍后重试");
      }
    },

    // 加载视频数据
    async loadVideoData(id) {
      try {
        // 首先获取所有视频用于推荐
        await this.getVideoInfos();

        // Then fetch the specific video data
        const videoResponse = await videoApi.getVideoById(id);

        if (videoResponse.data) {
          this.videoInfo = videoResponse.data;

          // Fetch user action status if user is logged in
          if (this.userInfo && this.userInfo.id) {
            const actionResponse = await videoApi.getVideoActionStatus(
              id,
              this.userInfo.id
            );

            if (actionResponse.data) {
              this.videoInfo.isLiked = actionResponse.data.isLiked;
              this.videoInfo.isCollected = actionResponse.data.isCollected;
              this.videoInfo.isCoined = actionResponse.data.isCoined;
              this.videoInfo.isShared = actionResponse.data.isShared;
            }
          }

          // Update recommended videos
          await this.refreshRecommendedVideos();

          // Get video tags
          await this.getVideoTags(id);

          // Get comments
          await this.getCommentList();
        }
      } catch (error) {
        console.error(`获取视频 ${id} 信息失败:`, error);
      }
    },

    // 获取视频互动状态
    async getVideoActionStatus(videoId) {
      if (!this.userInfo || !this.userInfo.id) return null;

      try {
        const response = await videoApi.getVideoActionStatus(videoId, this.userInfo.id);
        return response.data;
      } catch (error) {
        console.error(`获取视频 ${videoId} 的互动状态失败:`, error);
        return null;
      }
    },

    // 关注/取消关注
    async toggleFollow() {
      if (!this.userInfo || !this.userInfo.id) {
        alert("请先登录");
        return;
      }

      try {
        const params = {
          followerUid: this.userInfo.id,
          followingUid: this.videoInfo.authorId,
        };

        const response = this.videoInfo.isFollow
          ? await videoApi.unfollowUser(params.followerUid, params.followingUid)
          : await videoApi.followUser(params.followerUid, params.followingUid);

        if (response.data) {
          this.videoInfo.isFollow = !this.videoInfo.isFollow;
          // Update the follow count
          this.videoInfo.follow = this.videoInfo.isFollow
            ? (parseInt(this.videoInfo.follow) + 1).toString()
            : (parseInt(this.videoInfo.follow) - 1).toString();
        }
      } catch (error) {
        console.error("关注/取消关注失败:", error);
        alert("操作失败，请稍后重试");
      }
    },

    // 初始化数据
    async initData() {
      try {
        // 先获取视频基本信息
        await this.getVideoById(this.videoId);

        // 确保 videoInfo 有数据后再获取其他信息
        if (this.videoInfo && Object.keys(this.videoInfo).length > 0) {
          await this.getVideoTags(this.videoId);
          await this.getDanmakuList(this.videoId);
          await this.getCommentList();
        }
      } catch (error) {
        console.error("初始化数据失败:", error);
      }
    },

    // 格式化数字
    formatNumber(num) {
      if (!num) return "0";
      const n = parseFloat(num);
      if (isNaN(n)) return "0";

      if (n >= 100000000) {
        return (n / 100000000).toFixed(1) + "亿";
      } else if (n >= 10000) {
        return (n / 10000).toFixed(1) + "万";
      }
      return n.toString();
    },

    // 处理视频播放
    handlePlay() {
      if (this.hasReportedPlay) return;
      this.hasReportedPlay = true;
      console.log("上报播放量");
      try {
        videoApi.reportPlay(this.videoId);
      } catch (e) {
        console.error("上报播放量失败", e);
      }
    },

    // 更新视频时间
    updateCurrentTime(time) {
      this.currentTime = time;
    },

    // 刷新推荐视频
    async refreshRecommendedVideos() {
      try {
        const response = await videoApi.getRecommendVideos();
        console.log("接口返回完整数据:", response);

        if (response.status === 200) {
          this.recommendedVideos = response.data.map((video) => ({
            id: video.id,
            title: video.title,
            views: video.views,
            comments: video.comments,
            time: video.time,
            description: video.description,
            videoUrl: video.videoUrl,
            image: video.image,
            authorId: video.authorId,
            duration: video.duration,
            likeCount: video.likeCount,
            collectCount: video.collectCount,
            coinCount: video.coinCount,
            shareCount: video.shareCount,
            status: video.status,
            avatar: video.avatar,
            follow: video.follow,
            authorFans: video.authorFans,
            authorName: video.authorName,
          }));
          console.log("推荐视频列表:", this.recommendedVideos);
        } else {
          console.error("获取推荐视频失败:", response.message);
          // 使用备用方案
          this.useFallbackRecommendation();
        }
      } catch (error) {
        console.error("获取推荐视频失败:", error);
        // 使用备用方案
        this.useFallbackRecommendation();
      }
    },

    useFallbackRecommendation() {
      const currentId = parseInt(this.videoId);
      this.recommendedVideos = this.videoInfos.filter(
        (video) => parseInt(video.id) !== currentId
      );
    },

    // 处理视频点击
    handleVideoClick(videoId) {
      this.$router.push({
        name: "VideoView",
        params: { id: videoId },
      });
    },

    // 处理弹幕发送
    handleSendDanmaku(danmaku) {
      console.log("收到弹幕数据:", danmaku);
      // 将弹幕数据传递给显示组件
      if (this.$refs.danmakuDisplay) {
        this.$refs.danmakuDisplay.addNewDanmaku(danmaku);
        console.log("弹幕已传递给显示组件");
      } else {
        console.error("弹幕显示组件未找到");
      }
    },

    // 处理弹幕显示
    handleToggleDanmaku(isVisible) {
      this.isDanmakuVisible = isVisible;
    },

    // 处理弹幕类型切换
    handleDanmakuTypeChange(type) {
      this.currentDanmakuType = type;
    },

    // 处理弹幕位置切换
    handleDanmakuPositionChange(position) {
      this.currentDanmakuPosition = position;
    },

    // 处理弹幕显示
    handleDanmakuDisplay(display) {
      this.isDanmakuVisible = display;
    },

    // 处理弹幕控制
    handleDanmakuControl(control) {
      this.isDanmakuVisible = control;
    },

    // 处理评论添加后的逻辑
    handleCommentAdded() {
      // 处理评论添加后的逻辑
      console.log("新评论已添加");
    },

    // 初始化WebSocket连接
    initWebSocket() {
      // 确保WebSocket连接已建立
      if (!websocketClient.ws || websocketClient.ws.readyState !== WebSocket.OPEN) {
        websocketClient.connect();
      }
      // 添加消息处理器
      websocketClient.addMessageHandler(this.handleWebSocketMessage);
    },

    // 处理WebSocket消息
    handleWebSocketMessage(event) {
      try {
        const rawMessage = event.data;
        console.log("收到原始WebSocket消息:", rawMessage);

        // 处理系统消息
        if (typeof rawMessage === "string" && !rawMessage.startsWith("[用户]")) {
          console.log("系统消息:", rawMessage);
          return;
        }

        // 处理用户消息
        if (rawMessage.startsWith("[用户]")) {
          const jsonStartIndex = rawMessage.indexOf("：") + 1;
          if (jsonStartIndex > 0) {
            const jsonStr = rawMessage.substring(jsonStartIndex);
            const data = JSON.parse(jsonStr);
            console.log("解析后的消息数据:", data);

            if (data.type === "reply") {
              // 通过ref调用子组件的方法处理新回复
              this.$refs.commentList.handleNewReply(data);
            } else if (data.type === "comment") {
              // 刷新评论列表
              this.getCommentList();
            }
          }
        }
      } catch (error) {
        console.error("WebSocket消息处理失败:", error);
        console.log("原始消息:", event.data);
      }
    },

    // 处理回复发送
    async handleSendReply(reply) {
      try {
        const response = await commentApi.addReply(reply);
        if (response.data === "回复添加成功") {
          // 确保WebSocket连接已建立
          if (!websocketClient.ws || websocketClient.ws.readyState !== WebSocket.OPEN) {
            websocketClient.connect();
          }
          // 通过WebSocket发送回复
          websocketClient.send({
            type: "reply",
            ...reply,
          });
        }
      } catch (error) {
        console.error("回复发送失败:", error);
        alert("回复发送失败，请稍后重试");
      }
    },

    // 处理评论点赞
    async handleLike(commentId) {
      try {
        const response = await commentApi.likeComment(commentId);
        if (response.code === 200) {
          // 更新评论列表
          await this.getCommentList();
        }
      } catch (error) {
        console.error("点赞失败:", error);
      }
    },
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
    videoId() {
      return this.$route.params.id;
    },
    formattedTime() {
      if (!this.videoInfo.time) return "";
      const date = new Date(this.videoInfo.time);
      return date
        .toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        })
        .replace(/\//g, "-");
    },
    formattedViews() {
      return this.formatNumber(this.videoInfo.views);
    },
    formattedComments() {
      return this.formatNumber(this.videoInfo.comments);
    },
  },
  created() {
    if (!Array.isArray(this.videoInfos)) {
      this.videoInfos = [];
    }
  },
  mounted() {
    // 使用 async/await 确保数据加载完成
    this.initData();
    // 初始化WebSocket连接
    this.initWebSocket();
  },
  beforeUnmount() {
    this.activeDanmaku = [];
    document.removeEventListener("click", this.handleClickOutside);
    // 移除WebSocket消息处理器
    websocketClient.removeMessageHandler(this.handleWebSocketMessage);
  },
};
</script>

<style scoped>
.page {
  max-width: 1600px;
}

.divider {
  height: 1px;
  background-color: #e3e5e7;
  margin: 20px 0;
}

.comments-section .comment-item .comment-content {
  flex: 1;
}
.comments-section .replies-section {
  margin-left: 48px;
}
.comments-section .replies-section .reply-item .reply-content {
  flex: 1;
}
.video-tags .tags-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.video-tags .tag-item {
  background-color: #f6f7f8;
  color: #61666d;
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.video-tags .tag-item:hover {
  background-color: #e3e5e7;
  color: #00aeec;
}
</style>
