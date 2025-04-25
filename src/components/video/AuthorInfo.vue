<template>
  <div class="author-info">
    <div class="d-flex align-items-center">
      <div class="author-avatar me-2">
        <img
          :src="videoInfo.avatar"
          class="rounded-circle"
          width="80"
          height="80"
          alt="author avatar"
        />
      </div>
      <div class="author-details">
        <div class="d-flex align-items-center mb-1">
          <h6 class="mb-0">{{ videoInfo.author }}</h6>
          <el-icon class="ms-4"><Message /></el-icon>
          <div class="small ms-1">发消息</div>
        </div>
        <p class="small text-start mb-1">{{ videoInfo.description }}</p>
        <div>
          <div class="d-grid gap-1 d-md-block">
            <button class="btn btn-sm btn-outline-danger me-1" style="width: 80px">
              <i class="bi bi-lightning-charge"></i> 充电
            </button>
            <button
              class="btn btn-sm btn-primary"
              @click="toggleFollow"
              v-if="!userInfo.is_follow"
            >
              + 关注 {{ videoInfo.follow }}
            </button>
            <button
              class="btn btn-sm btn-secondary"
              @click="toggleFollow"
              v-if="userInfo.is_follow"
            >
              已关注 {{ videoInfo.follow }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AuthorInfo",
  props: {
    videoInfo: {
      type: Object,
      required: true,
    },
    userInfo: {
      type: Object,
      required: true,
    },
  },
  methods: {
    toggleFollow() {
      this.$emit("toggle-follow");
    },
  },
};
</script>

<style scoped>
.author-info {
  padding: 12px;
  background-color: white;
  border-radius: 6px;
  margin-bottom: 16px;
}

.author-avatar img {
  width: 46px;
  height: 46px;
  border: 1px solid #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.author-details h6 {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 0;
}

.author-details .text-muted {
  font-size: 15px;
  margin-bottom: 0;
}

.author-details .btn-sm {
  font-size: 12px;
}

.author-details .el-icon {
  font-size: 15px;
  margin-left: 4px;
  color: #666;
}
</style>
