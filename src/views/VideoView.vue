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
                ref="danmakuDisplay"
                @play="handlePlay"
              />
            </div>

            <!-- 弹幕控制条 -->
            <DanmakuControl
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
              <CommentInput @send-comment="handleNewComment" />
              <CommentList
                :comments="commentData"
                @like="handleLike"
                @comment-deleted="getCommentList"
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
            <DanmakuList :videoId="videoId" :danmakuUrl="danmakuList.url || ''" />
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
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";
import DanmakuControl from "@/components/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/video/DanmakuDisplay.vue";
import CommentInput from "@/components/video/CommentInput.vue";
import CommentList from "@/components/video/CommentList.vue";
import axios from "axios";
import DanmakuList from "@/components/video/DanmakuList.vue";
import VideoActions from "@/components/video/VideoActions.vue";
import VideoStats from "@/components/video/VideoStats.vue";

export default {
  components: {
    VideoBar,
    RecommendedVideos,
    AuthorInfo,
    DanmakuControl,
    DanmakuDisplay,
    CommentInput,
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
    async getVideoInfos() {
      const url = "http://127.0.0.1:8081/video/getAllVideo";
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      this.isLoading = true;

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // 处理成功响应
        this.videoInfos = response.data;
        console.log("获取视频列表", this.videoInfos);

        // 获取每个视频的互动状态
        if (this.userInfo && this.userInfo.id) {
          await Promise.all(
            this.videoInfos.map(async (video) => {
              try {
                const actionResponse = await axios.get(
                  `http://127.0.0.1:8081/video/action/status/${video.id}?userUid=${this.userInfo.id}`,
                  {
                    headers: {
                      Authorization: `Bearer ${token}`,
                    },
                  }
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
        }
      } catch (error) {
        console.error("获取视频列表失败:", error);
        alert("获取视频信息失败，请稍后再试");
      } finally {
        this.isLoading = false;
      }
    },

    async getVideoTags(videoId) {
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("Token is missing");
        return [];
      }

      try {
        const tagsResponse = await axios.get(
          `http://127.0.0.1:8081/api/tags/video/${videoId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

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
    async getVideoById(videoId) {
      const url = `http://127.0.0.1:8081/video/getVideoById?id=${videoId}`;
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          this.videoInfo = { ...response.data };
        }
      } catch (error) {
        console.error("Error fetching video info:", error);
      }
    },
    async getDanmakuList(videoId) {
      if (!videoId) {
        console.error("无效的 videoId:", videoId);
        return;
      }

      const url = `http://127.0.0.1:8081/danmaku/getDanmakuList?id=${videoId}`;
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          this.danmakuList = response.data;
        }
      } catch (error) {
        console.error("Error fetching danmaku list:", error);
      }
    },
    async getCommentList() {
      const url = `http://127.0.0.1:8081/comments/list?videoId=${this.videoId}`;
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data && Array.isArray(response.data)) {
          // 处理每条评论的数据
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

    async handleNewComment(newComment) {
      try {
        const url = `http://127.0.0.1:8081/comments/add`;
        const token = localStorage.getItem("token");

        if (!token) {
          console.error("Token is missing");
          return;
        }

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

        const response = await axios.post(url, commentData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

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
    handleLike(item) {
      item.isLiked = !item.isLiked;
      if (item.isLiked) {
        item.likeCount++;
      } else {
        item.likeCount--;
      }
    },

    async loadVideoData(id) {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token is missing");
          return;
        }

        // First get all videos for recommendations
        await this.getVideoInfos();

        // Then fetch the specific video data
        const videoResponse = await axios.get(
          `http://127.0.0.1:8081/video/getVideoById?id=${id}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        if (videoResponse.data) {
          this.videoInfo = videoResponse.data;

          // Fetch user action status if user is logged in
          if (this.userInfo && this.userInfo.id) {
            const actionResponse = await axios.get(
              `http://127.0.0.1:8081/video/action/status/${id}?userUid=${this.userInfo.id}`,
              {
                headers: { Authorization: `Bearer ${token}` },
              }
            );

            if (actionResponse.data) {
              this.videoInfo.isLiked = actionResponse.data.isLiked;
              this.videoInfo.isCollected = actionResponse.data.isCollected;
              this.videoInfo.isCoined = actionResponse.data.isCoined;
              this.videoInfo.isShared = actionResponse.data.isShared;
            }
          }

          // Update recommended videos
          this.refreshRecommendedVideos();

          // Get video tags
          await this.getVideoTags(id);

          // Get comments
          await this.getCommentList();
        }
      } catch (error) {
        console.error(`获取视频 ${id} 信息失败:`, error);
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
      if (Array.isArray(this.videoInfos) && this.videoInfos.length > 0) {
        this.recommendedVideos = this.videoInfos.filter(
          (video) => video.id !== currentId
        );
      } else {
        console.error("videoInfos 不是有效的数组或为空");
      }
    },

    is_follow() {
      if (this.userInfo) {
        this.userInfo.is_follow = !this.userInfo.is_follow;
      }
    },

    handleSendDanmaku(danmakuData) {
      if (this.$refs.danmakuDisplay) {
        this.$refs.danmakuDisplay.addNewDanmaku(danmakuData);
      }
    },
    handleToggleDanmaku(value) {
      this.isDanmakuVisible = value;
    },

    // 添加获取视频互动状态的方法
    async getVideoActionStatus(videoId) {
      if (!this.userInfo || !this.userInfo.id) return null;

      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          `http://127.0.0.1:8081/video/action/status/${videoId}?userUid=${this.userInfo.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        return response.data;
      } catch (error) {
        console.error(`获取视频 ${videoId} 的互动状态失败:`, error);
        return null;
      }
    },

    // Add the missing toggleFollow method
    async toggleFollow() {
      if (!this.userInfo || !this.userInfo.id) {
        alert("请先登录");
        return;
      }

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token is missing");
          return;
        }

        const params = {
          followerUid: this.userInfo.id,
          followingUid: this.videoInfo.authorId,
        };

        const response = await axios.post(
          `http://127.0.0.1:8081/api/follow/${
            this.videoInfo.isFollow ? "unfollow" : "follow"
          }`,
          null,
          {
            headers: { Authorization: `Bearer ${token}` },
            params,
          }
        );

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

    async handlePlay() {
      if (this.hasReportedPlay) return;
      this.hasReportedPlay = true;
      console.log("上报播放量");
      try {
        await axios.post(`http://127.0.0.1:8081/video/play/${this.videoId}`);
      } catch (e) {
        console.error("上报播放量失败", e);
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
  },
  beforeUnmount() {
    this.activeDanmaku = [];

    document.removeEventListener("click", this.handleClickOutside);
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
