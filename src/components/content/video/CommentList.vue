<template>
  <div class="comment-list" @click="handleOutsideClick">
    <div class="comment-container">
      <div v-for="comment in localComments" :key="comment.id" class="comment-item mb-4">
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
              <small class="me-3">{{ formatDateType(comment.createTime) }}</small>
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
                <i
                  class="bi bi-x"
                  @click="handleDeleteComment(comment)"
                  style="cursor: pointer"
                >
                  <small>删除</small></i
                >
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
                      <small class="me-3">{{ formatDateType(reply.createTime) }}</small>
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
                        <i
                          class="bi bi-x"
                          @click="handleDeleteReply(reply)"
                          style="cursor: pointer"
                          ><small>删除</small></i
                        >
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
  </div>
</template>

<script>
import { ThumbsUp, ThumbsDown, CommentOne } from "@icon-park/vue-next";
import { mapGetters } from "vuex";
import { commentApi } from "@/api/comment";
import { formatDateType } from "@/utils/date";
export default {
  name: "CommentList",
  components: {
    ThumbsUp,
    ThumbsDown,
    CommentOne,
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
      activeCommentId: null,
      localComments: [],
    };
  },
  watch: {
    comments: {
      immediate: true,
      handler(newComments) {
        this.localComments = [...newComments];
      },
    },
  },
  methods: {
    formatDateType,
    handleOutsideClick(event) {
      // 检查点击的元素是否在回复区域内
      const isReplyElement =
        event.target.closest(".replies-section") ||
        event.target.closest(".comment-content") ||
        event.target.closest(".comment-input") ||
        event.target.closest(".comment-item");

      // 如果点击在回复区域外，则收起所有回复
      if (!isReplyElement) {
        this.localComments.forEach((comment) => {
          if (comment.showReplies) {
            comment.showReplies = false;
          }
        });
      }
    },
    async toggleReplies(comment) {
      console.log("toggleReplies called for comment:", comment);
      if (comment.showReplies) {
        console.log("Closing replies for comment:", comment.id);
        comment.showReplies = false;
        this.activeCommentId = null;
        return;
      }

      // 关闭其他评论的回复框
      this.localComments.forEach((c) => {
        if (c !== comment && c.showReplies) {
          console.log("Closing replies for other comment:", c.id);
          c.showReplies = false;
        }
      });

      console.log("Opening replies for comment:", comment.id);
      comment.showReplies = true;
      this.activeCommentId = comment.id;

      try {
        // 获取回复列表
        console.log("Fetching replies for comment:", comment.id);
        const response = await commentApi.getReplies(comment.id);
        console.log("Replies response:", response);
        if (response.data) {
          comment.replies = response.data;
          console.log("Setting replies for comment:", comment.id, comment.replies);
          // 检查每条回复的点赞状态
          for (const reply of comment.replies) {
            if (this.userInfo?.id) {
              console.log("Checking like status for reply:", reply.id);
              const likeStatus = await commentApi.checkLikeStatus(
                reply.id,
                this.userInfo.id
              );
              console.log("Like status for reply:", reply.id, likeStatus);
              reply.isLiked = likeStatus.data;
            }
          }
        }
      } catch (error) {
        console.error("获取回复失败:", error);
        console.error("Error details:", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
        });
        alert("获取回复失败，请稍后重试");
      }
    },
    async handleSendReply(comment) {
      console.log("handleSendReply called for comment:", comment);
      if (!this.userInfo) {
        console.log("User not logged in");
        alert("请先登录后再评论");
        return;
      }

      if (!comment.replyContent || !comment.replyContent.trim()) {
        console.log("Empty reply content");
        return;
      }

      const tempContent = comment.replyContent;
      console.log("Prepared reply content:", tempContent);

      try {
        const replyData = {
          commentId: comment.id,
          userUid: this.userInfo.id,
          content: tempContent.trim(),
        };
        console.log("Sending reply data:", replyData);

        const response = await commentApi.addReply(replyData);
        console.log("Reply response:", response);
        if (response.data === "回复添加成功") {
          console.log("Reply added successfully, refreshing replies");
          comment.replyContent = "";
          // 重新获取回复列表
          await this.toggleReplies(comment);
        } else {
          console.log("Reply failed:", response.data);
          alert("回复发布失败，请稍后重试");
        }
      } catch (error) {
        console.error("发送回复失败:", error);
        console.error("Error details:", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
        });
        comment.replyContent = tempContent;
        alert("发送回复失败，请稍后重试");
      }
    },
    async handleSendReplyToReply(comment, reply) {
      console.log("handleSendReplyToReply called:", { comment, reply });
      if (!this.userInfo) {
        console.log("User not logged in");
        alert("请先登录后再回复");
        return;
      }

      if (!reply.replyContent || !reply.replyContent.trim()) {
        console.log("Empty reply content");
        return;
      }

      const content = `@${reply.username} ${reply.replyContent.trim()}`;
      console.log("Prepared reply content:", content);
      const newReplyData = {
        commentId: comment.id,
        userUid: this.userInfo.id,
        content,
        parentReplyId: reply.id,
      };
      console.log("Sending reply data:", newReplyData);

      try {
        const response = await commentApi.addReply(newReplyData);
        console.log("Reply response:", response);
        if (response.data === "回复添加成功") {
          console.log("Reply added successfully, refreshing replies");
          // 重新获取回复列表
          await this.toggleReplies(comment);
          reply.replyContent = "";
          reply.showReplies = false;
        } else {
          console.log("Reply failed:", response.data);
          alert("回复发布失败，请稍后重试");
        }
      } catch (error) {
        console.error("回复回复失败", error);
        console.error("Error details:", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
        });
        alert("回复失败，请稍后重试");
      }
    },
    async handleDeleteComment(comment) {
      if (!confirm("确定要删除这条评论吗？")) return;

      try {
        const response = await commentApi.deleteComment(comment.id);
        if (response.data === "删除成功") {
          this.$emit("comment-deleted");
        }
      } catch (error) {
        console.error("删除评论失败:", error);
        alert("删除评论失败，请稍后重试");
      }
    },
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
        if (!this.userInfo?.id) {
          alert("请先登录");
          return;
        }

        const response = await (comment.isLiked
          ? commentApi.unlikeComment(comment.id, this.userInfo.id)
          : commentApi.likeComment(comment.id, this.userInfo.id));

        if (response.data === "点赞成功" || response.data === "取消点赞成功") {
          comment.isLiked = !comment.isLiked;
          comment.likeCount = comment.isLiked
            ? comment.likeCount + 1
            : comment.likeCount - 1;
        } else {
          alert(response.data);
        }
      } catch (error) {
        console.error("点赞操作失败:", error);
        alert("操作失败，请稍后重试");
      }
    },
    async checkLikeStatus(comment) {
      try {
        if (!this.userInfo?.id) return;
        const response = await commentApi.checkLikeStatus(comment.id, this.userInfo.id);
        comment.isLiked = Boolean(response.data);
      } catch (error) {
        console.error("检查点赞状态失败:", error);
      }
    },
    async handleDeleteReply(reply) {
      if (!confirm("确定要删除这条回复吗？")) return;

      try {
        const response = await commentApi.deleteReply(reply.id);
        if (response.data === "删除成功") {
          const comment = this.localComments.find((c) =>
            c.replies.some((r) => r.id === reply.id)
          );
          if (comment) {
            comment.replies = comment.replies.filter((r) => r.id !== reply.id);
            comment.replyCount--;
          }
        }
      } catch (error) {
        console.error("删除回复失败:", error);
        alert("删除回复失败，请稍后重试");
      }
    },
    async handleUpdateReply(reply) {
      if (!reply.editContent || !reply.editContent.trim()) return;

      try {
        const updateData = {
          id: reply.id,
          commentId: reply.commentId,
          userId: reply.userUid,
          content: reply.editContent.trim(),
          createTime: reply.createTime,
          username: this.userInfo.username,
          avatar: this.userInfo.avatar,
          level: this.userInfo.level,
          isUp: this.userInfo.id === reply.authorId,
          likeCount: reply.likeCount,
          isLiked: reply.isLiked,
        };

        const response = await commentApi.updateReply(reply.id, updateData);
        if (response.data === "回复更新成功") {
          reply.content = reply.editContent.trim();
          reply.isEditing = false;
          reply.editContent = "";
        }
      } catch (error) {
        console.error("更新回复失败:", error);
        alert("更新回复失败，请稍后重试");
      }
    },
    async handleSendComment() {
      if (!this.userInfo) {
        alert("请先登录后再评论");
        return;
      }

      if (!this.newComment || !this.newComment.trim()) return;

      try {
        const commentData = {
          videoId: this.$route.params.id,
          userId: this.userInfo.id,
          content: this.newComment.trim(),
        };

        const response = await commentApi.addComment(commentData);
        if (response.data === "评论发布成功") {
          this.newComment = "";
          this.$emit("comment-added");
        } else {
          alert("评论发布失败，请稍后重试");
        }
      } catch (error) {
        console.error("发送评论失败:", error);
        alert("发送评论失败，请稍后重试");
      }
    },
    // 添加处理新回复的方法
    handleNewReply(reply) {
      const comment = this.localComments.find((c) => c.id === reply.commentId);
      if (comment) {
        if (!Array.isArray(comment.replies)) {
          comment.replies = [];
        }

        // 检查是否已经存在相同的回复
        const isDuplicate = comment.replies.some(
          (r) =>
            r.content === reply.content &&
            r.userUid === reply.userUid &&
            Math.abs(new Date(r.createTime) - new Date(reply.createTime)) < 1000
        );

        if (!isDuplicate) {
          comment.replies.unshift(reply);
          comment.replyCount = (comment.replyCount || 0) + 1;
        }
      }
    },
  },

  mounted() {
    console.log("CommentList component mounted");
    console.log("Initial comments:", this.comments);
    this.localComments = this.comments;
    console.log("Local comments set:", this.localComments);
  },
};
</script>

<style scoped>
.comment-list {
  position: relative;
  margin-top: 20px;
}

/* 保留其他原有样式 */
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
