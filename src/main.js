import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "bootstrap-icons/font/bootstrap-icons.css";
import axios from 'axios'

const token = localStorage.getItem("token");

if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

  axios.get("http://127.0.0.1:8081/user")
    .then(res => {
      const userInfo = res.data;
      console.log("main.js 获取 userInfo:", userInfo);
      localStorage.setItem("userInfo", JSON.stringify(userInfo));

      // 如果使用 Vuex，可以提交 mutation 保存用户信息
      store.commit("setUserInfo", userInfo);
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
