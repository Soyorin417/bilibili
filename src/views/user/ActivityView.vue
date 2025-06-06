<template>
  <div class="activity-page">
    <VideoBar />
    <div class="activity-container">
      <!-- 左侧用户信息 -->
      <UserActivityCard
        :userInfo="userInfo"
        @avatar-click="handleAvatarClick"
        @username-click="handleUsernameClick"
        @follow-click="handleFollowClick"
        @fans-click="handleFansClick"
        @dynamic-click="handleDynamicClick"
      />

      <!-- 中间动态列表 -->
      <div class="main-content">
        <PostEditor
          v-model="postContent"
          @post="handlePost"
          @image-change="handleImageChange"
          @image-remove="handleImageRemove"
          @emoji-select="handleEmojiSelect"
          @emoji-picker-toggle="handleEmojiPickerToggle"
          @content-change="handleContentChange"
        />

        <!-- 动态分类导航 -->
        <div class="feed-nav card">
          <div class="card-body d-flex">
            <a href="#" class="nav-item active me-4">全部</a>
            <a href="#" class="nav-item me-4">视频投稿</a>
            <a href="#" class="nav-item me-4">追番追剧</a>
            <a href="#" class="nav-item">专栏</a>
          </div>
        </div>

        <!-- 动态列表 -->
        <div class="feed-list">
          <ActivityCard
            v-for="post in posts"
            :key="post.id"
            :post="post"
            :userInfo="userInfo"
            @like="handleLike"
            @share="handleShare"
            @deletePost="deletePost"
            @comments="handleComments"
            @refresh-posts="fetchPosts"
          />
        </div>
      </div>

      <!-- 右侧话题列表 -->
      <TopicList :topics="topics" @topic-click="handleTopicClick" />
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import ActivityCard from "@/components/activity/ActivityCard.vue";
import PostEditor from "@/components/utils/PostEditor.vue";
import UserActivityCard from "@/components/activity/UserActivityCard.vue";
import TopicList from "@/components/activity/TopicList.vue";
import { mapGetters } from "vuex";
import postApi from "@/api/user/post";

export default {
  name: "ActivityView",
  components: {
    VideoBar,
    ActivityCard,
    PostEditor,
    UserActivityCard,
    TopicList,
  },
  data() {
    return {
      postContent: "",
      selectedImage: null,
      showEmojiPicker: false,
      posts: [],
      topics: [
        {
          id: 1,
          title: "鸣翠笑拉斯维嘎强摄影师招募",
          views: "86.1万",
          comments: "5164",
        },
        {
          id: 2,
          title: "你昨天做了什么梦？",
          views: "78.1万",
          comments: "1546",
        },
        {
          id: 3,
          title: "B站年度最佳新番评选",
          views: "92.3万",
          comments: "3872",
        },
        {
          id: 4,
          title: "2024年新年计划",
          views: "65.8万",
          comments: "2341",
        },
      ],
    };
  },
  methods: {
    handleLike({ id, liked, likeCount }) {
      const post = this.posts.find((p) => p.id === id);
      if (post) {
        post.liked = liked;
        post.likeCount = likeCount;
      }
    },
    handleShare({ id, shared }) {
      const post = this.posts.find((p) => p.id === id);
      if (post) {
        post.shared = shared;
      }
    },
    handleComments({ id, comments }) {
      const post = this.posts.find((p) => p.id === id);
      if (post) {
        post.comments = comments;
      }
    },
    handleContentChange(content) {
      this.postContent = content;
    },
    handleEmojiSelect(emoji) {
      this.postContent += emoji.i;
      this.showEmojiPicker = false;
    },
    handleEmojiPickerToggle(show) {
      this.showEmojiPicker = show;
    },
    handleImageChange(image) {
      this.selectedImage = image;
    },
    handleImageRemove() {
      this.selectedImage = null;
    },
    async handlePost({ content, image }) {
      if (!this.userInfo || !this.userInfo.username) {
        alert("用户信息未加载，请稍后再试");
        return;
      }
      if (!content.trim() && !image) return;

      const tempId = "temp_" + Date.now();
      this.posts.unshift({
        id: tempId,
        user_id: this.userInfo.id,
        username: this.userInfo.username,
        avatar: this.userInfo.avatar,
        time: "刚刚",
        content,
        media: image || null,
        is_image: !!image,
        views: 0,
        comments: 0,
        likeCount: 0,
        liked: false,
        shared: false,
      });

      const postData = {
        userId: this.userInfo.id,
        content,
        media: image || null,
        isImage: !!image,
      };

      try {
        const res = await postApi.createPost(postData);
        if (res && res.data === "动态创建成功") {
          console.log("创建动态成功");
        } else {
          this.posts = this.posts.filter((post) => post.id !== tempId);
          alert("创建动态失败，请稍后再试");
        }
      } catch (e) {
        this.posts = this.posts.filter((post) => post.id !== tempId);
        alert("创建动态失败，请稍后再试");
        console.error(e);
      }
    },

    async fetchPosts() {
      if (!this.userInfo || !this.userInfo.id) return;

      try {
        const response = await postApi.getPosts(this.userInfo.id);
        this.posts = response.data.map((post) => ({
          id: post.id,
          user_id: post.userId,
          username: post.username,
          avatar: post.avatar,
          time: post.createTime,
          content: post.content,
          media: post.media,
          duration: post.duration,
          views: post.views,
          comments: post.commentCount,
          likeCount: post.likeCount,
          liked: post.liked,
          shared: post.shared,
          is_image: post.isImage,
          shareCount: post.shareCount,
        }));

        console.log("获取动态成功:", this.posts);
      } catch (error) {
        console.error("获取动态失败:", error);
      }
    },
    async deletePost(id) {
      const oldPosts = [...this.posts];
      this.posts = this.posts.filter((post) => post.id !== id);

      if (typeof id === "string" && id.startsWith("temp_")) {
        return;
      }

      try {
        const res = await postApi.deletePost(id);
        if (res.status === 200) {
          console.log("删除成功");
        } else {
          this.posts = oldPosts;
          alert("删除失败，请稍后再试");
        }
      } catch (e) {
        this.posts = oldPosts;
        alert("删除失败，请稍后再试");
        console.error("删除动态失败", e);
      }
    },
    async updatePost(id, postData) {
      try {
        const res = await postApi.updatePost(id, postData);
        if (res === "动态更新成功") {
          console.log("更新动态成功");
        }
      } catch (e) {
        console.error("更新动态失败", e);
      }
    },
    // 处理头像点击
    handleAvatarClick(userInfo) {
      // 可以跳转到用户主页或打开头像预览
      console.log("Avatar clicked:", userInfo);
    },

    async submitComment(postId, parentCommentId, content) {
      const commentData = {
        postId,
        parentCommentId: parentCommentId || null,
        content,
      };

      try {
        const res = await postApi.addComment(commentData);
        console.log("评论成功", res);
      } catch (error) {
        console.error("评论失败", error);
      }
    },
    // 处理用户名点击
    handleUsernameClick(userInfo) {
      // 可以跳转到用户主页
      this.$router.push({
        name: "UserDetail",
        params: { id: userInfo.id },
        query: {
          username: userInfo.username,
        },
      });
      console.log("Username clicked:", userInfo);
    },

    // 处理关注点击
    handleFollowClick(userInfo) {
      // 可以跳转到关注列表
      console.log("Follow clicked:", userInfo);
    },

    // 处理粉丝点击
    handleFansClick(userInfo) {
      // 可以跳转到粉丝列表
      console.log("Fans clicked:", userInfo);
    },

    // 处理动态点击
    handleDynamicClick(userInfo) {
      // 可以跳转到用户动态列表
      console.log("Dynamic clicked:", userInfo);
    },

    // 处理话题点击
    handleTopicClick(topic) {
      // 可以跳转到话题详情页
      this.$router.push({
        name: "TopicDetail",
        params: { id: topic.id },
        query: {
          title: topic.title,
          views: topic.views,
          comments: topic.comments,
        },
      });
    },
    onSelectEmoji(emoji) {
      if (emoji && emoji.i) {
        this.postContent += emoji.i;
        this.showEmojiPicker = false;
      }
    },
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  mounted() {
    this.fetchPosts();
    document.addEventListener("click", (e) => {
      if (!e.target.closest(".emoji-picker-container")) {
        this.showEmojiPicker = false;
      }
    });
  },
  watch: {
    userInfo: {
      immediate: true,
    },
  },
  beforeUnmount() {
    document.removeEventListener("click", this.closeEmojiPicker);
  },
};
</script>

<style scoped>
.activity-page {
  background-color: #f4f5f7;
  min-height: 100vh;
}

.activity-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  display: grid;
  grid-template-columns: 240px 1fr 300px;
  gap: 20px;
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 16px;
  margin-bottom: 16px;
}

.feed-nav {
  margin-bottom: 16px;
}

.feed-nav .card-body {
  padding: 0 16px;
  height: 46px;
  align-items: center;
}

.nav-item {
  color: #666;
  text-decoration: none;
  font-size: 14px;
  padding: 12px 0;
  position: relative;
  transition: color 0.2s;
}

.nav-item:hover {
  color: #00aeec;
}

.nav-item.active {
  color: #00aeec;
  font-weight: 500;
}

.nav-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: #00aeec;
}
</style>
