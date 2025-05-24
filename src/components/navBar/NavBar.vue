<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid mt-1 position-absolute top-0 start-0">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link" href="#"><i class="bi bi-tv me-2"></i>首页</a>
        </li>
        <li class="nav-item" v-for="i in tags" :key="i">
          <router-link class="nav-link" :to="i[1]">{{ i[0] }}</router-link>
        </li>

        <li class="nav-item">
          <a class="nav-link" href="#"
            ><download
              theme="outline"
              size="20"
              fill="#ffffff"
              class="me-1"
            />下载客户端</a
          >
        </li>
      </ul>

      <form role="search" class="search-form">
        <div class="search-container">
          <div class="search-wrapper" :class="{ 'is-focused': isSearchFocused }">
            <input
              type="text"
              class="search-input"
              placeholder="哪吒票房是刷的 小明创造"
              v-model="searchQuery"
              @focus="handleSearchFocus"
              @blur="handleSearchBlur"
            />
            <button class="search-button" @click="handleSearch">
              <i class="bi bi-search"></i>
            </button>
          </div>

          <!-- 搜索下拉菜单 -->
          <div class="search-dropdown" v-if="isSearchFocused">
            <div class="search-history">
              <div class="history-header">
                <span>搜索历史</span>
                <span class="clear-history" @click="clearHistory">清空</span>
              </div>
              <div class="history-tags">
                <span
                  v-for="(tag, index) in historyTags"
                  :key="`history-${index}`"
                  class="history-tag"
                  @click="setSearchQuery(tag)"
                >
                  {{ tag }}
                </span>
              </div>
            </div>

            <div class="hot-search">
              <div class="hot-search-header">
                <span>bilibili热搜</span>
              </div>
              <div class="hot-search-list">
                <div class="row">
                  <div
                    class="col-6"
                    v-for="(column, colIndex) in hotTagsColumns"
                    :key="`col-${colIndex}`"
                  >
                    <div
                      v-for="(item, index) in column"
                      :key="`hot-${colIndex}-${index}`"
                      class="hot-search-item"
                      @click="setSearchQuery(item.message)"
                    >
                      <span
                        class="hot-number"
                        :class="{ 'top-rank': isTopRank(colIndex, index) }"
                      >
                        {{ item.num }}
                      </span>
                      <span class="hot-title">{{ item.message }}</span>
                      <span
                        v-if="item.icon"
                        class="hot-tag"
                        :style="item.background_color"
                      >
                        {{ item.icon }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </form>

      <ul class="navbar-nav ms-auto me-2">
        <li class="nav-item dropdown">
          <a
            class="nav-link"
            href="#"
            role="button"
            aria-expanded="false"
            :class="{ avatar: isAvatarVisible }"
          >
            <img :src="avatar" alt="用户头像" class="avart-img" v-if="isAvatarVisible" />
          </a>

          <div
            class="dropdown-menu card"
            style="padding: 0; border: none; box-shadow: none"
          >
            <UserProfileCardMini :user="userInfo" :is-login="isLogin" @logout="logout" />
          </div>
        </li>
        <li class="nav-item dropdown">
          <router-link :to="right_tags[1]" class="custom-link"
            ><div class="nav-link">
              <vip-one theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
                ><span class="visually-hidden">unread messages</span></span
              >
            </div>
            <div class="nav-text">大会员</div>
          </router-link>

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <router-link :to="right_tags[2]" class="custom-link"
            ><div class="nav-link">
              <message-one theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-80 translate-middle badge rounded-pill bg-danger"
                >9<span class="visually-hidden">unread messages</span>
              </span>
            </div>
            <div class="nav-text">消息</div></router-link
          >

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <router-link :to="right_tags[3]" class="custom-link"
            ><div class="nav-link">
              <windmill-two theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
                ><span class="visually-hidden">unread messages</span></span
              >
            </div>
            <div class="nav-text">动态</div></router-link
          >

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <router-link :to="right_tags[4]" class="custom-link"
            ><div class="nav-link">
              <star theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
                ><span class="visually-hidden">unread messages</span></span
              >
            </div>
            <div class="nav-text">收藏</div></router-link
          >

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <router-link :to="right_tags[5]" class="custom-link"
            ><div class="nav-link">
              <alarm-clock theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
                ><span class="visually-hidden">unread messages</span></span
              >
            </div>
            <div class="nav-text">历史</div></router-link
          >

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>

        <li class="nav-item">
          <router-link :to="right_tags[6]" class="custom-link"
            ><div class="nav-link">
              <Tips theme="outline" size="20" fill="#ffffff" />
              <span
                class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
                ><span class="visually-hidden">unread messages</span></span
              >
            </div>
            <div class="nav-text">创作中心</div></router-link
          >
        </li>
        <li class="nav-item mt-1 me-1">
          <button type="submit" class="btn btn-primary ms-3" @click="handleUpload">
            <upload theme="outline" size="20" fill="#ffffff" />
            <small class="ms-1">投稿</small>
          </button>
        </li>
        <li class="nav-item">
          <router-link
            v-if="userInfo && userInfo.role === 'admin'"
            to="/admin"
            class="btn btn-danger ms-2 mt-1"
            style="color: #fff; font-weight: bold"
            >后台管理</router-link
          >
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { Download } from "@icon-park/vue-next";
import { MessageOne } from "@icon-park/vue-next";
import { AlarmClock } from "@icon-park/vue-next";
import { WindmillTwo } from "@icon-park/vue-next";
import { VipOne } from "@icon-park/vue-next";
import { Star } from "@icon-park/vue-next";
import { Tips } from "@icon-park/vue-next";
import { Upload } from "@icon-park/vue-next";
import UserProfileCardMini from "@/components/user/UserProfileCardMini.vue";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "NavBar",
  components: {
    Download,
    MessageOne,
    AlarmClock,
    WindmillTwo,
    VipOne,
    Star,
    Tips,
    Upload,
    UserProfileCardMini,
  },
  data() {
    return {
      tags: [
        ["番剧", "/Anime"],
        ["直播", "/Broadcast"],
        ["游戏中心", "/GameCenter"],
        ["会员购", "/Purchase"],
        ["漫画", "/Caricature"],
        ["赛事", "/Events"],
        ["PDD", "/PDD"],
      ],
      right_tags: [
        "user",
        "vip",
        "message",
        "activity",
        "collection",
        "history",
        "create",
      ],
      history_tags: [
        "bilibili icon",
        "icon 图标",
        "明日方舟",
        "妈妈妈妈你看有花",
        "gptsovits",
        "viggleai使用教程",
        "回答我",
        "影色舞",
        "mygo",
        "NEXT TO YOU",
      ],
      hotTags: [
        [
          {
            num: 1,
            message: "法院禁止newjeans独立",
            icon: "热",
            background_color: "background-color:#f85a54",
          },
          {
            num: 3,
            message: "哪吒2延长上映至4月30日",
            icon: "热",
            background_color: "background-color:#f85a54",
          },
          {
            num: 5,
            message: "毛利兰 回答我",
            icon: "梗",
            background_color: "background-color:#ff6699",
          },
          { num: 7, message: "汉密尔顿王者归来", icon: "null", background_color: "" },
          {
            num: 9,
            message: "张郃得了mvp",
            icon: "独家",
            background_color: "background-color:#ff6699",
          },
        ],

        [
          {
            num: 2,
            message: "月薪多少吃得起火锅",
            icon: "新",
            background_color: "background-color:#ffbc4a",
          },
          {
            num: 4,
            message: "刺客信条影登顶全球销量榜",
            icon: "新",
            background_color: "background-color:#ffbc4a",
          },
          {
            num: 6,
            message: "泰国前总理他信重游唐人街",
            icon: "null",
            background_color: "",
          },
          {
            num: 8,
            message: "金秀贤事件法律争议",
            icon: "直播中",
            background_color: "background-color:#ff6699",
          },
          {
            num: 10,
            message: "跨越60年的爱情",
            icon: "新",
            background_color: "background-color:#ffbc4a",
          },
        ],
      ],
      searchQuery: "",
      isCardVisible: false,
      isAvatarVisible: true,
      isSearchFocused: false,
      historyTags: [
        "缘之空",
        "bilibili icon",
        "icon图标",
        "明日方舟",
        "妈妈妈妈你看我有",
      ],
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      return this.userInfo; // 这里返回的是 Vuex 中存储的 userInfo
    },
    avatar() {
      return this.user.avatar; // 返回头像
    },
    hotTagsColumns() {
      const columns = [[], []];
      this.hotTags.forEach((column, index) => {
        columns[index] = column;
      });
      return columns;
    },
  },

  methods: {
    ...mapActions("user", ["logout"]),
    handleUpload() {
      this.$router.push({
        path: "/upload",
      });
    },
    toggleCard() {
      this.isCardVisible = !this.isCardVisible;
      console.log(this.isCardVisible);
    },
    hideAvatar() {
      this.isAvatarVisible = !this.isAvatarVisible;
    },
    handleSearchFocus() {
      this.isSearchFocused = true;
    },
    handleSearchBlur() {
      setTimeout(() => {
        this.isSearchFocused = false;
      }, 200);
    },
    handleSearch() {
      if (this.searchQuery.trim()) {
        if (!this.historyTags.includes(this.searchQuery)) {
          this.historyTags.unshift(this.searchQuery);
          if (this.historyTags.length > 10) {
            this.historyTags.pop();
          }
        }
        this.$router.push({
          path: "/search",
          query: { keyword: this.searchQuery },
        });
      }
    },
    clearHistory() {
      this.historyTags = [];
    },
    setSearchQuery(query) {
      this.searchQuery = query;
      this.handleSearch();
    },
    isTopRank(colIndex, index) {
      const position = colIndex * 5 + index;
      return position < 3;
    },
  },
};
</script>

<style scoped>
.navbar {
  padding: 0.5rem 1rem;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 180px;
  background-image: url("http://113.45.69.13:9000/image/4da345730163c365225ed6c489a069a89a82cdd7.png");
}

.nav-link {
  margin: 0 0.5rem;
  font-weight: 500;
  font-size: small;
  color: white;
  display: inline-block;
  transition: transform 0.3s ease;
}

.custom-search {
  width: 500px;
  max-width: 100%;
}
.nav-text {
  color: white;
  font-size: small;
}
.avart-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
}
.avart-card {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}

.nav-link:hover {
  transform: translateY(-5px);
  color: white;
}

.btn-outline-success {
  margin-left: 1rem;
}

.nav-item.dropdown:hover .dropdown-menu {
  display: block;
  opacity: 1;
  visibility: visible;
}

.input-group.dropdown-toggle:focus-within .dropdown-menu {
  display: block;
  opacity: 1;
  visibility: visible;
}

.dropdown-menu {
  display: none;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s ease, visibility 0.3s ease;
}

.custom-search {
  border-right: none;
  border-radius: 4px 0 0 4px;
}

.search-menu {
  width: 545px;
  max-width: 100%;
}

.input-group-text {
  border-radius: 0 4px 4px 0;
  cursor: pointer;
}

.historyTag {
  height: 20px;
  width: 60px;
  font-size: small;
}
.hotTag {
  font-size: small;
}
.tag-icon {
  color: white;
  border-radius: 20%;
  font-size: smaller;
}
.tag-icon:hover {
  background-color: #e3e5e7;
}

.custom-link {
  text-decoration: none;
  color: inherit;
}

.search-form {
  position: relative;
  margin: 0 auto;
}

.search-container {
  position: relative;
  width: 500px;
  z-index: 100;
  margin: 0 20px;
}

.search-wrapper {
  display: flex;
  align-items: center;
  border: 1px solid #e3e5e7;
  border-radius: 8px;
  padding: 0 0.5rem;
  height: 36px;
  background-color: #f1f2f3;
  transition: all 0.3s ease;
}

.search-wrapper.is-focused {
  border-color: var(--bs-primary, #0d6efd);
  background-color: #fff;
  box-shadow: 0 0 5px rgba(13, 110, 253, 0.2);
}

.search-input {
  flex-grow: 1;
  border: none;
  background-color: transparent;
  padding: 0.5rem;
  font-size: 0.9rem;
  outline: none;
  color: #333;
}

.search-input::placeholder {
  color: #999;
  font-size: 0.9rem;
}

.search-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.search-button:hover {
  color: var(--bs-primary, #0d6efd);
}

.search-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  margin-top: 0.5rem;
  z-index: 1000;
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #e3e5e7;
  animation: dropdownFade 0.2s ease-in-out;
}

@keyframes dropdownFade {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.search-history,
.hot-search {
  padding: 1rem;
}

.hot-search {
  border-top: 1px solid #e3e5e7;
}

.history-header,
.hot-search-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.8rem;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
}

.clear-history {
  color: #0d6efd;
  cursor: pointer;
}

.clear-history:hover {
  text-decoration: underline;
}

.history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
}

.history-tag {
  background-color: #f6f7f8;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #666;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.history-tag:hover {
  background-color: #e3e5e7;
}

.hot-search-list {
  display: flex;
  flex-direction: column;
}

.hot-search-item {
  display: flex;
  align-items: center;
  padding: 0.5rem 0;
  cursor: pointer;
}

.hot-search-item:hover {
  background-color: #f6f7f8;
}

.hot-number {
  width: 20px;
  text-align: center;
  font-weight: 600;
  color: #999;
  margin-right: 0.8rem;
}

.hot-number.top-rank {
  color: #ff6699;
}

.hot-title {
  flex-grow: 1;
  font-size: 0.9rem;
}

.hot-tag {
  padding: 0.1rem 0.4rem;
  border-radius: 2px;
  font-size: 0.7rem;
  margin-left: 0.5rem;
  display: inline-block;
}
</style>
