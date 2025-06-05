<template>
  <div>
    <h4>系统设置</h4>
    <div class="settings-form">
      <form @submit.prevent="handleSave">
        <div class="form-group mb-3">
          <label>网站标题</label>
          <input type="text" v-model="localSettings.siteTitle" class="form-control" required />
        </div>

        <div class="form-group mb-3">
          <label>视频上传限制 (MB)</label>
          <input
            type="number"
            v-model="localSettings.uploadLimit"
            class="form-control"
            min="1"
            required
          />
        </div>

        <div class="form-group mb-3">
          <label>是否允许游客评论</label>
          <div class="form-check">
            <input
              type="checkbox"
              v-model="localSettings.allowGuestComment"
              class="form-check-input"
              id="allowGuestComment"
            />
            <label class="form-check-label" for="allowGuestComment"> 允许游客评论 </label>
          </div>
        </div>

        <div class="form-group mb-3">
          <label>视频审核</label>
          <div class="form-check">
            <input
              type="checkbox"
              v-model="localSettings.requireVideoReview"
              class="form-check-input"
              id="requireVideoReview"
            />
            <label class="form-check-label" for="requireVideoReview">
              上传视频需要审核
            </label>
          </div>
        </div>

        <div class="form-group mb-3">
          <label>评论审核</label>
          <div class="form-check">
            <input
              type="checkbox"
              v-model="localSettings.requireCommentReview"
              class="form-check-input"
              id="requireCommentReview"
            />
            <label class="form-check-label" for="requireCommentReview">
              发表评论需要审核
            </label>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">保存设置</button>
          <button type="button" class="btn btn-secondary ms-2" @click="handleReset">
            重置
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: "SystemSettings",
  props: {
    settings: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localSettings: { ...this.settings },
    };
  },
  watch: {
    settings: {
      handler(newSettings) {
        this.localSettings = { ...newSettings };
      },
      deep: true,
    },
  },
  methods: {
    handleSave() {
      this.$emit("save", this.localSettings);
    },
    handleReset() {
      this.localSettings = { ...this.settings };
      this.$emit("reset");
    },
  },
};
</script>

<style scoped>
.settings-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-actions {
  margin-top: 24px;
}

.form-check-label {
  margin-left: 8px;
}
</style>
