<template>
  <NavBar>
    <Home></Home>
  </NavBar>
  <router-view />
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
<script>
import "bootstrap/dist/js/bootstrap";
import "bootstrap/dist/css/bootstrap.css";
import { Home } from "@icon-park/vue-next";

import NavBar from "./components/NavBar.vue";
export default {
  name: "App",
  components: {
    NavBar,
    Home,
  },
};
</script>
