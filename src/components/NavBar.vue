<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid position-absolute top-1 start-50 translate-middle">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <tv theme="outline" size="20" fill="#ffffff" />
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
          class="form-control me-2"
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
import { Tv } from "@icon-park/vue-next";
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
    Tv,
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
  background-image: url("../assets/lucy.png");
}

.nav-link {
  margin: 0 0.5rem; /* 调整导航链接间距 */
  font-weight: 500; /* 加粗字体 */
  font-size: small;
  color: white;
  display: inline-block; /* 确保 transform 生效 */
  transition: transform 0.3s ease;
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
