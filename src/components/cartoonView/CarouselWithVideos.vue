<template>
  <div class="top-section d-flex gap-3">
    <!-- 轮播图部分 -->
    <div class="carousel-section mt-4">
      <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-indicators">
          <button
            v-for="(item, index) in carouselItems"
            :key="index"
            type="button"
            data-bs-target="#carouselExampleIndicators"
            :data-bs-slide-to="index"
            :class="{ active: index === 0 }"
            :aria-label="'Slide ' + (index + 1)"
          ></button>
        </div>
        <div class="carousel-inner">
          <div
            class="carousel-item"
            v-for="(item, index) in carouselItems"
            :key="index"
            :class="{ active: index === 0 }"
          >
            <img :src="item.image" class="d-block w-100 carousel-img" :alt="item.title" />
            <div class="carousel-caption">
              <h5>{{ item.title }}</h5>
            </div>
          </div>
        </div>
        <button
          class="carousel-control-prev"
          type="button"
          data-bs-target="#carouselExampleIndicators"
          data-bs-slide="prev"
        >
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Previous</span>
        </button>
        <button
          class="carousel-control-next"
          type="button"
          data-bs-target="#carouselExampleIndicators"
          data-bs-slide="next"
        >
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Next</span>
        </button>
      </div>
    </div>

    <!-- 右侧视频部分 -->
    <div class="featured-video-grid mt-4 w-100">
      <div class="row row-cols-3 g-4">
        <div v-for="(video, index) in featuredVideos" :key="index" class="col">
          <router-link :to="`/video/${video.id}`" class="video-link">
            <div class="video-card text-ellipsis">
              <div class="video-thumbnail">
                <img :src="video.cover" :alt="video.title" class="w-100" />
                <div class="video-duration">{{ video.duration }}</div>
                <div class="video-stats">
                  <span><i class="bi bi-play-fill"></i> {{ video.views }}</span>
                  <span><i class="bi bi-chat-fill"></i> {{ video.comments }}</span>
                </div>
              </div>
              <div class="video-info">
                <div class="video-title">{{ video.title }}</div>
                <div class="video-uploader">
                  <i class="bi bi-person-circle"></i>
                  {{ video.uploader || "UP主" }}
                </div>
                <div class="video-date">{{ video.date || "" }}</div>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CarouselWithVideos",
  props: {
    carouselItems: {
      type: Array,
      required: true,
    },
    featuredVideos: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.top-section {
  margin-bottom: 20px;
  text-align: left;
}

.carousel-section {
  width: 666px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.carousel-img {
  width: 100%;
  height: 220px;
  object-fit: cover;
  border-radius: 8px;
}

.featured-video-grid {
  max-width: 100%;
}

.video-link {
  text-decoration: none;
  color: inherit;
}

.video-card {
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s;
  background: white;
}

.video-card:hover {
  transform: translateY(-4px);
}

.video-thumbnail {
  position: relative;
  padding-top: 56.25%; /* 16:9 比例 */
}

.video-thumbnail img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 2px 4px;
  border-radius: 4px;
  font-size: 12px;
}

.video-stats {
  position: absolute;
  bottom: 8px;
  left: 8px;
  color: white;
  font-size: 12px;
}

.video-stats span {
  margin-right: 12px;
}

.video-info {
  padding: 8px;
}

.video-title {
  font-size: 14px;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.video-uploader {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.video-date {
  font-size: 12px;
  color: #999;
}

.text-ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
