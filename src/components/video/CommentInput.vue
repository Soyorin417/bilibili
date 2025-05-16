<template>
  <div class="comment-input">
    <div class="form-floating mb-3">
      <input
        @click.stop="showButton = true"
        v-model="content"
        type="text"
        class="form-control"
        id="floatingInput"
        placeholder="与其赞同他人的话，不如自己畅所欲言"
      />
      <label for="floatingInput">与其赞同他人的话，不如自己畅所欲言</label>
    </div>
    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
      <button
        v-if="showButton"
        @click="handleSend"
        class="btn btn-primary btn-sm"
        type="button"
      >
        发送
      </button>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "CommentInput",
  data() {
    return {
      showButton: false,
      content: "",
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
    user() {
      return this.userInfo;
    },
    avatar() {
      return this.user.avatar;
    },
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
  methods: {
    handleClickOutside(event) {
      if (!event.target.closest(".form-floating")) {
        this.showButton = false;
      }
    },
    handleSend() {
      if (!this.content.trim()) return;

      const newComment = {
        commentId: Date.now(),
        userName: this.userInfo.username,
        avatar: this.userInfo.avatar,
        isUp: false,
        userLevel: this.userInfo.level || 0,
        content: this.content,
        createTime: new Date().toLocaleString(),
        likeCount: 0,
        isLiked: false,
        replyCount: 0,
        showReplies: false,
        replies: [],
      };

      this.$emit("send-comment", newComment);
      this.showButton = false;
      this.content = "";
    },
  },
};
</script>

<style scoped>
.comment-input {
  margin-bottom: 20px;
}
</style>
