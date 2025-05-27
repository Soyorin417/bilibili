<template>
  <div class="banner-section">
    <div class="carousel-container">
      <div
        class="carousel-slide"
        v-for="(slide, index) in slides"
        :key="index"
        v-show="currentIndex === index"
      >
        <img :src="slide.image" class="banner-image" :alt="slide.title" />
        <div class="banner-title">
          <h1>{{ slide.title }}</h1>
          <p class="subtitle">{{ slide.subtitle }}</p>
        </div>
      </div>
    </div>

    <div class="thumbnail-nav">
      <div class="thumbnail-container">
        <div
          v-for="(slide, index) in slides"
          :key="index"
          class="thumbnail-item"
          :class="{ active: currentIndex === index }"
          @click="setCurrentSlide(index)"
        >
          <img :src="slide.thumbnail" :alt="slide.title" />
          <div class="thumbnail-title">{{ slide.title }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AnimeBanner",
  data() {
    return {
      currentIndex: 0,
      slides: [
        {
          title: "最强王者的第二人生",
          subtitle: "The Beginning After the End",
          image: "/path/to/banner1.jpg",
          thumbnail: "/path/to/thumb1.jpg",
        },
      ],
    };
  },
  methods: {
    setCurrentSlide(index) {
      this.currentIndex = index;
    },
    nextSlide() {
      this.currentIndex = (this.currentIndex + 1) % this.slides.length;
    },
    startAutoPlay() {
      setInterval(this.nextSlide, 5000); // Change slide every 5 seconds
    },
  },
  mounted() {
    this.startAutoPlay();
  },
};
</script>

<style scoped>
.banner-section {
  position: relative;
  width: 100%;
  height: 480px;
  margin-bottom: 20px;
}

.carousel-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 8px;
}

.carousel-slide {
  position: absolute;
  width: 100%;
  height: 100%;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-title {
  position: absolute;
  bottom: 80px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.banner-title h1 {
  font-size: 48px;
  margin-bottom: 10px;
}

.banner-title .subtitle {
  font-size: 24px;
}

.thumbnail-nav {
  position: absolute;
  bottom: 20px;
  left: 0;
  width: 100%;
  padding: 0 20px;
}

.thumbnail-container {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.thumbnail-item {
  width: 160px;
  height: 90px;
  position: relative;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.thumbnail-item.active {
  opacity: 1;
  border: 2px solid #00a1d6;
}

.thumbnail-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumbnail-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 4px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 12px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
