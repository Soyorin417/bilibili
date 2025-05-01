<template>
  <div>
    <NavBar />
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <TopNav />
        <CarouselWithVideos
          :carousel-items="carouselItems"
          :featured-videos="featuredVideos"
        />
        <VideoGrid :videos="videos" />
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/navBar/NavBar.vue";
import TopNav from "@/components/navBar/TopNav.vue";
import CarouselWithVideos from "@/components/cartoonView/CarouselWithVideos.vue";
import VideoGrid from "@/components/cartoonView/VideoGrid.vue";
import { carouselItems, featuredVideos, videos } from "@/data/testData";

export default {
  name: "NewsView",
  components: {
    NavBar,
    TopNav,
    CarouselWithVideos,
    VideoGrid,
  },
  data() {
    return {
      carouselItems,
      featuredVideos,
      videos,
    };
  },
};
</script>

<style scoped>
.page {
  max-width: 1700px;
  margin: 0 auto;
}
</style>
