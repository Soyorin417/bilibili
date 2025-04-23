<!-- 根组件模板 -->
<template>
  <div>
    <!-- 路由视图，用于显示当前路由对应的组件 -->
    <router-view />
  </div>
</template>

<!-- 全局样式 -->
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>

<!-- 组件脚本 -->
<script>
// 导入 Bootstrap 的 JavaScript 和 CSS
import "bootstrap/dist/js/bootstrap";
import "bootstrap/dist/css/bootstrap.css";

export default {
  // 组件名称
  name: "App",
};
</script>
