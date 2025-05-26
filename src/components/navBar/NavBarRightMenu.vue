<template>
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

      <div class="dropdown-menu card" style="padding: 0; border: none; box-shadow: none">
        <UserProfileCardMini :user="userInfo" :is-login="isLogin" @logout="logout" />
      </div>
    </li>
    <li class="nav-item dropdown">
      <router-link :to="`/vip`" class="custom-link"
        ><div class="nav-link">
          <VipIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
            ><span class="visually-hidden">unread messages</span></span
          >
        </div>
        <div class="nav-text" :style="{ color: fill }">大会员</div>
      </router-link>
    </li>
    <li class="nav-item dropdown">
      <router-link :to="`/message`" class="custom-link"
        ><div class="nav-link">
          <MessageIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-80 translate-middle badge rounded-pill bg-danger"
            >9<span class="visually-hidden">unread messages</span>
          </span>
        </div>
        <div class="nav-text" :style="{ color: fill }">消息</div></router-link
      >

      <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="#">回复我的</a></li>
        <li><a class="dropdown-item" href="#">@我的</a></li>
        <li><a class="dropdown-item" href="#">收到的赞</a></li>
        <li><a class="dropdown-item" href="#">系统消息</a></li>
        <li><a class="dropdown-item" href="#">我的消息</a></li>
      </ul>
    </li>
    <li class="nav-item dropdown">
      <router-link :to="`/activity`" class="custom-link"
        ><div class="nav-link">
          <ActivityIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
            ><span class="visually-hidden">unread messages</span></span
          >
        </div>
        <div class="nav-text" :style="{ color: fill }">动态</div></router-link
      >

      <ul class="dropdown-menu dropdown-menu-end" style="width: 320px">
        <li>
          <div class="dropdown-center-content">
            <DynamicList :dynamics="navbarDynamics" />
          </div>
        </li>
      </ul>
    </li>
    <li class="nav-item dropdown">
      <router-link :to="`/collection`" class="custom-link"
        ><div class="nav-link">
          <CollectIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
            ><span class="visually-hidden">unread messages</span></span
          >
        </div>
        <div class="nav-text" :style="{ color: fill }">收藏</div></router-link
      >

      <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="#">个人资料</a></li>
        <li><a class="dropdown-item" href="#">设置</a></li>
        <li><a class="dropdown-item" href="#">退出登录</a></li>
      </ul>
    </li>
    <li class="nav-item dropdown">
      <router-link :to="`/watch-history`" class="custom-link"
        ><div class="nav-link">
          <HistoryIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
            ><span class="visually-hidden">unread messages</span></span
          >
        </div>
        <div class="nav-text" :style="{ color: fill }">历史</div></router-link
      >

      <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="#">个人资料</a></li>
        <li><a class="dropdown-item" href="#">设置</a></li>
        <li><a class="dropdown-item" href="#">退出登录</a></li>
      </ul>
    </li>

    <li class="nav-item">
      <router-link :to="`/Article`" class="custom-link"
        ><div class="nav-link">
          <CreateIcon :fill="fill" />
          <span
            class="position-absolute top-1 start-60 translate-middle badge border border-light rounded-circle bg-danger p-1"
            ><span class="visually-hidden">unread messages</span></span
          >
        </div>
        <div class="nav-text" :style="{ color: fill }">创作中心</div></router-link
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
</template>

<script>
import { Upload } from "@icon-park/vue-next";
import UserProfileCardMini from "@/components/user/UserProfileCardMini.vue";
import DynamicList from "@/components/dynamic/DynamicList.vue";
import { mapGetters, mapActions } from "vuex";
import VipIcon from "@/components/ui/VipIcon.vue";
import MessageIcon from "@/components/ui/MessageIcon.vue";
import CollectIcon from "@/components/ui/CollectIcon.vue";
import HistoryIcon from "@/components/ui/HistoryIcon.vue";
import CreateIcon from "@/components/ui/CreateIcon.vue";
import ActivityIcon from "@/components/ui/ActivityIcon.vue";
export default {
  name: "NavBarRightMenu",
  components: {
    Upload,
    UserProfileCardMini,
    DynamicList,
    VipIcon,
    MessageIcon,
    CollectIcon,
    HistoryIcon,
    CreateIcon,
    ActivityIcon,
  },
  props: {
    navbarDynamics: {
      type: Array,
      required: true,
    },
    fill: {
      type: String,
      default: "#000",
    },
  },
  data() {
    return {
      isAvatarVisible: true,
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      return this.userInfo;
    },
    avatar() {
      return this.user.avatar;
    },
  },
  methods: {
    ...mapActions("user", ["logout"]),
    handleLogout() {
      this.logout();
    },
    handleUpload() {
      this.$router.push({
        path: "/upload",
      });
    },
  },
};
</script>

<style scoped>
.nav-link {
  margin: 0 0.1rem;
  font-weight: 400;
  font-size: small;
  color: white;
  display: inline-block;
  transition: transform 0.3s ease;
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

.nav-link:hover {
  transform: translateY(-5px);
  color: white;
}

.nav-item.dropdown:hover .dropdown-menu {
  display: block;
  opacity: 1;
  visibility: visible;
}

.dropdown-menu {
  min-width: 180px;
  visibility: hidden;
  left: 50% !important;
  right: auto !important;
  transform: translateX(-50%);
  max-width: 90vw;
  display: block;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.custom-link {
  text-decoration: none;
  color: inherit;
}

.dropdown-item {
  font-size: 14px;
  text-align: center;
  color: #61666d;
}
</style>
