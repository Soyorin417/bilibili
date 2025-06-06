<template>
  <div>
    <VideoBar />
    <div class="admin-layout">
      <!-- 左侧导航栏 -->
      <AdminSidebar
        :tabs="tabs"
        :current-tab="currentTab"
        :expanded-tabs="expandedTabs"
        @tab-click="handleTabClick"
        @toggle-expand="toggleExpand"
      />

      <!-- 右侧内容区域 -->
      <div class="admin-content">
        <!-- 审核视频 -->
        <div v-if="currentTab === 'pending'" class="admin-section">
          <PendingVideos
            :videos="pendingVideos"
            @approve="approveVideo"
            @reject="rejectVideo"
            @preview="previewVideo"
          />
        </div>

        <!-- 用户管理 -->
        <div v-if="currentTab === 'users'" class="admin-section">
          <UserManagement
            :users="users"
            :search-query="searchQuery"
            @search="searchUsers"
            @toggle-status="toggleUserStatus"
            @delete="deleteUser"
          />
        </div>

        <!-- 系统设置 -->
        <div v-if="currentTab === 'settings'" class="admin-section">
          <SystemSettings
            :settings="settings"
            @save="saveSettings"
            @reset="resetSettings"
          />
        </div>

        <!-- 数据统计 -->
        <div v-if="currentTab === 'stats'" class="admin-section">
          <StatisticsPanel />
        </div>

        <div v-if="currentTab === 'banner'" class="admin-section">
          <BannerManagement
            :banners="banners"
            @add="handleAddBanner"
            @update="handleUpdateBanner"
            @delete="handleDeleteBanner"
            @type-change="handleBannerTypeChange"
          />
        </div>

        <div v-if="currentTab === 'category'" class="admin-section">
          <h4>分类管理</h4>
          <div class="category-manager">
            <!-- 分类管理内容 -->
          </div>
        </div>

        <div v-if="currentTab === 'theme'" class="admin-section">
          <h4>主题设置</h4>
          <div class="theme-manager">
            <!-- 主题设置内容 -->
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
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import adminApi from "@/api/admin/admin";
import AdminSidebar from "@/components/admin/AdminSidebar.vue";
import PendingVideos from "@/components/admin/PendingVideos.vue";
import UserManagement from "@/components/admin/UserManagement.vue";
import SystemSettings from "@/components/admin/SystemSettings.vue";
import StatisticsPanel from "@/components/admin/Statistics.vue";
import BannerManagement from "@/components/admin/BannerManagement.vue";
import carouselApi from "@/api/admin/carousel";

export default {
  name: "AdminView",
  components: {
    VideoBar,
    AdminSidebar,
    PendingVideos,
    UserManagement,
    SystemSettings,
    StatisticsPanel,
    BannerManagement,
  },
  data() {
    return {
      currentTab: "pending",
      expandedTabs: ["view"],
      tabs: [
        {
          id: "pending",
          name: "审核视频",
          icon: "video",
          children: [],
        },
        {
          id: "users",
          name: "用户管理",
          icon: "user",
          children: [],
        },
        {
          id: "settings",
          name: "系统设置",
          icon: "setting",
          children: [],
        },
        {
          id: "stats",
          name: "数据统计",
          icon: "chart",
          children: [],
        },
        {
          id: "view",
          name: "界面管理",
          icon: "layout",
          children: [
            { id: "banner", name: "轮播图管理" },
            { id: "category", name: "分类管理" },
            { id: "theme", name: "主题设置" },
          ],
        },
      ],
      pendingVideos: [],
      previewVisible: false,
      previewVideoData: null,
      users: [],
      searchQuery: "",
      settings: {
        siteTitle: "",
        uploadLimit: 100,
        allowGuestComment: false,
        requireVideoReview: true,
        requireCommentReview: false,
      },
      stats: {
        totalUsers: 0,
        totalVideos: 0,
        todayUploads: 0,
        pendingReviews: 0,
      },
      banners: [],
      currentBannerType: "home",
      bannerTypes: [
        { label: "首页轮播图", value: "home" },
        { label: "动漫轮播图", value: "anime" },
      ],
    };
  },
  methods: {
    handleTabClick(tabId) {
      this.currentTab = tabId;
    },
    toggleExpand(tabId) {
      const index = this.expandedTabs.indexOf(tabId);
      if (index === -1) {
        this.expandedTabs.push(tabId);
      } else {
        this.expandedTabs.splice(index, 1);
      }
    },
    async fetchPendingVideos() {
      try {
        const res = await adminApi.getPendingVideos();
        this.pendingVideos = res.data || [];
      } catch (error) {
        this.$message.error("获取待审核视频失败");
      }
    },
    async approveVideo(videoId) {
      try {
        await adminApi.reviewVideo(videoId, "approved");
        this.$message.success("视频审核通过");
        this.fetchPendingVideos();
      } catch (error) {
        this.$message.error("操作失败");
      }
    },
    async rejectVideo(videoId) {
      try {
        await adminApi.reviewVideo(videoId, "rejected");
        this.$message.success("视频已拒绝");
        this.fetchPendingVideos();
      } catch (error) {
        this.$message.error("操作失败");
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
    async fetchUsers() {
      try {
        const res = await adminApi.getAllUserInfo();
        if (res && res.data) {
          this.users = res.data;
        } else {
          this.users = [];
          this.$message.error("获取用户列表失败");
        }
      } catch (error) {
        console.error("获取用户列表失败:", error);
        this.users = [];
        this.$message.error("获取用户列表失败");
      }
    },
    async searchUsers(query) {
      this.searchQuery = query;
      if (!query.trim()) {
        await this.fetchUsers();
        return;
      }

      try {
        let res;
        if (/^\d+$/.test(query.trim())) {
          res = await adminApi.getUserById(Number(query.trim()));
          this.users = res.data ? [res.data] : [];
        } else {
          res = await adminApi.getUserByUsername(query.trim());
          this.users = res.data ? [res.data] : [];
        }
      } catch (error) {
        console.error("搜索用户失败:", error);
        this.$message.error("搜索用户失败");
      }
    },

    async toggleUserStatus(user) {
      try {
        if (user.is_banned === false) {
          await adminApi.banUser(user.id);
          user.is_banned = true;
          console.log(user.id);
          this.$message.success("用户已封禁");
        } else {
          await adminApi.unbanUser(user.id);
          user.is_banned = false;
          this.$message.success("用户已解封");
        }
      } catch (error) {
        console.error("更新用户状态失败:", error);
        this.$message.error("操作失败");
      }
    },
    async deleteUser(userId) {
      if (!confirm("确定要删除该用户吗？")) return;
      try {
        await adminApi.deleteUser(userId);
        this.users = this.users.filter((u) => u.id !== userId);
        this.$message.success("用户删除成功");
      } catch (error) {
        console.error("删除用户失败:", error);
        this.$message.error("删除失败");
      }
    },
    async fetchSettings() {
      try {
        const res = await adminApi.getSystemSettings();
        if (res && res.data) {
          this.settings = { ...res.data };
        }
      } catch (error) {
        console.error("获取系统设置失败:", error);
        this.$message.error("获取系统设置失败");
      }
    },
    async saveSettings() {
      try {
        await adminApi.updateSystemSettings(this.settings);
        this.$message.success("设置保存成功");
      } catch (error) {
        console.error("保存设置失败:", error);
        this.$message.error("保存设置失败");
      }
    },
    resetSettings() {
      this.fetchSettings();
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
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
    async fetchBanners() {
      try {
        const res = await carouselApi.getAllCarousels();
        this.banners = (res.data || []).filter(
          (banner) => banner.type === this.currentBannerType
        );
      } catch (error) {
        console.error("获取轮播图列表失败:", error);
        this.$message.error("获取轮播图列表失败");
      }
    },
    handleBannerTypeChange(type) {
      this.currentBannerType = type;
      this.fetchBanners();
    },
    async handleAddBanner(formData) {
      try {
        formData.append("type", this.currentBannerType);
        const res = await carouselApi.addCarousel(formData);
        if (res.data.error_message === "success") {
          this.$message.success("添加轮播图成功");
          this.fetchBanners();
        } else {
          this.$message.error(res.data.error_message || "添加失败");
        }
      } catch (error) {
        console.error("添加轮播图失败:", error);
        this.$message.error("添加轮播图失败");
      }
    },
    async handleUpdateBanner(formData) {
      try {
        if (!formData.get("type")) {
          formData.append("type", this.currentBannerType);
        }
        const res = await carouselApi.updateCarousel(formData);
        if (res.data.error_message === "success") {
          this.$message.success("更新轮播图成功");
          this.fetchBanners();
        } else {
          this.$message.error(res.data.error_message || "更新失败");
        }
      } catch (error) {
        console.error("更新轮播图失败:", error);
        this.$message.error("更新轮播图失败");
      }
    },
    async handleDeleteBanner(id) {
      try {
        const response = await carouselApi.deleteCarousel(id);
        if (response.data.error_message === "success") {
          this.$message.success("轮播图删除成功");
          this.fetchBanners();
          return response.data;
        } else {
          const errorMsg =
            response.data.error_message || response.data.message || "删除失败";
          this.$message.error(errorMsg);
          throw new Error(errorMsg);
        }
      } catch (error) {
        console.error("删除轮播图失败:", error);
        if (error.response) {
          const errorMsg =
            error.response.data?.error_message ||
            error.response.data?.message ||
            error.message;
          this.$message.error(`服务器响应错误: ${errorMsg}`);
        } else if (error.request) {
          this.$message.error("无法连接到服务器，请确保后端服务已启动");
        } else {
          this.$message.error(`请求错误: ${error.message}`);
        }
        throw error;
      }
    },
  },
  mounted() {
    this.fetchPendingVideos();
    this.fetchUsers();
    this.fetchSettings();
    this.fetchStats();
    this.fetchBanners();
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
.admin-layout {
  display: flex;
  min-height: calc(100vh - 60px);
  background: #f5f5f5;
}

.admin-content {
  flex: 1;
  margin-left: 240px;
  padding: 32px;
  background: #fff;
  min-height: calc(100vh - 60px);
}

.admin-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.06);
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
