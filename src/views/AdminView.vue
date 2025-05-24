<template>
  <div>
    <NavBar />
    <div class="admin-container">
      <h2>管理员后台</h2>
      <div class="admin-section">
        <h4>待审核视频</h4>
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>封面</th>
              <th>标题</th>
              <th>作者</th>
              <th>上传时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="video in pendingVideos" :key="video.id">
              <td>
                <img
                  :src="video.image"
                  style="width: 80px; height: 50px; object-fit: cover; border-radius: 6px"
                />
              </td>
              <td>{{ video.title }}</td>
              <td>{{ video.author }}</td>
              <td>{{ video.uploadTime }}</td>
              <td>
                <button class="btn btn-primary btn-sm me-2" @click="previewVideo(video)">
                  预览
                </button>
                <button
                  class="btn btn-success btn-sm me-2"
                  @click="reviewVideo(video.id, 'approved')"
                >
                  通过
                </button>
                <button
                  class="btn btn-danger btn-sm"
                  @click="reviewVideo(video.id, 'rejected')"
                >
                  拒绝
                </button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="pendingVideos.length === 0" class="text-muted">暂无待审核视频</div>
      </div>
    </div>
    <!-- 视频预览弹窗 -->
    <div v-if="previewVisible" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <h5>{{ previewVideoData.title }}</h5>
          <video
            :src="previewVideoData.videoUrl"
            controls
            style="width: 100%; max-width: 600px"
          ></video>
          <p>{{ previewVideoData.description }}</p>
          <button class="btn btn-secondary mt-2" @click="closePreview">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import NavBar from "@/components/navBar/NavBar.vue";
export default {
  name: "AdminView",
  components: { NavBar },
  data() {
    return {
      pendingVideos: [],
      previewVisible: false,
      previewVideoData: null,
    };
  },
  methods: {
    async fetchPendingVideos() {
      try {
        const res = await axios.get("http://localhost:8081/admin/videos/pending");
        this.pendingVideos = res.data || [];
      } catch (e) {
        this.pendingVideos = [];
        this.$message && this.$message.error("获取待审核视频失败");
      }
    },
    async reviewVideo(id, status) {
      try {
        await axios.post("http://localhost:8081/admin/videos/review", { id, status });
        this.$message.success("操作成功");
        this.fetchPendingVideos();
      } catch (e) {
        this.$message && this.$message.error("操作失败");
      }
    },
    previewVideo(video) {
      this.previewVideoData = video;
      this.previewVisible = true;
    },
    closePreview() {
      this.previewVisible = false;
      this.previewVideoData = null;
    },
  },
  mounted() {
    this.fetchPendingVideos();
  },
};
</script>

<style scoped>
.admin-container {
  max-width: 1600px;
  margin: 40px auto;
  padding: 32px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.06);
}
.admin-section {
  margin-top: 24px;
}
.table th,
.table td {
  vertical-align: middle;
  text-align: center;
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
</style>
