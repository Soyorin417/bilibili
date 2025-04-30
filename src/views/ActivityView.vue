<template>
  <div class="activity-page">
    <VideoBar />
    <div class="activity-container">
      <!-- 左侧用户信息 -->
      <div class="left-sidebar">
        <div class="user-info card">
          <div class="user-header d-flex">
            <div class="user-avatar">
              <img :src="userInfo.avatar" alt="用户头像" />
            </div>
            <div class="user-name ms-2 mb-4">
              <div>
                {{ userInfo.name }}
              </div>
              <div>
                <span class="user-level">LV{{ userInfo.level }}</span>
              </div>
            </div>
          </div>
          <div class="user-stats">
            <div class="stat-item">
              <div class="stat-value">2</div>
              <div class="stat-label">关注</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">13</div>
              <div class="stat-label">粉丝</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">48</div>
              <div class="stat-label">动态</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间动态列表 -->
      <div class="main-content">
        <!-- 发布动态区域 -->
        <div class="post-box card">
          <textarea
            class="post-input"
            placeholder="好的标题更容易获得支持，选填20字"
            v-model="postContent"
          ></textarea>
          <div class="post-tools">
            <div class="tool-icons">
              <div class="emoji-picker-container">
                <button class="tool-btn" @click="toggleEmojiPicker">
                  <i class="bi bi-emoji-smile"></i>
                </button>
                <EmojiPicker
                  v-if="showEmojiPicker"
                  @select="onSelectEmoji"
                  :style="{ position: 'absolute', 'z-index': 1000, top: '100%', left: 0 }"
                />
              </div>
              <button class="tool-btn" @click="triggerFileInput">
                <i class="bi bi-image"></i>
              </button>
              <input
                type="file"
                ref="fileInput"
                accept="image/*"
                @change="handleImageUpload"
                style="display: none"
              />
              <button class="tool-btn"><i class="bi bi-at"></i></button>
              <button class="tool-btn"><i class="bi bi-bar-chart"></i></button>
              <button class="tool-btn"><i class="bi bi-hash"></i></button>
            </div>
            <div class="post-actions">
              <span class="word-count">{{ postContent.length }}/200</span>
              <button class="publish-btn" @click="sendPost">发布</button>
            </div>
          </div>
          <div v-if="selectedImage" class="selected-image">
            <img :src="selectedImage" alt="Selected" />
            <button class="remove-image" @click="removeImage">
              <i class="bi bi-x"></i>
            </button>
          </div>
        </div>

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
            @delete="handleDelete"
            @comments="handleComments"
          />
        </div>
      </div>

      <!-- 右侧话题列表 -->
      <div class="right-sidebar">
        <div class="topics-card card">
          <div class="card-header">
            <h3>话题</h3>
          </div>
          <div class="topics-list">
            <div class="relevant-topic" v-for="topic in topics" :key="topic.id">
              <svg
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
                class="relevant-topic__prefix"
              >
                <path
                  fill-rule="evenodd"
                  clip-rule="evenodd"
                  d="M11.4302 2.57458C11.4416 2.51023 11.4439 2.43974 11.4218 2.3528C11.3281 1.98196 10.9517 1.72037 10.5284 1.7527C10.432 1.76018 10.3599 1.78383 10.297 1.81376C10.2347 1.84398 10.1832 1.88155 10.1401 1.92465C10.1195 1.94485 10.1017 1.96692 10.0839 1.98897L10.0808 1.99289L10.0237 2.06277L9.91103 2.2033C9.76177 2.39141 9.61593 2.58191 9.47513 2.77556C9.33433 2.96936 9.19744 3.16585 9.06672 3.36638C9.00275 3.46491 8.93968 3.56401 8.87883 3.66461L8.56966 3.6613C8.00282 3.6574 7.43605 3.65952 6.86935 3.67034C6.80747 3.56778 6.74325 3.46677 6.67818 3.3664C6.54732 3.16585 6.41045 2.96934 6.26968 2.77568C6.12891 2.58186 5.98309 2.39134 5.83387 2.20322L5.72122 2.06268L5.66416 1.99279L5.6622 1.99036C5.64401 1.96783 5.62586 1.94535 5.60483 1.92454C5.56192 1.88144 5.51022 1.84388 5.44797 1.81364C5.38522 1.78386 5.31305 1.76006 5.21665 1.75273C4.80555 1.72085 4.4203 1.97094 4.32341 2.35273C4.30147 2.43968 4.30358 2.51018 4.31512 2.57453C4.32715 2.63859 4.34975 2.69546 4.38112 2.74649C4.39567 2.77075 4.41283 2.79315 4.42999 2.81557C4.43104 2.81694 4.43209 2.81831 4.43314 2.81968L4.48759 2.89122L4.59781 3.03355C4.74589 3.22242 4.89739 3.40905 5.05377 3.59254C5.09243 3.63788 5.13136 3.68306 5.17057 3.72785C4.99083 3.73681 4.81112 3.7467 4.63143 3.75756C4.41278 3.771 4.19397 3.78537 3.97547 3.80206L3.64757 3.82786L3.48362 3.84177L3.39157 3.85181C3.36984 3.8543 3.34834 3.8577 3.32679 3.86111C3.31761 3.86257 3.30843 3.86402 3.29921 3.86541C3.05406 3.90681 2.81526 3.98901 2.59645 4.10752C2.37765 4.22603 2.17867 4.38039 2.00992 4.56302C1.84117 4.74565 1.70247 4.95593 1.60144 5.18337C1.50025 5.4105 1.43687 5.65447 1.41362 5.90153C1.33103 6.77513 1.27663 7.6515 1.25742 8.5302C1.23758 9.40951 1.25835 10.2891 1.3098 11.1655C1.32266 11.3846 1.33738 11.6035 1.35396 11.8223L1.38046 12.1505L1.39472 12.3144L1.39658 12.335L1.39906 12.3583L1.40417 12.4048C1.40671 12.4305 1.41072 12.4558 1.41473 12.4811C1.41561 12.4866 1.41648 12.4922 1.41734 12.4977C1.45717 12.7449 1.53806 12.9859 1.65567 13.2074C1.77314 13.4289 1.92779 13.6304 2.11049 13.8022C2.29319 13.974 2.50441 14.1159 2.73329 14.2197C2.96201 14.3235 3.2084 14.3901 3.45836 14.4135C3.47066 14.415 3.48114 14.4159 3.49135 14.4167C3.49477 14.417 3.49817 14.4173 3.50159 14.4176L3.5425 14.4212L3.62448 14.4283L3.78843 14.4417L4.11633 14.4674C4.33514 14.4831 4.55379 14.4983 4.7726 14.5111C6.52291 14.6145 8.27492 14.6346 10.0263 14.5706C10.4642 14.5547 10.9019 14.5332 11.3396 14.5062C11.5584 14.4923 11.7772 14.4776 11.9959 14.4604L12.3239 14.434L12.4881 14.4196L12.5813 14.4093C12.6035 14.4065 12.6255 14.403 12.6474 14.3995C12.6565 14.3981 12.6655 14.3966 12.6746 14.3952C12.9226 14.3527 13.1635 14.2691 13.3844 14.1486C13.6052 14.0284 13.8059 13.8716 13.9759 13.6868C14.1463 13.5022 14.2861 13.2892 14.3874 13.0593C14.4381 12.9444 14.4793 12.8253 14.5108 12.7037C14.519 12.6734 14.5257 12.6428 14.5322 12.612L14.5421 12.566L14.55 12.5196C14.5556 12.4887 14.5607 12.4578 14.5641 12.4266C14.5681 12.3959 14.5723 12.363 14.5746 12.3373C14.6642 11.4637 14.7237 10.5864 14.7435 9.70617C14.764 8.825 14.7347 7.94337 14.6719 7.06715C14.6561 6.8479 14.6385 6.62896 14.6183 6.41033L14.5867 6.08246L14.5697 5.91853L14.5655 5.87758C14.5641 5.86445 14.5618 5.8473 14.5599 5.83231C14.5588 5.8242 14.5578 5.81609 14.5567 5.80797C14.5538 5.78514 14.5509 5.76229 14.5466 5.7396C14.5064 5.49301 14.4252 5.25275 14.3067 5.03242C14.1886 4.81208 14.0343 4.61153 13.8519 4.44095C13.6695 4.27038 13.4589 4.12993 13.2311 4.02733C13.0033 3.92458 12.7583 3.85907 12.5099 3.83636C12.4974 3.83492 12.4865 3.83394 12.4759 3.833C12.4729 3.83273 12.4698 3.83246 12.4668 3.83219L12.4258 3.82879L12.3438 3.82199L12.1798 3.80886L11.8516 3.78413C11.633 3.76915 11.4143 3.75478 11.1955 3.74288C10.993 3.73147 10.7904 3.72134 10.5878 3.71243L10.6914 3.59236C10.8479 3.40903 10.9992 3.22242 11.1473 3.03341L11.2576 2.89124L11.312 2.81971C11.3136 2.81773 11.3151 2.81575 11.3166 2.81377C11.3333 2.79197 11.3501 2.77013 11.3641 2.74653C11.3954 2.6955 11.418 2.63863 11.4302 2.57458ZM9.33039 5.49268C9.38381 5.16945 9.67705 4.95281 9.98536 5.00882L9.98871 5.00944C10.2991 5.06783 10.5063 5.37802 10.4524 5.70377L10.2398 6.99039L11.3846 6.9904C11.7245 6.9904 12 7.27925 12 7.63557C12 7.99188 11.7245 8.28073 11.3846 8.28073L10.0266 8.28059L9.7707 9.82911L11.0154 9.82913C11.3553 9.82913 11.6308 10.118 11.6308 10.4743C11.6308 10.8306 11.3553 11.1195 11.0154 11.1195L9.55737 11.1195L9.32807 12.5073C9.27465 12.8306 8.98141 13.0472 8.6731 12.9912L8.66975 12.9906C8.35937 12.9322 8.1522 12.622 8.20604 12.2962L8.40041 11.1195H6.89891L6.66961 12.5073C6.61619 12.8306 6.32295 13.0472 6.01464 12.9912L6.01129 12.9906C5.7009 12.9322 5.49374 12.622 5.54758 12.2962L5.74196 11.1195L4.61538 11.1195C4.27552 11.1195 4 10.8306 4 10.4743C4 10.118 4.27552 9.82913 4.61538 9.82913L5.95514 9.82911L6.21103 8.28059L4.98462 8.28073C4.64475 8.28073 4.36923 7.99188 4.36923 7.63557C4.36923 7.27925 4.64475 6.9904 4.98462 6.9904L6.42421 6.99039L6.67193 5.49268C6.72535 5.16945 7.01859 4.95281 7.3269 5.00882L7.33025 5.00944C7.64063 5.06783 7.8478 5.37802 7.79396 5.70377L7.58132 6.99039H9.08281L9.33039 5.49268ZM8.61374 9.82911L8.86963 8.28059H7.36813L7.11225 9.82911H8.61374Z"
                  fill="#00aeec"
                ></path>
              </svg>
              <div class="relevant-topic__title bili-ellipsis">
                {{ topic.title }}
              </div>
              <div class="relevant-topic__exposed">
                <span class="bili-ellipsis"
                  >{{ topic.views }}浏览 · {{ topic.comments }}讨论</span
                >
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
import ActivityCard from "@/components/ActivityCard.vue";
import userData from "@/data/userData.js";
import EmojiPicker from "vue3-emoji-picker";
import "vue3-emoji-picker/css";

export default {
  name: "ActivityView",
  components: {
    VideoBar,
    ActivityCard,
    EmojiPicker,
  },
  data() {
    return {
      userInfo: userData,
      postContent: "",
      selectedImage: null,
      showEmojiPicker: false,
      posts: [
        {
          id: 1,
          user_id: 1,
          author: userData.name,
          avatar: userData.avatar,
          time: "2小时前",
          content: "这是一个视频标题",
          media:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          duration: "5:20",
          is_image: true,
          views: 114,
          comments: 23,
          likes: 45,
          liked: false,
          shared: false,
        },
        {
          id: 2,
          user_id: 2,
          author: userData.name,
          avatar: userData.avatar,
          time: "1小时前",
          content: "这是一个视频标题",
          media:
            "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
          duration: "5:20",
          is_image: true,
          views: 114,
          comments: 23,
          likes: 45,
          liked: false,
          shared: false,
        },
      ],
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
    handleLike({ id, liked, likes }) {
      const post = this.posts.find((p) => p.id === id);
      if (post) {
        post.liked = liked;
        post.likes = likes;
      }
    },
    handleShare({ id, shared }) {
      const post = this.posts.find((p) => p.id === id);
      if (post) {
        post.shared = shared;
      }
    },
    handleDelete(id) {
      const index = this.posts.findIndex((p) => p.id === id);
      if (index !== -1) {
        this.posts.splice(index, 1);
      }
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },

    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.selectedImage = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },

    removeImage() {
      this.selectedImage = null;
      this.$refs.fileInput.value = "";
    },

    sendPost() {
      if (!this.postContent.trim() && !this.selectedImage) return;

      this.posts.unshift({
        id: this.posts.length + 1,
        user_id: this.userInfo.id,
        author: this.userInfo.name,
        avatar: this.userInfo.avatar,
        time: "刚刚",
        content: this.postContent,
        media:
          this.selectedImage ||
          "http://113.45.69.13:9000/image/35d43d37454c820e9d994445e8edd348a0a2233c.jpg",
        duration: "5:20",
        is_image: !!this.selectedImage,
        views: 0,
        comments: 0,
        likes: 0,
        liked: false,
        shared: false,
      });

      this.postContent = "";
      this.removeImage();
    },

    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker;
    },

    onSelectEmoji(emoji) {
      this.postContent += emoji.i;
      this.showEmojiPicker = false;
    },
  },
  mounted() {
    // 点击页面其他地方时关闭 emoji 选择器
    document.addEventListener("click", (e) => {
      if (!e.target.closest(".emoji-picker-container")) {
        this.showEmojiPicker = false;
      }
    });
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

.left-sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.user-info {
  padding: 16px;
}

.user-header {
  text-align: center;
  margin-bottom: 16px;
  height: 50px;
}

.user-avatar {
  margin-bottom: 12px;
}

.user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 2px solid #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #18191c;
  margin-bottom: 4px;

  align-items: center;
  justify-content: center;
  gap: 8px;
}

.user-level {
  font-size: 10px;
  color: #fff;
  background: linear-gradient(90deg, #ff6699 0%, #ff9966 100%);
  padding: 1px 1px;

  font-weight: lighter;
}

.user-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-top: 1px solid #f0f1f2;
  padding-top: 16px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 16px;
  font-weight: 600;
  color: #18191c;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #61666d;
}

.right-sidebar {
  position: sticky;
  top: 20px;
  height: fit-content;
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 16px;
  margin-bottom: 16px;
}

.post-box {
  background: #fff;
}

.post-input {
  width: 100%;
  border: none;
  resize: none;
  padding: 12px;
  font-size: 14px;
  height: 80px;
  outline: none;
}

.post-tools {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  position: relative;
  z-index: 100;
}

.tool-icons {
  display: flex;
  gap: 12px;
}

.tool-btn {
  border: none;
  background: none;
  color: #757575;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
}

.tool-btn:hover {
  background: #f5f5f5;
}

.post-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.word-count {
  color: #999;
  font-size: 14px;
}

.publish-btn {
  background: #00aeec;
  color: #fff;
  border: none;
  padding: 8px 24px;
  border-radius: 4px;
  cursor: pointer;
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

.post-card {
  margin-bottom: 16px;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  margin-right: 12px;
}

.post-info {
  flex: 1;
}

.post-author {
  font-weight: 500;
  margin-bottom: 4px;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-more {
  color: #999;
  cursor: pointer;
  padding: 4px;
}

.post-content {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.6;
}

.post-media img {
  max-width: 100%;
  border-radius: 8px;
  margin-bottom: 12px;
}

.post-actions {
  display: flex;
  gap: 32px;
  color: #757575;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
  font-size: 14px;
}

.topics-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.card-header {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 500;
  color: #18191c;
  margin: 0;
}

.topics-list {
  padding: 0 16px;
}

.relevant-topic {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.relevant-topic:last-child {
  border-bottom: none;
}

.relevant-topic__prefix {
  flex-shrink: 0;
  margin-right: 8px;
}

.relevant-topic__title {
  flex: 1;
  font-size: 14px;
  color: #18191c;
  margin-right: 8px;
}

.relevant-topic__exposed {
  font-size: 12px;
  color: #9499a0;
  white-space: nowrap;
}

.bili-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.relevant-topic:hover .relevant-topic__title {
  color: #00aeec;
}

.selected-image {
  position: relative;
  margin-top: 12px;
  width: 160px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
}

.selected-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  border: none;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
}

.remove-image i {
  font-size: 12px;
}

.tool-btn {
  position: relative;
  overflow: visible;
}

.emoji-picker-container {
  position: relative;
  display: inline-block;
}

.post-tools {
  position: relative;
  z-index: 100;
}

/* 自定义 emoji picker 样式 */
:deep(.v3-emoji-picker) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  border: none;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search) {
  padding: 8px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search input) {
  border: 1px solid #e5e5e5;
  border-radius: 4px;
  padding: 6px 12px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-search input:focus) {
  border-color: #00aeec;
  outline: none;
}

:deep(.v3-emoji-picker .v3-emoji-picker-category-title) {
  font-size: 12px;
  color: #666;
  padding: 4px 8px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-emoji) {
  cursor: pointer;
  padding: 4px;
}

:deep(.v3-emoji-picker .v3-emoji-picker-emoji:hover) {
  background: rgba(0, 174, 236, 0.1);
  border-radius: 4px;
}
</style>
