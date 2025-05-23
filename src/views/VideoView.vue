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
            <div class="mt-4" id="videoStatus">
              <DanmakuDisplay
                :url="videoInfo.videoUrl"
                :videoId="videoId"
                :isVisible="isDanmakuVisible"
                ref="danmakuDisplay"
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
              @toggle-follow="is_follow"
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
import { PlayTwo, CommentOne, Forbid } from "@icon-park/vue-next";
import { mapGetters } from "vuex";
import RecommendedVideos from "@/components/video/RecommendedVideos.vue";
import AuthorInfo from "@/components/video/AuthorInfo.vue";
import DanmakuControl from "@/components/video/DanmakuControl.vue";
import DanmakuDisplay from "@/components/video/DanmakuDisplay.vue";
import CommentInput from "@/components/video/CommentInput.vue";
import CommentList from "@/components/video/CommentList.vue";
import axios from "axios";
import userData from "@/data/userData";
import DanmakuList from "@/components/video/DanmakuList.vue";
import VideoActions from "@/components/video/VideoActions.vue";

export default {
  components: {
    VideoBar,
    PlayTwo,
    CommentOne,
    Forbid,
    RecommendedVideos,
    AuthorInfo,
    DanmakuControl,
    DanmakuDisplay,
    CommentInput,
    CommentList,
    DanmakuList,
    VideoActions,
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
      userData,
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
                  // 更新视频的互动状态
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

      this.isLoading = true;

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          // 获取视频基本信息
          this.videoInfo = {
            ...response.data,
          };
        } else {
          console.error("No video data received.");
        }
      } catch (error) {
        console.error("Error fetching video info:", error);
        alert("获取视频信息失败，请稍后再试");
      } finally {
        this.isLoading = false;
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

        console.log("=== 评论发送调试信息 ===");
        console.log("1. 用户信息:", {
          userInfo: this.userInfo,
          uid: this.userInfo?.id,
          username: this.userInfo?.username,
        });
        console.log("2. 评论内容:", newComment);
        console.log("3. 视频ID:", this.videoId);
        console.log("4. Token状态:", token ? "存在" : "不存在");

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

        console.log(
          "5. 准备发送的评论数据:",
          commentData,
          "时间",
          new Date(Date.now() + 8 * 60 * 60 * 1000)
        );

        const response = await axios.post(url, commentData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        console.log("6. 服务器响应:", {
          status: response.status,
          data: response.data,
          headers: response.headers,
        });

        if (response.data === "评论发布成功") {
          console.log("7. 评论发布成功，开始刷新评论列表");
          await this.getCommentList();
          console.log("8. 评论列表刷新完成");
        } else {
          console.error("评论发布失败，响应数据:", response.data);
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
    videoLike() {
      this.videoInfo.is_like = !this.videoInfo.is_like;
      this.videoInfo.likeCount++;
    },
    videoDislike() {
      this.videoInfo.is_like = !this.videoInfo.is_like;
      this.videoInfo.likeCount--;
    },
    videoCollect() {
      this.videoInfo.is_collect = !this.videoInfo.is_collect;
      this.videoInfo.collectCount++;
    },
    videoDiscollect() {
      this.videoInfo.is_collect = !this.videoInfo.is_collect;
      this.videoInfo.collectCount--;
    },
    videoShare() {
      this.videoInfo.is_share = !this.videoInfo.is_share;
      this.videoInfo.shareCount++;
    },
    videoDisshare() {
      this.videoInfo.is_share = !this.videoInfo.is_share;
      this.videoInfo.shareCount--;
    },
    videoCoin() {
      this.videoInfo.is_coin = true;
      this.videoInfo.coinCount++;
    },
    videoDiscoin() {
      this.videoInfo.is_coin = false;
      this.videoInfo.coinCount--;
    },
    async loadVideoData(id) {
      await this.getVideoInfos();

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
        // 如果本地没有找到视频，直接从服务器获取
        try {
          const token = localStorage.getItem("token");
          const [videoResponse, actionResponse] = await Promise.all([
            axios.get(`http://127.0.0.1:8081/video/getVideoById?id=${id}`, {
              headers: { Authorization: `Bearer ${token}` },
            }),
            this.userInfo && this.userInfo.id
              ? axios.get(
                  `http://127.0.0.1:8081/video/action/status/${id}?userUid=${this.userInfo.id}`,
                  {
                    headers: { Authorization: `Bearer ${token}` },
                  }
                )
              : Promise.resolve({ data: null }),
          ]);

          if (videoResponse.data) {
            const videoData = {
              ...videoResponse.data,
              id: parseInt(id),
            };

            // 如果有互动状态数据，更新状态
            if (actionResponse.data) {
              videoData.isLiked = actionResponse.data.isLiked;
              videoData.isCollected = actionResponse.data.isCollected;
              videoData.isCoined = actionResponse.data.isCoined;
              videoData.isShared = actionResponse.data.isShared;
            }

            this.videoInfo = videoData;
            this.refreshRecommendedVideos();
          }
        } catch (error) {
          console.error(`获取视频 ${id} 信息失败:`, error);
        }
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
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
    videoId() {
      return this.$route.params.id;
    },
  },
  created() {
    if (!Array.isArray(this.videoInfos)) {
      this.videoInfos = [];
    }
  },
  mounted() {
    this.getVideoTags(this.videoId);
    this.loadVideoData(this.videoId);
    this.getDanmakuList(this.videoId);
    this.getVideoById(this.videoId);
    this.getCommentList();

    document.addEventListener("click", this.handleClickOutside);
  },
  beforeUnmount() {
    this.activeDanmaku = [];

    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<style scoped lang="less">
.page {
  max-width: 1500px;
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
  font-size: 20px;
  font-weight: 600;
  line-height: 1.4;
  letter-spacing: 0.5px;
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

.video-tags {
  .tags-wrapper {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .tag-item {
    background-color: #f6f7f8;
    color: #61666d;
    padding: 4px 12px;
    border-radius: 4px;
    font-size: 13px;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      background-color: #e3e5e7;
      color: #00aeec;
    }
  }
}
</style>
