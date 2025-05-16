<template>
  <div class="comment-list" @click="handleOutsideClick">
    <div v-for="comment in comments" :key="comment.commentId" class="comment-item mb-4">
      <div class="d-flex">
        <div class="user-avatar me-3">
          <img
            :src="comment.avatar"
            class="rounded-circle"
            width="48"
            height="48"
            alt="user avatar"
          />
        </div>
        <div class="comment-content">
          <div class="d-flex align-items-center mb-1">
            <h6 class="mb-0 me-2">{{ comment.username }}</h6>
            <span class="badge bg-primary me-2" v-if="comment.isUp">UP主</span>
            <span class="badge bg-secondary">Lv.{{ comment.level }}</span>
          </div>
          <p class="mb-1">{{ comment.content }}</p>
          <div class="d-flex align-items-center text-muted">
            <small class="me-3">{{ formatDate(comment.createTime) }}</small>
            <div class="d-flex align-items-center me-3">
              <thumbs-up
                @click="handleLike(comment)"
                theme="filled"
                size="16"
                :fill="comment.isLiked ? '#00aeec' : '#666666'"
                class="me-1"
                style="cursor: pointer"
              /><small>{{ comment.likeCount }}</small>
              <thumbs-down
                @click="commentDislike(comment)"
                theme="filled"
                size="16"
                :fill="comment.isDisliked ? '#00aeec' : '#666666'"
                class="ms-2"
                style="cursor: pointer"
              />
              <small>{{ comment.dislikeCount || 0 }}</small>
            </div>
            <div class="d-flex align-items-center me-3">
              <comment-one
                @click.stop="toggleReplies(comment)"
                theme="filled"
                size="16"
                fill="#666666"
                class="me-1"
                style="cursor: pointer"
              />
              <small>{{ comment.replyCount }}</small>
              <small
                v-if="comment.showReplies"
                class="text-muted ms-2"
                style="cursor: pointer"
                @click.stop="toggleReplies(comment)"
              >
                收起
              </small>
            </div>
            <div
              v-if="userInfo && userInfo.id === comment.userUid"
              class="d-flex align-items-center"
            >
              <delete
                @click="handleDeleteComment(comment)"
                theme="filled"
                size="16"
                fill="#ff4d4f"
                class="me-1"
                style="cursor: pointer"
              />
              <small class="text-danger">删除</small>
            </div>
          </div>

          <!-- 回复列表 -->
          <div v-if="comment.showReplies" class="replies-section mt-3">
            <!-- 回复列表 -->
            <div
              v-for="reply in comment.replies"
              :key="reply.commentId"
              class="reply-item mb-3"
            >
              <div class="d-flex">
                <div class="user-avatar me-3">
                  <img
                    :src="reply.avatar"
                    class="rounded-circle"
                    width="32"
                    height="32"
                    alt="user avatar"
                  />
                </div>
                <div class="reply-content">
                  <div class="d-flex align-items-center mb-1">
                    <h6 class="mb-0 me-2">{{ reply.username }}</h6>
                    <span class="badge bg-primary me-2" v-if="reply.isUp">UP主</span>
                    <span class="badge bg-secondary">Lv.{{ reply.level }}</span>
                  </div>
                  <p class="mb-1">{{ reply.content }}</p>
                  <div class="d-flex align-items-center text-muted">
                    <small class="me-3">{{ formatDate(reply.createTime) }}</small>
                    <div class="d-flex align-items-center me-3">
                      <thumbs-up
                        @click="$emit('like', reply)"
                        theme="filled"
                        size="16"
                        :fill="reply.isLiked ? '#00aeec' : '#666666'"
                        class="me-1"
                      /><small>{{ reply.likeCount }}</small>
                      <thumbs-down
                        @click="commentDislike(reply)"
                        theme="filled"
                        size="16"
                        :fill="reply.isDisliked ? '#00aeec' : '#666666'"
                        class="ms-2"
                      />
                      <small>{{ reply.dislikeCount || 0 }}</small>
                    </div>
                    <!-- 添加编辑和删除按钮，只有回复作者可见 -->
                    <div
                      v-if="userInfo && userInfo.id === reply.userUid"
                      class="d-flex align-items-center"
                    >
                      <delete
                        @click="handleDeleteReply(reply)"
                        theme="filled"
                        size="16"
                        fill="#ff4d4f"
                        class="me-1"
                        style="cursor: pointer"
                      />
                      <small class="text-danger">删除</small>
                    </div>
                  </div>

                  <!-- 编辑回复的输入框 -->
                  <div v-if="reply.isEditing" class="edit-reply mt-2">
                    <div class="form-floating">
                      <input
                        v-model="reply.editContent"
                        type="text"
                        class="form-control"
                        :id="'editReplyInput-' + reply.id"
                        placeholder="编辑回复"
                      />
                      <label :for="'editReplyInput-' + reply.id">编辑回复</label>
                    </div>
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
                      <button
                        @click="handleUpdateReply(reply)"
                        class="btn btn-primary btn-sm me-2"
                        type="button"
                      >
                        保存
                      </button>
                      <button
                        @click="reply.isEditing = false"
                        class="btn btn-secondary btn-sm"
                        type="button"
                      >
                        取消
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 回复输入框 -->
            <div class="reply-input mt-3">
              <div class="form-floating">
                <input
                  v-model="comment.replyContent"
                  type="text"
                  class="form-control"
                  :id="'replyInput-' + comment.commentId"
                  placeholder="回复评论"
                />
                <label :for="'replyInput-' + comment.commentId">回复评论</label>
              </div>
              <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
                <button
                  @click="handleSendReply(comment)"
                  class="btn btn-primary btn-sm"
                  type="button"
                >
                  发送回复
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ThumbsUp, ThumbsDown, CommentOne, Delete } from "@icon-park/vue-next";
import { mapGetters } from "vuex";
import axios from "axios";
export default {
  name: "CommentList",
  components: {
    ThumbsUp,
    ThumbsDown,
    CommentOne,
    Delete,
  },

  props: {
    comments: {
      type: Array,
      required: true,
    },
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  data() {
    return {
      newComment: "",
      activeCommentId: null, // 添加当前展开的评论ID
    };
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return "";

      const date = new Date(dateString);
      const now = new Date();
      const diff = now - date;

      // 小于1分钟
      if (diff < 60000) {
        return "刚刚";
      }
      // 小于1小时
      if (diff < 3600000) {
        return `${Math.floor(diff / 60000)}分钟前`;
      }
      // 小于24小时
      if (diff < 86400000) {
        return `${Math.floor(diff / 3600000)}小时前`;
      }
      // 小于30天
      if (diff < 2592000000) {
        return `${Math.floor(diff / 86400000)}天前`;
      }

      // 超过30天显示具体日期
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const hour = String(date.getHours()).padStart(2, "0");
      const minute = String(date.getMinutes()).padStart(2, "0");

      return `${year}-${month}-${day} ${hour}:${minute}`;
    },
    handleOutsideClick(event) {
      // 检查点击的元素是否在回复区域内
      const isReplyElement =
        event.target.closest(".replies-section") ||
        event.target.closest(".comment-content") ||
        event.target.closest(".comment-input") ||
        event.target.closest(".comment-item");

      // 如果点击在回复区域外，则收起所有回复
      if (!isReplyElement) {
        this.comments.forEach((comment) => {
          if (comment.showReplies) {
            comment.showReplies = false;
          }
        });
      }
    },
    toggleReplies(comment) {
      // 如果点击的是当前展开的评论，则收起
      if (comment.showReplies) {
        comment.showReplies = false;
        this.activeCommentId = null;
        return;
      }

      // 收起其他已展开的评论
      this.comments.forEach((c) => {
        if (c !== comment && c.showReplies) {
          c.showReplies = false;
        }
      });

      // 展开当前评论
      comment.showReplies = true;
      this.activeCommentId = comment.id;

      if (!comment.replies || comment.replies.length === 0) {
        const token = localStorage.getItem("token");
        if (token) {
          axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        }

        // 检查点赞状态
        this.checkLikeStatus(comment);

        axios
          .get(`http://127.0.0.1:8081/reply/list?commentId=${comment.id}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          })
          .then((response) => {
            console.log("获取到的回复数据:", response.data);
            comment.replies = response.data || [];
          })
          .catch((error) => {
            console.error("获取回复失败:", error);
          });
      }
    },
    async handleSendReply(comment) {
      if (!comment.replyContent || !comment.replyContent.trim()) return;

      let newReply = null; // 声明在外部，使其在整个函数作用域内可用

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }

        if (!this.userInfo || !this.userInfo.id) {
          return;
        }

        // 创建新回复对象
        newReply = {
          content: comment.replyContent.trim(),
          userUid: this.userInfo.id,
          username: this.userInfo.username,
          avatar: this.userInfo.avatar,
          level: this.userInfo.level,
          isUp: this.userInfo.id === this.$route.params.id,
          createTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString(),
          likeCount: 0,
          isLiked: false,
          commentId: comment.id,
        };

        // 先添加到评论的回复列表中
        if (!comment.replies) {
          comment.replies = [];
        }
        comment.replies.unshift(newReply);
        comment.replyCount = (comment.replyCount || 0) + 1;

        const tempContent = comment.replyContent;
        comment.replyContent = "";

        const replyData = {
          commentId: comment.id,
          userUid: this.userInfo.id,
          content: tempContent.trim(),
          createTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString(),
        };

        const response = await axios.post("http://127.0.0.1:8081/reply/add", replyData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (response.data === "回复添加成功") {
          // 更新回复ID
          newReply.id = response.data.id;
        }
      } catch (error) {
        console.error("发送回复失败:", error);
        // 如果发送失败，从回复列表中移除
        if (comment.replies && newReply) {
          const index = comment.replies.findIndex((r) => r === newReply);
          if (index !== -1) {
            comment.replies.splice(index, 1);
            comment.replyCount = Math.max(0, (comment.replyCount || 0) - 1);
          }
        }
      }
    },

    toggleReplyReplies(reply) {
      reply.showReplies = !reply.showReplies;
      if (reply.showReplies && !reply.replyContent) {
        reply.replyContent = "";
      }
    },

    handleSendReplyToReply(comment, reply) {
      if (!reply.replyContent || !reply.replyContent.trim()) return;

      const token = localStorage.getItem("token");
      if (token) {
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      }

      const content = `@${reply.username} ${reply.replyContent.trim()}`;
      const newReplyData = {
        commentId: comment.id,
        userUid: this.userInfo.uid,
        content,
      };

      axios
        .post("http://127.0.0.1:8081/replies/add", newReplyData)
        .then((res) => {
          if (res.data === "回复发布成功") {
            // 重新获取回复列表
            this.toggleReplies(comment);
            reply.replyContent = "";
            reply.showReplies = false;
          } else {
            console.error("回复发布失败");
          }
        })
        .catch((err) => {
          console.error("回复回复失败", err);
        });
    },

    async handleDeleteComment(comment) {
      if (!confirm("确定要删除这条评论吗？")) {
        return;
      }

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }

        const response = await axios.delete(
          `http://127.0.0.1:8081/comments/delete/${comment.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.data === "评论删除成功") {
          this.$emit("comment-deleted");
        }
      } catch (error) {
        console.error("删除评论失败:", error);
      }
    },

    // 视频相关事件保持不变
    videoLike() {
      this.$emit("video-like");
    },
    videoDislike() {
      this.$emit("video-dislike");
    },
    videoCollect() {
      this.$emit("video-collect");
    },
    videoDiscollect() {
      this.$emit("video-discollect");
    },
    videoShare() {
      this.$emit("video-share");
    },
    videoDisshare() {
      this.$emit("video-disshare");
    },
    videoCoin() {
      this.$emit("video-coin");
    },
    videoDiscoin() {
      this.$emit("video-discoin");
    },
    commentDislike(comment) {
      if (typeof comment.isDisliked === "undefined") comment.isDisliked = false;
      if (typeof comment.dislikeCount === "undefined") comment.dislikeCount = 0;
      comment.isDisliked = !comment.isDisliked;
      comment.dislikeCount += comment.isDisliked ? 1 : -1;
    },
    async handleLike(comment) {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token is missing");
          return;
        }

        if (!this.userInfo || !this.userInfo.id) {
          console.error("用户信息不完整");
          return;
        }

        console.log("评论信息:", comment);
        console.log("用户信息:", this.userInfo);

        const params = {
          commentId: comment.id,
          userUid: this.userInfo.id,
        };

        const url = comment.isLiked
          ? `http://127.0.0.1:8081/comment-like/unlike`
          : `http://127.0.0.1:8081/comment-like/like`;

        console.log("点赞请求URL:", url);
        console.log("请求参数:", params);

        const response = await axios.post(url, null, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          params: params,
        });

        console.log("点赞响应:", response);

        if (response.data === "点赞成功" || response.data === "取消点赞成功") {
          // 更新点赞状态
          comment.isLiked = !comment.isLiked;
          // 更新点赞数
          comment.likeCount = comment.isLiked
            ? comment.likeCount + 1
            : comment.likeCount - 1;
        } else {
          console.log("点赞操作提示:", response.data);
          alert(response.data);
        }
      } catch (error) {
        // 处理业务逻辑错误（如已点赞）
        if (error.response?.status === 400) {
          console.log("点赞操作提示:", error.response.data);
          alert(error.response.data);
          return;
        }

        // 处理其他错误
        console.error("点赞操作失败:", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
          params: error.config?.params,
        });
        alert("操作失败，请稍后重试");
      }
    },

    // 检查评论是否已点赞
    async checkLikeStatus(comment) {
      try {
        const token = localStorage.getItem("token");
        if (!token || !this.userInfo || !this.userInfo.id) {
          return;
        }

        const params = {
          commentId: comment.id,
          userUid: this.userInfo.id,
        };

        const response = await axios.get(`http://127.0.0.1:8081/comment-like/is-liked`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          params: params,
        });

        // 确保 isLiked 是布尔值
        comment.isLiked = Boolean(response.data);
        console.log("评论点赞状态:", comment.id, comment.isLiked);
      } catch (error) {
        console.error("检查点赞状态失败:", error);
      }
    },

    async handleDeleteReply(reply) {
      if (!confirm("确定要删除这条回复吗？")) {
        return;
      }

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token is missing");
          return;
        }

        // 删除回复调试信息
        console.log("尝试删除回复:", reply);

        const response = await axios.delete(
          `http://127.0.0.1:8081/reply/delete/${reply.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data === "回复删除成功") {
          // 从回复列表中移除该回复
          const comment = this.comments.find((c) =>
            c.replies.some((r) => r.id === reply.id)
          );
          if (comment) {
            comment.replies = comment.replies.filter((r) => r.id !== reply.id);
            comment.replyCount--;
          }
        } else {
          console.error("回复删除失败:", response.data);
          alert(response.data);
        }
      } catch (error) {
        console.error("删除回复失败:", error);
        alert("删除回复失败，请稍后重试");
      }
    },

    async handleUpdateReply(reply) {
      if (!reply.editContent || !reply.editContent.trim()) return;

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error("Token is missing");
          return;
        }

        // 构建更新数据
        const updateData = {
          id: reply.id,
          commentId: reply.commentId,
          userUid: reply.userUid,
          content: reply.editContent.trim(),
          createTime: reply.createTime,
          username: this.userInfo.username, // 用户名
          avatar: this.userInfo.avatar,
          level: this.userInfo.level,
          isUp: this.userInfo.id === reply.authorId,
          likeCount: reply.likeCount,
          isLiked: reply.isLiked,
        };

        console.log("更新回复数据:", updateData);
        console.log("用户ID:", this.userInfo.id);
        console.log("作者ID:", reply.authorId);
        console.log("是否为UP主:", updateData.isUp);

        const response = await axios.put(
          `http://127.0.0.1:8081/reply/update/${reply.id}`,
          updateData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        console.log("更新响应:", response.data);

        if (response.data === "回复更新成功") {
          reply.content = reply.editContent.trim();
          reply.isEditing = false;
          reply.editContent = "";
        } else {
          console.error("回复更新失败:", response.data);
          alert(response.data);
        }
      } catch (error) {
        console.error("更新回复失败:", error);
        if (error.response) {
          console.error("错误响应:", error.response.data);
          console.error("错误状态:", error.response.status);
        } else {
          alert("更新回复失败，请稍后重试");
        }
      }
    },

    async handleSendComment() {
      if (!this.newComment || !this.newComment.trim()) {
        return;
      }

      try {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }

        if (!this.userInfo || !this.userInfo.id) {
          return;
        }

        const commentData = {
          videoId: this.$route.params.id,
          userUid: this.userInfo.id,
          content: this.newComment.trim(),
          createTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString(),
        };

        const response = await axios.post(
          "http://127.0.0.1:8081/comments/add",
          commentData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data === "评论添加成功") {
          this.newComment = "";
          // 通知父组件刷新评论列表
          this.$emit("comment-added");
        }
      } catch (error) {
        console.error("发送评论失败:", error);
      }
    },
  },
};
</script>

<style scoped>
.comment-list {
  margin-top: 20px;
}

.comment-item .comment-content {
  flex: 1;
}

.replies-section {
  margin-left: 20px;
}

.replies-section .reply-item .reply-content {
  flex: 1;
}

/* 回复按钮和内容之间增加间距 */
.comment-item .comment-content {
  padding-bottom: 10px;
}

.reply-item .reply-content {
  padding-left: 5px;
}

.reply-input {
  margin-top: 15px;
}

.edit-reply {
  margin-top: 10px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.nested-replies {
  margin-left: 10px;
  padding-left: 10px;
  border-left: 2px solid #eee;
}

.nested-reply-item {
  font-size: 0.9em;
}

.nested-reply-content {
  flex: 1;
}
</style>
