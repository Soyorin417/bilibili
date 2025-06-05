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
import VideoGrid from "@/components/content/cartoonView/VideoGrid.vue";

export default {
  name: "VlogView",
  components: {
    NavBar,
    TopNav,
    VideoGrid,
  },
};
</script>

<style scoped>
.page {
  max-width: 1700px;
  margin: 0 auto;
}
</style>
