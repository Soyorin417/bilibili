import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "bootstrap-icons/font/bootstrap-icons.css";
import axios from 'axios'
import websocketClient from './utils/websocket'

const token = localStorage.getItem("token");

if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

  axios.get("http://127.0.0.1:8081/api/user/info")
    .then(res => {
      const userInfo = res.data;
      localStorage.setItem("userInfo", JSON.stringify(userInfo));
      store.commit("user/SET_USER_INFO", userInfo);

      websocketClient.connect();
    })
    .catch(err => {
      console.error("main.js 获取用户信息失败", err);
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
    });
}

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(store).use(router).use(ElementPlus).mount("#app");
