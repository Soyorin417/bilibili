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
          <h6 class="mb-0">{{ videoInfo.authorName }}</h6>
          <el-icon class="ms-4"><Message /></el-icon>
          <div class="small ms-1">发消息</div>
        </div>
        <p class="small text-start mb-1">{{ truncatedDescription }}</p>
        <div>
          <div class="d-grid gap-1 d-md-block">
            <button class="btn btn-sm btn-outline-danger me-1" style="width: 80px">
              <i class="bi bi-lightning-charge"></i> 充电
            </button>
            <button
              class="btn btn-sm btn-primary"
              @click="toggleFollow"
              v-if="!isFollow"
              :disabled="loading"
            >
              + 关注 {{ localFollowCount }}
            </button>
            <button
              class="btn btn-sm btn-secondary"
              @click="toggleFollow"
              v-if="isFollow"
              :disabled="loading"
            >
              已关注 {{ localFollowCount }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
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
  data() {
    return {
      followList: [],
      isFollow: false,
      loading: false,
      localFollowCount: 0,
    };
  },
  watch: {
    videoInfo: {
      immediate: true,
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.localFollowCount = newVal.follow || 0;
          this.checkFollowStatus();
          this.fetchFollowList();
        }
      },
    },
  },
  computed: {
    truncatedDescription() {
      if (!this.videoInfo.description) return "";
      return this.videoInfo.description.length > 20
        ? this.videoInfo.description.slice(0, 20) + "..."
        : this.videoInfo.description;
    },
    isSelf() {
      return this.userInfo && this.videoInfo && this.userInfo.id === this.getAuthorId();
    },
  },
  mounted() {
    if (this.videoInfo && Object.keys(this.videoInfo).length > 0) {
      this.checkFollowStatus();
      this.fetchFollowList();
    }
  },
  methods: {
    async toggleFollow() {
      if (this.loading) return;
      if (this.userInfo && this.getAuthorId && this.userInfo.id === this.getAuthorId()) {
        alert("不能关注自己！");
        return;
      }
      this.loading = true;
      try {
        const params = {
          followerUid: this.userInfo.id,
          followingUid: this.getAuthorId(),
        };
        if (!this.isFollow) {
          await axios.post("http://127.0.0.1:8081/api/follow/follow", null, { params });
        } else {
          await axios.post("http://127.0.0.1:8081/api/follow/unfollow", null, { params });
        }
        // 操作后刷新
        await this.checkFollowStatus();
        await this.refreshFollowCount();
      } catch (e) {
        console.error("toggleFollow error:", e);
      } finally {
        this.loading = false;
      }
    },
    async refreshFollowCount() {
      // 假设有接口能查作者粉丝数
      const authorId = this.getAuthorId();
      const res = await axios.get("http://127.0.0.1:8081/api/user/info", {
        params: { uid: authorId },
      });
      this.localFollowCount = res.data.follow || 0;
    },
    async fetchFollowList() {
      if (!this.userInfo || !this.userInfo.id) return;
      const res = await axios.get("http://127.0.0.1:8081/api/follow/list", {
        params: { followerUid: this.userInfo.id },
      });
      this.followList = res.data;
    },
    async checkFollowStatus() {
      const myId = this.userInfo.id;
      const authorId = this.getAuthorId();
      const res = await axios.get("http://127.0.0.1:8081/api/follow/list", {
        params: { followerUid: myId },
      });
      this.isFollow = res.data.some(
        (item) => Number(item.followingUid) === Number(authorId)
      );
    },
    getAuthorId() {
      return (
        this.videoInfo.authorId || this.videoInfo.author?.id || this.videoInfo.author?.uid
      );
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

.author-details p.small {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
