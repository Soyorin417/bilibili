<template>
  <div class="post-content">
    <div v-if="post.video && post.video.image">
      <div class="video-card-content" @click="$emit('video-click')">
        <div class="video-thumbnail">
          <img :src="post.video.image" alt="视频封面" />
          <span class="video-duration">{{ post.video.duration }}</span>
        </div>
        <div class="video-info">
          <div class="video-title">{{ post.content }}</div>
          <div class="video-stats-line">
            <span class="me-3">
              <i class="bi bi-play-fill"></i> {{ post.video.playCount }}
            </span>
            <span> <i class="bi bi-chat"></i> {{ post.video.commentCount }} </span>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="post-content-text">{{ post.content }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PostContent",
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.post-content {
  margin-bottom: 8px;
  padding-left: 44px;
}

.post-content-text {
  font-size: 14px;
  text-align: left;
}

.video-card-content {
  display: flex;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.video-card-content:hover {
  transform: translateY(-2px);
}

.video-thumbnail {
  flex-shrink: 0;
  width: 160px;
  height: 90px;
  border-radius: 4px;
  overflow: hidden;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-thumbnail:hover img {
  transform: scale(1.05);
}

.video-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: left;
  padding: 4px 0;
}

.video-title {
  font-size: 13px;
  color: #18191c;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-align: left;
  margin-bottom: 8px;
}

.video-stats-line {
  font-size: 12px;
  color: #9499a0;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
