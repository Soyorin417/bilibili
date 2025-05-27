<template>
  <div>
    <router-view :key="token" />
  </div>
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

export default {
  name: "App",
};
</script>
