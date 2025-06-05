<template>
  <div>
    <VideoBar />
    <div class="profile-container container-fluid mt-4">
      <div class="row">
        <div class="col-md-3">
          <ProfileSidebar />
        </div>
        <div class="col-md-9">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import ProfileSidebar from "@/components/user/ProfileSidebar.vue";
export default {
  name: "AchievementView",
  components: {
    VideoBar,
    ProfileSidebar,
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
</style>
