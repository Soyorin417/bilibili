<template>
  <div class="search-view">
    <VideoBar />
    <div class="search-container">
      <!-- 分类导航 -->
      <div class="category-nav">
        <div class="nav-items">
          <router-link to="/search?type=all" class="nav-item active">综合</router-link>
          <router-link to="/search?type=video" class="nav-item">
            视频
            <span class="count">99+</span>
          </router-link>
          <router-link to="/search?type=bangumi" class="nav-item">
            番剧
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=movie" class="nav-item">
            影视
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=live" class="nav-item">
            直播
            <span class="count">0</span>
          </router-link>
          <router-link to="/search?type=article" class="nav-item">
            专栏
            <span class="count">31</span>
          </router-link>
          <router-link to="/search?type=user" class="nav-item">
            用户
            <span class="count">0</span>
          </router-link>
        </div>
      </div>

      <!-- 二级筛选 -->
      <div class="sub-filters">
        <div class="filter-tags">
          <span class="filter-tag active">综合排序</span>
          <span class="filter-tag">最多播放</span>
          <span class="filter-tag">最新发布</span>
          <span class="filter-tag">最多收藏</span>
          <span class="filter-tag">最多弹幕</span>
        </div>
        <div class="more-filters">
          更多筛选
          <i class="bi bi-chevron-down"></i>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results">
        <div v-if="loading" class="loading">
          <div class="spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="results.length === 0 && users.length === 0" class="no-results">
          <i class="bi bi-search"></i>
          <p>没有找到相关内容</p>
        </div>
        <div v-else>
          <!-- 用户搜索结果 -->
          <div v-if="users.length > 0" class="user-results mb-4">
            <h3 class="section-title">用户</h3>
            <div class="user-grid">
              <div v-for="user in users" :key="user.id" class="user-card">
                <div class="user-card-left">
                  <img
                    class="user-avatar"
                    :src="user.avatar"
                    :alt="user.username"
                    @click="goToChat(user.id, user.avatar, user.username)"
                    style="cursor: pointer"
                  />
                </div>
                <div class="user-card-main">
                  <div class="user-header">
                    <span class="username">{{ user.username }}</span>
                    <span class="user-level" v-if="user.level">LV{{ user.level }}</span>
                  </div>
                  <div class="user-meta-desc">
                    <span>{{ user.fans }}粉丝 ·</span>
                    <span>{{ user.videoCount }}个视频 ·</span>
                    <span class="user-desc">{{
                      user.description || "这个用户很懒，什么都没写~"
                    }}</span>
                  </div>
                  <button
                    class="follow-btn"
                    :class="{ followed: user.follow }"
                    @click="user.follow ? handleUnfollow(user) : handleFollow(user)"
                  >
                    {{ user.follow ? "已关注" : "+ 关注" }}
                  </button>
                </div>
                <div class="user-card-action"></div>
              </div>
            </div>
          </div>

          <!-- 视频搜索结果 -->
          <div v-if="results.length > 0" class="video-results">
            <h3 class="section-title">视频</h3>
            <div class="results-grid">
              <div v-for="(item, index) in results" :key="index" class="result-card">
                <router-link :to="`/video/${item.id}`" class="video-link" target="_blank">
                  <div class="video-thumbnail">
                    <img :src="item.image" :alt="item.title" />
                    <span class="video-duration">{{ item.duration }}</span>
                    <div class="video-stats">
                      <span><i class="bi bi-play-fill"></i> {{ item.views }}</span>
                      <span><i class="bi bi-chat-fill"></i> {{ item.comments }}</span>
                    </div>
                  </div>
                  <div class="video-info">
                    <h3 class="video-title">{{ item.title }}</h3>
                    <div class="video-meta">
                      <div class="uploader-info">
                        <UpIcon></UpIcon>
                        <span class="uploader-name">{{ item.author }}</span>
                        <span class="upload-time">{{ item.time }}</span>
                      </div>
                    </div>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import { mapGetters } from "vuex";
import { searchApi } from "@/api/content/search";
import { userApi } from "@/api/user/user";
import { formatDateType, formatCount } from "@/utils/date";
import UpIcon from "@/components/utils/ui/UpIcon.vue";
import { goToChat } from "@/utils/chatUtils";

export default {
  name: "SearchView",
  components: {
    VideoBar,
    UpIcon,
  },
  data() {
    return {
      keyword: "",
      loading: true,
      totalResults: 0,
      currentFilter: "all",
      results: [],
      videoInfos: [],
      users: [],
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
  },
  created() {
    this.keyword = this.$route.query.keyword || "";
    this.fetchSearchResults();
  },
  methods: {
    formatDateType,
    formatCount,
    async fetchSearchResults() {
      try {
        this.loading = true;
        const keyword = this.$route.query.keyword || "";

        // 搜索视频
        const videoResponse = await searchApi.searchVideo(keyword);
        console.log("视频搜索返回：", videoResponse.data);
        this.results = videoResponse.data.map((video) => ({
          id: video.id,
          title: video.title,
          image: video.image,
          duration: video.duration || "0:00",
          author: video.author,
          uploaderAvatar: video.uploaderAvatar,
          views: formatCount(video.views || 0),
          danmaku: formatCount(video.danmaku_count || 0),
          comments: video.comments,
          time: formatDateType(video.time),
        }));

        // 如果用户已登录，搜索用户
        if (this.userInfo && this.userInfo.id) {
          const userResponse = await searchApi.searchUser(keyword, this.userInfo.id);
          console.log("用户搜索返回：", userResponse.data);
          this.users = userResponse.data.map((user) => ({
            id: user.id,
            username: user.username,
            avatar: user.avatar,
            fans: user.fans,
            videoCount: user.videoCount,
            level: user.level,
            follow: user.follow,
          }));
        }

        this.totalResults = this.results.length;
      } catch (error) {
        console.error("获取搜索结果失败:", error);
        this.error = "获取搜索结果失败，请稍后重试";
      } finally {
        this.loading = false;
      }
    },
    async handleFollow(user) {
      if (!this.userInfo || !this.userInfo.id) {
        this.$message && this.$message.warning("请先登录后再关注");
        return;
      }
      try {
        await userApi.followUser(user.id);
        user.follow = true;
        user.fans++;
        this.$message && this.$message.success("关注成功");
      } catch (error) {
        console.error("关注失败:", error);
        this.$message && this.$message.error("关注失败，请重试");
      }
    },
    async handleUnfollow(user) {
      if (!this.userInfo || !this.userInfo.id) {
        this.$message && this.$message.warning("请先登录后再取消关注");
        return;
      }
      try {
        await userApi.unfollowUser(user.id);
        user.follow = false;
        user.fans--;
        this.$message && this.$message.success("取消关注成功");
      } catch (error) {
        console.error("取消关注失败:", error);
        this.$message && this.$message.error("取消关注失败，请重试");
      }
    },
    goToChat(targetUserId, targetUserAvatar, targetUsername) {
      goToChat(
        this.userInfo,
        targetUserId,
        targetUserAvatar,
        targetUsername,
        this.$router
      );
    },
  },
  watch: {
    "$route.query": {
      handler() {
        this.fetchSearchResults();
      },
      deep: true,
    },
  },
};
</script>

<style scoped>
.search-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.search-container {
  max-width: 1700px;
  margin: 0 auto;
  padding: 16px;
}

.category-nav {
  background: #fff;
  padding: 0 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.nav-items {
  display: flex;
  align-items: center;
  height: 46px;
}

.nav-item {
  position: relative;
  padding: 0 16px;
  color: #61666d;
  text-decoration: none;
  font-size: 14px;
  display: flex;
  align-items: center;
  height: 100%;
}

.nav-item.active {
  color: #00aeec;
  font-weight: 500;
}

.nav-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 16px;
  right: 16px;
  height: 2px;
  background-color: #00aeec;
}

.count {
  margin-left: 4px;
  font-size: 12px;
  color: #9499a0;
}

.sub-filters {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.filter-tags {
  display: flex;
  gap: 24px;
}

.filter-tag {
  color: #61666d;
  font-size: 14px;
  cursor: pointer;
  padding: 6px 0;
}

.filter-tag:hover {
  color: #00aeec;
}

.filter-tag.active {
  color: #00aeec;
  font-weight: 500;
}

.more-filters {
  color: #61666d;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.result-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.video-link {
  text-decoration: none;
  color: inherit;
}

.video-thumbnail {
  position: relative;
  padding-top: 56.25%;
}

.video-thumbnail::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(to bottom, transparent, rgba(0, 0, 0, 0.7));
  pointer-events: none;
}

.video-thumbnail img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.video-stats {
  position: absolute;
  bottom: 8px;
  left: 8px;
  font-size: 12px;
  display: flex;
  gap: 12px;
  color: #fff;
  z-index: 1;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  padding: 2px 4px;
  border-radius: 4px;
  font-size: 12px;
  color: #fff;
  z-index: 1;
}

.video-info {
  padding: 8px;
}

.video-title {
  font-size: 15px;
  line-height: 1.5;
  height: 22px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  font-weight: 500;
  text-align: left;
}

.video-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #9499a0;
}

.uploader-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.uploader-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #9499a0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #00aeec;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 12px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #9499a0;
}

.no-results i {
  font-size: 48px;
  margin-bottom: 12px;
  display: block;
}

.user-results {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 16px;
  color: #18191c;
}

.user-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.user-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 8px;
  padding: 20px 24px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}

.user-card-left {
  margin-right: 20px;
}

.user-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
}

.user-card-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.user-header {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}

.username {
  font-weight: bold;
  font-size: 18px;
  margin-right: 8px;
}

.user-level {
  background: #ff4d4f;
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
  padding: 2px 6px;
  margin-left: 2px;
}

.user-meta-desc {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #9499a0;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  gap: 4px;
}

.user-desc {
  color: #61666d;

  min-width: 0;
}

.user-card-action {
  margin-left: 24px;
}

.follow-btn {
  align-self: flex-start;
  margin-top: 10px;
  background: #00aeec;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 4px 14px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.follow-btn:hover {
  background: #0096c7;
}

.follow-btn.followed {
  background: #e5e5e5;
  color: #aaa;
  border: 1px solid #e5e5e5;
}

.mb-4 {
  margin-bottom: 1.5rem;
}
</style>
