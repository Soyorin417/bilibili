<template>
  <div>
    <NavBar />
    <div class="admin-container">
      <h2>管理员后台</h2>

      <!-- 管理员导航栏 -->
      <div class="admin-nav">
        <button
          v-for="tab in tabs"
          :key="tab.id"
          :class="['nav-btn', { active: currentTab === tab.id }]"
          @click="currentTab = tab.id"
        >
          {{ tab.name }}
        </button>
      </div>

      <!-- 待审核视频 -->
      <div v-if="currentTab === 'pending'" class="admin-section">
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

      <!-- 用户管理 -->
      <div v-if="currentTab === 'users'" class="admin-section">
        <h4>用户管理</h4>
        <div class="search-bar mb-3">
          <input
            type="text"
            v-model="userSearchQuery"
            class="form-control"
            placeholder="搜索用户..."
            @input="searchUsers"
          />
        </div>
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>注册时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.registerTime }}</td>
              <td>
                <span
                  :class="[
                    'badge',
                    user.status === 'active' ? 'bg-success' : 'bg-danger',
                  ]"
                >
                  {{ user.status === "active" ? "正常" : "禁用" }}
                </span>
              </td>
              <td>
                <button
                  class="btn btn-warning btn-sm me-2"
                  @click="toggleUserStatus(user)"
                >
                  {{ user.status === "active" ? "禁用" : "启用" }}
                </button>
                <button class="btn btn-danger btn-sm" @click="deleteUser(user.id)">
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 系统设置 -->
      <div v-if="currentTab === 'settings'" class="admin-section">
        <h4>系统设置</h4>
        <div class="settings-form">
          <div class="form-group mb-3">
            <label>网站标题</label>
            <input type="text" v-model="settings.siteTitle" class="form-control" />
          </div>
          <div class="form-group mb-3">
            <label>视频上传限制 (MB)</label>
            <input type="number" v-model="settings.uploadLimit" class="form-control" />
          </div>
          <div class="form-group mb-3">
            <label>是否允许游客评论</label>
            <div class="form-check">
              <input
                type="checkbox"
                v-model="settings.allowGuestComment"
                class="form-check-input"
              />
            </div>
          </div>
          <button class="btn btn-primary" @click="saveSettings">保存设置</button>
        </div>
      </div>

      <!-- 数据统计 -->
      <div v-if="currentTab === 'stats'" class="admin-section">
        <h4>数据统计</h4>
        <div class="stats-grid">
          <div class="stat-card">
            <h5>总用户数</h5>
            <div class="stat-value">{{ stats.totalUsers }}</div>
          </div>
          <div class="stat-card">
            <h5>总视频数</h5>
            <div class="stat-value">{{ stats.totalVideos }}</div>
          </div>

          <div class="stat-card">
            <h5>待审核</h5>
            <div class="stat-value">{{ stats.pendingReviews }}</div>
          </div>
        </div>
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
import NavBar from "@/components/navBar/NavBar.vue";
import userApi from "@/api/user";
import adminApi from "@/api/admin";

export default {
  name: "AdminView",
  components: { NavBar },
  data() {
    return {
      currentTab: "pending",
      tabs: [
        { id: "pending", name: "待审核视频" },
        { id: "users", name: "用户管理" },
        { id: "settings", name: "系统设置" },
        { id: "stats", name: "数据统计" },
      ],
      pendingVideos: [],
      previewVisible: false,
      previewVideoData: null,
      users: [],
      userSearchQuery: "",
      settings: {
        siteTitle: "",
        uploadLimit: 100,
        allowGuestComment: true,
      },
      stats: {
        totalUsers: 0,
        totalVideos: 0,
        todayUploads: 0,
        pendingReviews: 0,
      },
    };
  },
  methods: {
    async fetchPendingVideos() {
      try {
        const res = await adminApi.getPendingVideos();
        this.pendingVideos = res.data || [];
      } catch (e) {
        this.pendingVideos = [];
        this.$message && this.$message.error("获取待审核视频失败");
      }
    },
    async reviewVideo(id, status) {
      try {
        await adminApi.reviewVideo(id, status);
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
    async searchUsers() {
      try {
        const res = await adminApi.getAllUserInfo();
        this.users = res.data || [];
      } catch (e) {
        this.$message && this.$message.error("获取用户列表失败");
      }
    },
    async toggleUserStatus(user) {
      try {
        await userApi.updateUser({
          id: user.id,
          status: user.status === "active" ? "disabled" : "active",
        });
        user.status = user.status === "active" ? "disabled" : "active";
        this.$message.success("状态更新成功");
      } catch (e) {
        this.$message && this.$message.error("操作失败");
      }
    },
    async deleteUser(userId) {
      if (!confirm("确定要删除该用户吗？")) return;
      try {
        await userApi.deleteUser(userId);
        this.users = this.users.filter((u) => u.id !== userId);
        this.$message.success("用户删除成功");
      } catch (e) {
        this.$message && this.$message.error("删除失败");
      }
    },
    async saveSettings() {
      try {
        await userApi.updateUser(this.settings);
        this.$message.success("设置保存成功");
      } catch (e) {
        this.$message && this.$message.error("保存失败");
      }
    },
    async fetchStats() {
      try {
        const [userCount, videoCount] = await Promise.all([
          adminApi.getUserCount(),
          adminApi.getVideoCount(),
        ]);
        this.stats = {
          totalUsers: userCount.data || 0,
          totalVideos: videoCount.data || 0,
          pendingReviews: 0,
        };
      } catch (e) {
        console.error("获取统计数据失败:", e);
        this.$message && this.$message.error("获取统计数据失败");
      }
    },
  },
  mounted() {
    this.fetchPendingVideos();
    this.searchUsers();
    this.fetchStats();
  },
  watch: {
    currentTab(newTab) {
      if (newTab === "stats") {
        this.fetchStats();
      }
    },
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

.admin-nav {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  border-bottom: 1px solid #eee;
  padding-bottom: 12px;
}

.nav-btn {
  padding: 8px 16px;
  border: none;
  background: none;
  color: #666;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-btn:hover {
  background: #f5f5f5;
}

.nav-btn.active {
  background: #00aeec;
  color: white;
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #00aeec;
  margin-top: 10px;
}

.settings-form {
  max-width: 600px;
  margin: 0 auto;
}

.search-bar {
  max-width: 300px;
}
</style>
