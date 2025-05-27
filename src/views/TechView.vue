<template>
  <div>
    <NavBar />
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <TopNav />

        <VideoGrid :videos="videos" />
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/navBar/NavBar.vue";
import TopNav from "@/components/navBar/TopNav.vue";

export default {
  name: "TechView",
  components: {
    NavBar,
    TopNav,
  },
};
</script>

<style scoped>
.page {
  max-width: 1700px;
  margin: 0 auto;
}
</style>
