<template>
  <div class="pending-videos">
    <div class="search-bar">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="搜索视频标题或描述..."
        class="form-control"
      />
    </div>
    <div class="video-list">
      <table class="table">
        <thead>
          <tr>
            <th>缩略图</th>
            <th>标题</th>
            <th>上传者</th>
            <th>上传时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="video in filteredVideos" :key="video.id">
            <td>
              <img :src="video.image" class="video-thumbnail" :alt="video.title" />
            </td>
            <td>{{ video.title }}</td>
            <td>{{ video.uploader }}</td>
            <td>{{ formatDate(video.uploadTime) }}</td>
            <td>
              <button class="btn btn-info btn-sm me-2" @click="handlePreview(video)">
                预览
              </button>
              <button
                class="btn btn-success btn-sm me-2"
                @click="handleApprove(video.id)"
              >
                通过
              </button>
              <button class="btn btn-danger btn-sm" @click="handleReject(video.id)">
                拒绝
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="videos.length === 0" class="text-muted text-center py-4">
        暂无待审核视频
      </div>
    </div>

    <!-- 视频预览弹窗 -->
    <div v-if="previewVisible" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <h5>{{ previewVideoData.title }}</h5>
          <video :src="previewVideoData.videoUrl" controls class="preview-video"></video>
          <p class="mt-3">{{ previewVideoData.description }}</p>
          <button class="btn btn-secondary mt-3" @click="closePreview">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PendingVideos",
  props: {
    videos: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      previewVisible: false,
      previewVideoData: null,
      searchQuery: "",
    };
  },
  computed: {
    filteredVideos() {
      return this.videos.filter(
        (video) =>
          video.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
          video.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
    handlePreview(video) {
      this.$emit("preview", video);
    },
    handleApprove(videoId) {
      this.$emit("approve", videoId);
    },
    handleReject(videoId) {
      this.$emit("reject", videoId);
    },
  },
};
</script>

<style scoped>
.video-thumbnail {
  width: 120px;
  height: 68px;
  object-fit: cover;
  border-radius: 4px;
}

.video-list {
  margin-top: 20px;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-wrapper {
  width: 100%;
  max-width: 700px;
}

.modal-container {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  text-align: center;
}

.preview-video {
  width: 100%;
  max-width: 600px;
  border-radius: 4px;
}
</style>
