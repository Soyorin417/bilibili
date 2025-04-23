// 导入 Vue 核心库和创建应用的方法
import { createApp } from "vue";
// 导入根组件
import App from "./App.vue";
// 导入路由配置
import router from "./router";
// 导入状态管理配置
import store from "./store";
// 导入 Element Plus UI 组件库
import ElementPlus from "element-plus";
// 导入 Element Plus 样式
import "element-plus/dist/index.css";
// 导入 Element Plus 图标组件
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
// 导入 Bootstrap 图标样式
import "bootstrap-icons/font/bootstrap-icons.css";

// 创建 Vue 应用实例
const app = createApp(App);

// 注册所有 Element Plus 图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

// 使用状态管理
app.use(store);
// 使用路由
app.use(router);
// 使用 Element Plus UI 组件库
app.use(ElementPlus);
// 挂载应用到 DOM
app.mount("#app");
