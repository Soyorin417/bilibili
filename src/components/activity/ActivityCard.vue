<template>
  <div v-if="post && userInfo" class="post-card card">
    <PostHeader
      :post="post"
      :userInfo="userInfo"
      @delete="$emit('deletePost', post.id)"
    />
    <PostContent :post="post" @video-click="handleVideoClick" />
    <PostActions
      :shared="localShared"
      :is-comments="is_comments"
      :liked="localLiked"
      :comment-count="post.comments"
      :like-count="localLikes"
      @share="handleShare"
      @comments="handleComments"
      @like="handleLike"
    />
    <CommentSection
      :show-comment-box="localShared || is_comments"
      :show-comments="is_comments"
      :is-share="localShared"
      :comments="comments"
      :user-info="userInfo"
      @send-post="handleSendPost"
      @delete-comment="handleDeleteComment"
      @like-comment="handleCommentLike"
      @reply-comment="replyComment"
    />
  </div>
</template>

<script>
import PostHeader from "./PostHeader.vue";
import PostContent from "./PostContent.vue";
import PostActions from "./PostActions.vue";
import CommentSection from "./CommentSection.vue";
import { postApi } from "@/api/post";

export default {
  name: "ActivityCard",
  components: {
    PostHeader,
    PostContent,
    PostActions,
    CommentSection,
  },
  props: {
    post: {
      type: Object,
      required: true,
      default: () => ({
        id: "",
        user_id: "",
        username: "",
        avatar: "",
        time: "",
        content: "",
        media: "",
        duration: "",
        views: 0,
        comments: 0,
        likes: 0,
        liked: false,
        shared: false,
        video: {
          id: "",
          image: "",
          title: "",
          duration: "",
          playCount: 0,
          commentCount: 0,
        },
      }),
    },
    userInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localLiked: this.post.liked,
      localShared: this.post.shared,
      localLikes: this.post.likeCount,
      localComments: this.post.comments,
      is_comments: false,
      comments: [],
    };
  },
  methods: {
    async handleLike() {
      if (!this.post || !this.post.id) {
        alert("动态数据未加载，请稍后再试");
        return;
      }
      if (!this.userInfo || !this.userInfo.id) {
        alert("请先登录");
        return;
      }
      const postId = this.post.id;
      const userId = this.userInfo.id;

      // 1. 本地先更新
      const oldLiked = this.localLiked;
      const oldLikes = this.localLikes;
      if (!this.localLiked) {
        this.localLiked = true;
        this.localLikes++;
      } else {
        this.localLiked = false;
        this.localLikes--;
      }

      try {
        if (!oldLiked) {
          // 点赞
          const res = await postApi.likePost(postId, userId);
          if (res.data !== "点赞成功") {
            // 失败回滚
            this.localLiked = oldLiked;
            this.localLikes = oldLikes;
          }
        } else {
          // 取消点赞
          const res = await postApi.unlikePost(postId, userId);
          if (res.data !== "取消点赞成功") {
            // 失败回滚
            this.localLiked = oldLiked;
            this.localLikes = oldLikes;
          }
        }
        this.$emit("like", {
          id: postId,
          liked: this.localLiked,
          likeCount: this.localLikes,
        });
      } catch (e) {
        // 失败回滚
        this.localLiked = oldLiked;
        this.localLikes = oldLikes;
        alert("操作失败，请稍后重试");
        console.error(e);
      }
    },
    handleComments() {
      this.is_comments = !this.is_comments;
      this.$emit("comments", { id: this.post.id, comments: this.localComments });
    },
    handleShare() {
      this.localShared = !this.localShared;
      this.$emit("share", { id: this.post.id, shared: this.localShared });
    },
    handleVideoClick() {
      // 处理视频点击事件
      this.$emit("video-click", this.post.video);
    },
    async handleSendPost(data) {
      if (this.localShared) {
        // 处理转发
        this.$emit("share-post", {
          id: this.post.id,
          content: data.content,
          image: data.image,
        });
      } else {
        try {
          // 处理评论
          const comment = {
            postId: this.post.id,
            content: data.content,
            parentCommentId: data.parentCommentId, // 如果是回复评论，则包含父评论ID
          };

          const res = await postApi.addComment(comment);
          if (res.data === "评论成功") {
            // 刷新评论列表
            await this.fetchComments(this.post.id);
            this.localComments++;
            this.$emit("new-comment", {
              id: this.post.id,
              comments: this.localComments,
            });
          }
        } catch (e) {
          console.error("发送评论失败", e);
          alert("发送失败，请稍后重试");
        }
      }
    },
    async handleCommentLike(comment) {
      if (!this.userInfo?.id) {
        alert("请先登录");
        return;
      }

      if (comment.liked) {
        await this.unlikeComment(comment);
      } else {
        await this.likeComment(comment);
      }
    },
    async likeComment(comment) {
      try {
        const res = await postApi.likeComment(comment.id);
        if (res.data === "点赞成功") {
          comment.likes++;
          comment.liked = true;
        }
      } catch (e) {
        console.error("点赞失败", e);
        alert("点赞失败，请稍后重试");
      }
    },
    async unlikeComment(comment) {
      try {
        const res = await postApi.unlikeComment(comment.id);
        if (res.data === "取消点赞成功") {
          comment.likes--;
          comment.liked = false;
        }
      } catch (e) {
        console.error("取消点赞失败", e);
        alert("取消点赞失败，请稍后重试");
      }
    },
    replyComment(comment) {
      this.$emit("reply-comment", comment);
    },
    async handleDeleteComment(commentId) {
      if (!window.confirm("确定要删除这条评论吗？")) return;
      try {
        const res = await postApi.deleteComment(commentId);
        if (res.data === "删除成功") {
          // 刷新评论列表
          await this.fetchComments(this.post.id);
          this.localComments--;
          this.$emit("new-comment", {
            id: this.post.id,
            comments: this.localComments,
          });
        } else {
          alert(typeof res.data === "string" ? res.data : JSON.stringify(res.data));
        }
      } catch (e) {
        alert("删除失败，请稍后再试");
        console.error("删除评论失败", e);
      }
    },
    async fetchComments(postId) {
      try {
        const res = await postApi.getComments(postId, this.userInfo?.id);
        this.comments = res.data;
      } catch (e) {
        console.error("获取评论失败", e);
      }
    },
    async loadReplies(parentCommentId) {
      try {
        const res = await postApi.getReplies(parentCommentId);
        return res.data;
      } catch (e) {
        console.error("获取回复失败", e);
        return [];
      }
    },
  },
  watch: {
    "post.liked"(newVal) {
      this.localLiked = newVal;
    },
    "post.shared"(newVal) {
      this.localShared = newVal;
    },
    "post.likes"(newVal) {
      this.localLikes = newVal;
    },
    "post.comments"(newVal) {
      this.localComments = newVal;
    },
    is_comments(newVal) {
      if (newVal) {
        this.fetchComments(this.post.id);
      }
    },
  },
};
</script>

<style scoped>
.post-card {
  background: #fff;
  border-radius: 4px;
  padding: 12px 16px;
  margin-bottom: 12px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s ease;
}

.post-card:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
