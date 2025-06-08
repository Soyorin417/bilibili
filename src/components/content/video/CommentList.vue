<template>
  <div class="comment-list">
    <!-- 评论输入框 -->
    <div class="comment-input mb-4">
      <div class="form-floating">
        <input
          v-model="commentContent"
          type="text"
          class="form-control"
          id="commentInput"
          placeholder="发表评论"
        />
        <label for="commentInput">发表评论</label>
      </div>
      <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-2">
        <button @click="sendComment" class="btn btn-primary btn-sm" type="button">
          发送评论
        </button>
      </div>
    </div>

    <!-- 评论列表 -->
    <CommentItem
      v-for="comment in comments"
      :key="comment.id"
      :comment="comment"
      :user-info="userInfo"
      @like="handleLike"
      @dislike="handleDislike"
      @toggle-replies="handleToggleReplies"
      @delete="handleDeleteComment"
      @delete-reply="handleDeleteReply"
      @update-reply="handleUpdateReply"
      @send-reply="handleSendReply"
    />
  </div>
</template>

<script>
import CommentItem from "./CommentItem.vue";
import commentApi from "@/api/message/comment";
import { ElMessage } from "element-plus";

export default {
  name: "CommentList",
  components: {
    CommentItem,
  },
  props: {
    videoId: {
      type: [String, Number],
      required: true,
    },
    userInfo: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      comments: [],
      commentContent: "",
    };
  },
  created() {
    this.getComments();
  },
  methods: {
    async getComments() {
      try {
        console.log(this.videoId, "this.videoId");
        const response = await commentApi.getComments(this.videoId);
        console.log("获取到的评论数据:", response.data);
        this.comments = response.data;
      } catch (error) {
        console.error("获取评论失败:", error);
        ElMessage.error("获取评论失败");
      }
    },

    async sendComment() {
      if (!this.commentContent.trim()) {
        ElMessage.warning("评论内容不能为空");
        return;
      }

      try {
        const response = await commentApi.addComment({
          videoId: this.videoId,
          content: this.commentContent,
        });

        if (response.data === "评论发布成功") {
          this.commentContent = "";
          ElMessage.success("评论发送成功");
          // 重新获取评论列表
          await this.getComments();
        } else {
          ElMessage.error(response.data || "评论发送失败");
        }
      } catch (error) {
        console.error("发送评论失败:", error);
        ElMessage.error("发送评论失败");
      }
    },
    async handleLike(comment) {
      if (!this.userInfo || !this.userInfo.id) {
        ElMessage.warning("请先登录");
        return;
      }

      try {
        let response;
        if (comment.isLiked) {
          // 如果已经点赞，则取消点赞
          response = await commentApi.unlikeComment(comment.id, this.userInfo.id);
        } else {
          // 如果未点赞，则点赞
          response = await commentApi.likeComment(comment.id, this.userInfo.id);
        }

        // 根据后端返回的数据更新前端状态
        if (response.data && typeof response.data.isLiked !== "undefined") {
          comment.isLiked = response.data.isLiked;
          comment.likeCount = response.data.likeCount;
        } else {
          // 如果后端没有返回isLiked和likeCount，则手动更新
          comment.isLiked = !comment.isLiked;
          comment.likeCount = comment.isLiked
            ? comment.likeCount + 1
            : comment.likeCount - 1;
        }

        ElMessage.success(comment.isLiked ? "点赞成功" : "取消点赞成功");
      } catch (error) {
        console.error("点赞操作失败:", error);
        ElMessage.error("操作失败");
      }
    },

    async handleDislike(comment) {
      // 点踩逻辑通常不涉及isLiked状态，这里只做简单的计数更新
      // 如果后端有对应的API，也应该调用
      comment.isDisliked = !comment.isDisliked;
      comment.dislikeCount = comment.isDisliked
        ? (comment.dislikeCount || 0) + 1
        : (comment.dislikeCount || 0) - 1;
      ElMessage.success(comment.isDisliked ? "点踩成功" : "取消点踩成功");
    },

    handleToggleReplies(comment) {
      comment.showReplies = !comment.showReplies;
      if (comment.showReplies && !comment.replies) {
        this.getReplies(comment);
      }
    },

    async getReplies(comment) {
      try {
        const response = await commentApi.getReplies(comment.id);
        comment.replies = response.data;
      } catch (error) {
        console.error("获取回复失败:", error);
        ElMessage.error("获取回复失败");
      }
    },

    async handleDeleteComment(comment) {
      try {
        await commentApi.deleteComment(comment.id);
        const index = this.comments.findIndex((c) => c.id === comment.id);
        if (index !== -1) {
          this.comments.splice(index, 1);
        }
        ElMessage.success("评论删除成功");
      } catch (error) {
        console.error("删除评论失败:", error);
        ElMessage.error("删除评论失败");
      }
    },

    async handleDeleteReply(reply) {
      try {
        await commentApi.deleteReply(reply.id);
        const comment = this.comments.find((c) => c.id === reply.parentId);
        if (comment && comment.replies) {
          const index = comment.replies.findIndex((r) => r.id === reply.id);
          if (index !== -1) {
            comment.replies.splice(index, 1);
          }
        }
        ElMessage.success("回复删除成功");
      } catch (error) {
        console.error("删除回复失败:", error);
        ElMessage.error("删除回复失败");
      }
    },

    async handleUpdateReply(reply) {
      try {
        await commentApi.updateReply(reply.id, {
          content: reply.editContent,
        });
        reply.content = reply.editContent;
        reply.isEditing = false;
        ElMessage.success("回复更新成功");
      } catch (error) {
        console.error("更新回复失败:", error);
        ElMessage.error("更新回复失败");
      }
    },

    async handleSendReply(comment) {
      if (!comment.replyContent.trim()) {
        ElMessage.warning("回复内容不能为空");
        return;
      }

      try {
        const replyData = {
          commentId: comment.id,
          content: comment.replyContent,
        };
        console.log("发送回复数据:", replyData);
        console.log("comment.id:", comment.id);
        console.log("comment.replyContent:", comment.replyContent);

        const response = await commentApi.addReply(replyData);

        if (response.data === "回复发布成功") {
          comment.replyContent = "";
          ElMessage.success("回复发送成功");
          // 重新获取回复列表
          await this.getReplies(comment);
        } else {
          ElMessage.error(response.data || "回复发送失败");
        }
      } catch (error) {
        console.error("发送回复失败:", error);
        ElMessage.error("发送回复失败");
      }
    },
  },
};
</script>

<style scoped>
.comment-list {
  padding: 20px;
}

.comment-input {
  margin-bottom: 20px;
}
</style>
