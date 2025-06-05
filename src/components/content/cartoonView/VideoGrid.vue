<template>
  <div class="video-grid mt-4">
    <div class="row row-cols-5 g-4">
      <div v-for="(video, index) in videos" :key="index" class="col">
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
                {{ video.uploader }}
              </div>
              <div class="video-date">{{ video.date }}</div>
            </div>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "VideoGrid",
  props: {
    videos: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.video-grid {
  margin-top: 20px;
  text-align: left;
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
