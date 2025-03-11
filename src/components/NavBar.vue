<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid position-absolute top-1 start-50 translate-middle">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <svg
            width="18"
            height="18"
            viewBox="0 0 18 18"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            class="zhuzhan-icon"
          >
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M3.73252 2.67094C3.33229 2.28484 3.33229 1.64373 3.73252 1.25764C4.11291 0.890684 4.71552 0.890684 5.09591 1.25764L7.21723 3.30403C7.27749 3.36218 7.32869 3.4261 7.37081 3.49407H10.5789C10.6211 3.4261 10.6723 3.36218 10.7325 3.30403L12.8538 1.25764C13.2342 0.890684 13.8368 0.890684 14.2172 1.25764C14.6175 1.64373 14.6175 2.28484 14.2172 2.67094L13.364 3.49407H14C16.2091 3.49407 18 5.28493 18 7.49407V12.9996C18 15.2087 16.2091 16.9996 14 16.9996H4C1.79086 16.9996 0 15.2087 0 12.9996V7.49406C0 5.28492 1.79086 3.49407 4 3.49407H4.58579L3.73252 2.67094ZM4 5.42343C2.89543 5.42343 2 6.31886 2 7.42343V13.0702C2 14.1748 2.89543 15.0702 4 15.0702H14C15.1046 15.0702 16 14.1748 16 13.0702V7.42343C16 6.31886 15.1046 5.42343 14 5.42343H4ZM5 9.31747C5 8.76519 5.44772 8.31747 6 8.31747C6.55228 8.31747 7 8.76519 7 9.31747V10.2115C7 10.7638 6.55228 11.2115 6 11.2115C5.44772 11.2115 5 10.7638 5 10.2115V9.31747ZM12 8.31747C11.4477 8.31747 11 8.76519 11 9.31747V10.2115C11 10.7638 11.4477 11.2115 12 11.2115C12.5523 11.2115 13 10.7638 13 10.2115V9.31747C13 8.76519 12.5523 8.31747 12 8.31747Z"
              fill="currentColor"
            ></path>
          </svg>
          <a class="nav-link" href="#">首页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">番剧</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">直播</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">游戏中心</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">会员购</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">漫画</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">赛事</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">PDD</a>
        </li>
        <li class="nav-item">
          <download theme="outline" size="20" fill="#ffffff" />
          <a class="nav-link" href="#">下载客户端</a>
        </li>
      </ul>

      <form class="d-flex" role="search">
        <input
          class="form-control me-2 custom-search"
          type="search"
          placeholder="小明剑魔我老爸得了mvp"
          aria-label="Search"
        />
      </form>

      <ul class="navbar-nav ms-auto">
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false"
            ><img src="../assets/lucy.png" alt="" />
          </a>

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false">
            <vip-one theme="outline" size="20" fill="#ffffff" />
          </a>
          <div class="nav-text">大会员</div>

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false">
            <message-one theme="outline" size="20" fill="#ffffff" /><span
              class="position-absolute top-0 start-100 translate-middle badge rounded-pill text-bg-secondary"
              >+99 <span class="visually-hidden">unread messages</span></span
            >
          </a>
          <div class="nav-text">消息</div>

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false">
            <windmill-two theme="outline" size="20" fill="#ffffff" />
          </a>
          <div class="nav-text">动态</div>

          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false">
            <star theme="outline" size="20" fill="#ffffff" />
          </a>
          <div class="nav-text">收藏</div>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" href="#" role="button" aria-expanded="false">
            <alarm-clock theme="outline" size="20" fill="#ffffff" />
          </a>
          <div class="nav-text">历史</div>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </li>

        <li class="nav-item">
          <a class="nav-link" href="#">
            <Tips theme="outline" size="20" fill="#ffffff" />
          </a>
          <div class="nav-text">创作中心</div>
        </li>
        <li class="nav-item">
          <button class="btn btn-outline-success" type="submit">投稿</button>
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
  },
};
</script>

<style scoped>
.navbar {
  padding: 0.5rem 1rem; /* 调整导航栏内边距 */
  background-size: cover; /* 背景图覆盖整个导航栏 */
  background-position: center; /* 背景图居中 */
  background-repeat: no-repeat; /* 背景图不重复 */
  height: 180px;
  background-image: url("../assets/background.webp");
}

.nav-link {
  margin: 0 0.5rem; /* 调整导航链接间距 */
  font-weight: 500; /* 加粗字体 */
  font-size: small;
  color: white;
  display: inline-block; /* 确保 transform 生效 */
  transition: transform 0.3s ease;
}

.custom-search {
  width: 500px; /* 设置固定宽度 */
  max-width: 100%; /* 确保在小屏幕上不会溢出 */
}
.nav-text {
  color: white;
  font-size: small;
}
.nav-item img {
  width: 40px; /* 设置图片宽度 */
  height: 40px; /* 设置图片高度 */
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ffffff;
}

.nav-link:hover {
  transform: translateY(-5px); /* 向上移动 5px */
  color: white;
}

.btn-outline-success {
  margin-left: 1rem; /* 调整按钮间距 */
}

.nav-item.dropdown:hover .dropdown-menu {
  display: block; /* 悬浮时显示下拉菜单 */
  opacity: 1;
  visibility: visible;
}

.dropdown-menu {
  display: none;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s ease, visibility 0.3s ease;
}
</style>
