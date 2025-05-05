import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "bootstrap-icons/font/bootstrap-icons.css";
import axios from 'axios'


const app = createApp(App);
const defaultAvatar = "http://113.45.69.13:9000/image/default-avatar.jpg";

// 恢复 token 和用户信息
const token = localStorage.getItem("token");
if (token) {
  const savedUserInfo = JSON.parse(localStorage.getItem("userInfo")) || {
    avatar: defaultAvatar,
    nickname: "未登录",
    level: 0,
    exp: 0,
    nextExp: 0,
    coin: 0,
    bcoin: 0,
    following: 0,
    fans: 0,
    dynamic: 0,
  };
  store.commit("user/SET_TOKEN", token);
  store.commit("user/SET_USER_INFO", savedUserInfo);
}



axios.defaults.baseURL = 'http://127.0.0.1:8081'


axios.interceptors.request.use(
  config => {

    const token = localStorage.getItem('token')
    if (token) {

      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}


app.use(store).use(router).use(ElementPlus).mount("#app");
