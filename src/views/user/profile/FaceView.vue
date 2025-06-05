<template>
  <div>
    <VideoBar />
    <div class="profile-container container-fluid mt-4">
      <div class="row">
        <div class="col-md-3">
          <ProfileSidebar />
        </div>
        <div class="col-md-9">
          <div class="card">
            <div class="card-header">
              <h3>更改信息</h3>
            </div>
            <div class="card-body">
              <form @submit.prevent="updateProfile">
                <div class="form-group mb-3">
                  <div class="avatar-section">
                    <div class="avatar-container">
                      <img
                        :src="profile.avatarUrl || '/default-avatar.png'"
                        alt="用户头像"
                        class="avatar"
                        @click="triggerFileInput"
                      />
                      <div class="avatar-overlay" @click="triggerFileInput">
                        <i class="el-icon-camera"></i>
                      </div>
                    </div>
                    <input
                      type="file"
                      ref="fileInput"
                      style="display: none"
                      accept="image/*"
                      @change="handleAvatarChange"
                    />
                  </div>
                  <div v-if="uploadProgress > 0" class="upload-progress">
                    <el-progress
                      :percentage="uploadProgress"
                      :show-text="false"
                    ></el-progress>
                  </div>
                </div>
                <div class="form-group mb-3">
                  <label for="username">用户名</label>
                  <input
                    type="text"
                    class="form-control mt-2"
                    id="username"
                    v-model="profile.username"
                  />
                </div>
                <div class="form-group mb-3">
                  <label for="bio">简介</label>
                  <textarea
                    class="form-control mt-2"
                    id="bio"
                    v-model="profile.bio"
                    rows="3"
                  ></textarea>
                </div>
                <button type="submit" class="btn btn-primary">保存</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import ProfileSidebar from "@/components/user/ProfileSidebar.vue";
import UploadUtils from "@/utils/uploadUtils";

export default {
  name: "FaceView",
  components: {
    VideoBar,
    ProfileSidebar,
  },
  data() {
    return {
      profile: {
        username: "",
        bio: "",
        avatar: null,
        avatarUrl: "",
      },
      uploadProgress: 0,
    };
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleAvatarChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.profile.avatar = file;
        this.profile.avatarUrl = URL.createObjectURL(file);
      }
    },
    async updateProfile() {
      try {
        const updateInfo = {};
        if (this.profile.username) {
          updateInfo.username = this.profile.username;
        }
        if (this.profile.bio) {
          updateInfo.bio = this.profile.bio;
        }

        let res;
        if (this.profile.avatar) {
          // 使用工具类上传头像
          res = await UploadUtils.uploadAvatar(
            this.profile.avatar,
            updateInfo,
            (progress) => {
              this.uploadProgress = progress;
            }
          );
        } else {
          // 使用工具类更新用户信息
          res = await UploadUtils.updateUserInfo(updateInfo);
        }

        console.log("更新响应:", res);

        // 检查响应数据的结构
        const responseData = res.data || res;
        if (responseData && responseData.error_message === "success") {
          // 更新本地存储的用户信息
          const userInfo = JSON.parse(localStorage.getItem("userInfo"));
          if (userInfo) {
            if (this.profile.username) {
              userInfo.username = this.profile.username;
            }
            if (this.profile.bio) {
              userInfo.bio = this.profile.bio;
            }
            if (this.profile.avatar) {
              userInfo.avatar = this.profile.avatarUrl;
            }
            localStorage.setItem("userInfo", JSON.stringify(userInfo));
          }
          this.$message && this.$message.success
            ? this.$message.success("信息更新成功")
            : alert("信息更新成功");
        } else {
          const errorMsg =
            responseData?.error_message || responseData?.message || "更新失败";
          console.error("更新失败，响应数据:", responseData);
          throw new Error(errorMsg);
        }
      } catch (e) {
        console.error("更新信息失败，完整错误:", e);
        const errorMessage =
          e.response?.data?.error_message || e.message || "信息更新失败";
        this.$message && this.$message.error
          ? this.$message.error(errorMessage)
          : alert(errorMessage);
      } finally {
        this.uploadProgress = 0;
      }
    },
  },
  mounted() {
    // 获取用户信息
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo) {
      this.profile.username = userInfo.username || "";
      this.profile.bio = userInfo.bio || "";
      this.profile.avatarUrl = userInfo.avatar || "/default-avatar.png";
    }
  },
};
</script>

<style scoped>
.profile-container {
  padding-top: 20px;
  padding-bottom: 20px;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.avatar-container {
  position: relative;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.avatar-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
  transition: opacity 0.3s;
}

.avatar-preview:hover {
  opacity: 0.8;
}

.avatar-section {
  text-align: center;
  margin-bottom: 20px;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
}

.avatar-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 150px;
  height: 150px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.upload-progress {
  margin-top: 10px;
}
</style>
