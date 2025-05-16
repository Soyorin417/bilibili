<template>
  <div class="video-actions d-flex align-items-center">
    <div class="action-item me-4" @click="isLiked ? cancelLike() : handleLike()">
      <thumbs-up
        v-if="!isLiked"
        theme="filled"
        size="24"
        fill="#666666"
        class="me-1"
        style="cursor: pointer"
      />
      <thumbs-up
        v-else
        theme="filled"
        size="24"
        fill="#00aeec"
        class="me-1"
        style="cursor: pointer"
      />
      <span :class="{ 'text-primary': isLiked }">{{ likeCount }}</span>
    </div>

    <div
      class="action-item me-4"
      @click="isCollected ? cancelCollect() : handleCollect()"
    >
      <star
        v-if="!isCollected"
        theme="filled"
        size="24"
        fill="#666666"
        class="me-1"
        style="cursor: pointer"
      />
      <star
        v-else
        theme="filled"
        size="24"
        fill="#00aeec"
        class="me-1"
        style="cursor: pointer"
      />
      <span :class="{ 'text-primary': isCollected }">{{ collectCount }}</span>
    </div>

    <div class="action-item me-4" @click="handleCoin">
      <bitcoin
        v-if="!isCoined"
        theme="filled"
        size="24"
        fill="#666666"
        class="me-1"
        style="cursor: pointer"
      />
      <bitcoin
        v-else
        theme="filled"
        size="24"
        fill="#00aeec"
        class="me-1"
        style="cursor: pointer"
      />
      <span :class="{ 'text-primary': isCoined }">{{ coinCount }}</span>
    </div>

    <div class="action-item" @click="handleShare">
      <share-two
        theme="filled"
        size="24"
        :fill="isShared ? '#00aeec' : '#666666'"
        class="me-1"
        style="cursor: pointer"
      />
      <span :class="{ 'text-primary': isShared }">{{ shareCount }}</span>
    </div>
  </div>
</template>

<script>
import { ThumbsUp, Star, Bitcoin, ShareTwo } from "@icon-park/vue-next";
import { mapGetters } from "vuex";
import axios from "axios";

export default {
  name: "VideoActions",
  components: {
    ThumbsUp,
    Star,
    Bitcoin,
    ShareTwo,
  },
  props: {
    videoId: {
      type: [String, Number],
      required: true,
    },
    initialLikeCount: {
      type: Number,
      default: 0,
    },
    initialCollectCount: {
      type: Number,
      default: 0,
    },
    initialCoinCount: {
      type: Number,
      default: 0,
    },
    initialShareCount: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      likeCount: this.initialLikeCount,
      collectCount: this.initialCollectCount,
      coinCount: this.initialCoinCount,
      shareCount: this.initialShareCount,
      isLiked: false,
      isCollected: false,
      isCoined: false,
      isShared: false,
      isProcessing: false,
      requestCounters: {
        like: 0,
        collect: 0,
        coin: 0,
        share: 0,
      },
    };
  },
  async created() {
    // 获取视频互动状态
    const actionStatus = await this.$parent.getVideoActionStatus(this.videoId);
    if (actionStatus) {
      // 使用数据库中的状态
      this.isLiked = actionStatus.isLike === true;
      this.isCollected = actionStatus.isCollect === true;
      this.isCoined = actionStatus.isCoined === true;
      this.isShared = actionStatus.isShare === true;
    }
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  methods: {
    async fetchVideoInfo() {
      try {
        const response = await axios.get(`http://127.0.0.1:8081/video/getVideoById`, {
          params: {
            id: this.videoId,
          },
        });

        if (response.data) {
          const videoInfo = response.data;
          this.likeCount = videoInfo.likeCount || 0;
          this.collectCount = videoInfo.collectCount || 0;
          this.coinCount = videoInfo.coinCount || 0;
          this.shareCount = videoInfo.shareCount || 0;
          this.$forceUpdate();
        }
      } catch (error) {
        console.error("获取视频信息失败:", error);
      }
    },

    async checkActionStatus() {
      if (!this.userInfo || !this.userInfo.id) return;

      try {
        const token = localStorage.getItem("token");
        if (!token) return;

        // 从video_action_info表获取用户操作状态
        const response = await axios.get(
          `http://127.0.0.1:8081/video/action/status/${this.videoId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              userUid: this.userInfo.id,
            },
          }
        );

        if (response.data) {
          const actionInfo = response.data;
          // 根据VideoActionInfo的字段设置状态
          this.isLiked = actionInfo.isLike === true;
          this.isCollected = actionInfo.isCollect === true;
          this.isCoined = actionInfo.isCoined === true;
          this.isShared = actionInfo.isShare === true;
          this.$forceUpdate();
        }
      } catch (error) {
        console.error("检查操作状态失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      }
    },

    async handleLike() {
      try {
        const token = localStorage.getItem("token");
        console.log("=== 点赞操作 ===");
        console.log("当前状态:", {
          isLiked: this.isLiked,
          likeCount: this.likeCount,
        });
        const response = await axios.post(
          `http://127.0.0.1:8081/video/action/like/${this.videoId}?userUid=${this.userInfo.id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("点赞响应:", response.data);
        if (response.data === "点赞成功") {
          this.isLiked = true;
          this.likeCount += 1;
          console.log("点赞后状态:", {
            isLiked: this.isLiked,
            likeCount: this.likeCount,
          });
        }
      } catch (error) {
        console.error("点赞操作失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      }
    },

    async cancelLike() {
      if (!this.userInfo || !this.userInfo.id) {
        console.log("取消点赞失败: 用户未登录");
        return;
      }

      const token = localStorage.getItem("token");
      if (!token) {
        console.log("取消点赞失败: Token不存在");
        return;
      }

      try {
        console.log("=== 取消点赞操作 ===");
        console.log("当前状态:", {
          isLiked: this.isLiked,
          likeCount: this.likeCount,
        });
        const response = await axios.delete(
          `http://127.0.0.1:8081/video/action/like/${this.videoId}?userUid=${this.userInfo.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("取消点赞响应:", response.data);
        if (response.data === "取消点赞成功") {
          this.isLiked = false;
          this.likeCount = Math.max(0, this.likeCount - 1);
          console.log("取消点赞后状态:", {
            isLiked: this.isLiked,
            likeCount: this.likeCount,
          });
        }
      } catch (error) {
        console.error("取消点赞失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      }
    },

    async handleCollect() {
      if (this.isProcessing) {
        console.log("操作正在处理中，请稍候...");
        return;
      }

      try {
        this.isProcessing = true;
        this.requestCounters.collect++;
        console.log(`=== 收藏操作 (第${this.requestCounters.collect}次请求) ===`);
        const token = localStorage.getItem("token");
        console.log("操作前状态:", {
          isCollected: this.isCollected,
          collectCount: this.collectCount,
        });
        const response = await axios.post(
          `http://127.0.0.1:8081/video/action/collect/${this.videoId}?userUid=${this.userInfo.id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("收藏响应:", response.data);
        if (response.data === "收藏成功") {
          this.isCollected = true;
          this.collectCount += 1;
          console.log("收藏后状态:", {
            isCollected: this.isCollected,
            collectCount: this.collectCount,
          });
        }
      } catch (error) {
        console.error("收藏操作失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      } finally {
        this.isProcessing = false;
      }
    },

    async cancelCollect() {
      if (!this.userInfo || !this.userInfo.id) {
        console.log("取消收藏失败: 用户未登录");
        return;
      }

      const token = localStorage.getItem("token");
      if (!token) {
        console.log("取消收藏失败: Token不存在");
        return;
      }

      try {
        console.log("=== 取消收藏操作 ===");
        console.log("当前状态:", {
          isCollected: this.isCollected,
          collectCount: this.collectCount,
        });
        const response = await axios.delete(
          `http://127.0.0.1:8081/video/action/collect/${this.videoId}?userUid=${this.userInfo.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("取消收藏响应:", response.data);
        if (response.data === "取消收藏成功") {
          this.isCollected = false;
          this.collectCount = Math.max(0, this.collectCount - 1);
          console.log("取消收藏后状态:", {
            isCollected: this.isCollected,
            collectCount: this.collectCount,
          });
        }
      } catch (error) {
        console.error("取消收藏失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      }
    },

    async handleCoin() {
      if (this.isProcessing) {
        console.log("操作正在处理中，请稍候...");
        return;
      }

      try {
        this.isProcessing = true;
        this.requestCounters.coin++;
        console.log(`=== 投币操作 (第${this.requestCounters.coin}次请求) ===`);
        const token = localStorage.getItem("token");
        console.log("操作前状态:", {
          isCoined: this.isCoined,
          coinCount: this.coinCount,
        });
        const response = await axios.post(
          `http://127.0.0.1:8081/video/action/coin/${this.videoId}?userUid=${this.userInfo.id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("投币响应:", response.data);
        if (response.data === "投币成功") {
          this.isCoined = true;
          this.coinCount += 1;
          console.log("投币后状态:", {
            isCoined: this.isCoined,
            coinCount: this.coinCount,
          });
        }
      } catch (error) {
        console.error("投币操作失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      } finally {
        this.isProcessing = false;
      }
    },

    async handleShare() {
      try {
        const token = localStorage.getItem("token");
        console.log("=== 分享操作 ===");
        console.log("当前状态:", {
          isShared: this.isShared,
          shareCount: this.shareCount,
        });
        const response = await axios.post(
          `http://127.0.0.1:8081/video/action/share/${this.videoId}?userUid=${this.userInfo.id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("分享响应:", response.data);
        if (response.data === "分享成功") {
          this.isShared = true;
          this.shareCount += 1;
          console.log("分享后状态:", {
            isShared: this.isShared,
            shareCount: this.shareCount,
          });
        }
      } catch (error) {
        console.error("分享操作失败:", error);
        if (error.response) {
          console.error("错误详情:", {
            status: error.response.status,
            data: error.response.data,
            headers: error.response.headers,
          });
        }
      }
    },
  },
  mounted() {
    this.fetchVideoInfo();
    this.checkActionStatus();
  },
};
</script>

<style scoped>
.video-actions {
  margin: 20px 0;
}

.action-item {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.action-item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.text-primary {
  color: #00aeec;
}

.text-muted {
  color: #666;
  font-size: 12px;
  cursor: pointer;
}

.text-muted:hover {
  color: #00aeec;
}
</style>
