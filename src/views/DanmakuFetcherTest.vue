<template>
  <div class="danmaku-fetcher-test-view container mt-4">
    <h2>Bilibili Danmaku URL Fetcher Test</h2>
    <div class="input-group mb-3">
      <input
        type="text"
        class="form-control"
        placeholder="Enter BV ID (e.g., BV1eK411W7R7)"
        v-model="bvId"
      />
      <button class="btn btn-primary" @click="fetchDanmakuUrl">Fetch Danmaku URL</button>
    </div>

    <div v-if="loading" class="alert alert-info">Loading...</div>
    <div v-if="error" class="alert alert-danger">Error: {{ error }}</div>

    <div v-if="cid || danmakuUrl">
      <h3>Results:</h3>
      <p v-if="cid"><strong>CID:</strong> {{ cid }}</p>
      <p v-if="danmakuUrl"><strong>Danmaku XML URL:</strong> {{ danmakuUrl }}</p>
    </div>
    <div v-else-if="!loading && !error" class="alert alert-warning">
      Enter a BV ID and click "Fetch Danmaku URL" to get started.
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "DanmakuFetcherTestView",
  data() {
    return {
      bvId: "",
      cid: null,
      danmakuUrl: null,
      loading: false,
      error: null,
    };
  },
  methods: {
    async fetchDanmakuUrl() {
      if (!this.bvId) {
        this.error = "Please enter a BV ID.";
        this.cid = null;
        this.danmakuUrl = null;
        return;
      }

      this.loading = true;
      this.error = null;
      this.cid = null;
      this.danmakuUrl = null;

      try {
        const url = `https://api.bilibili.com/x/player/pagelist?bvid=${this.bvId}`;
        const response = await axios.get(url);
        const data = response.data;

        if (data.code === 0 && data.data && data.data.length > 0) {
          this.cid = data.data[0].cid;
          this.danmakuUrl = `https://comment.bilibili.com/${this.cid}.xml`;
          console.log(`BV号 ${this.bvId} 的 CID 是 ${this.cid}`);
          console.log(`弹幕 XML 地址为: ${this.danmakuUrl}`);
        } else {
          this.error = data.message || "Failed to get CID.";
          console.error("未能获取 CID：", data.message);
        }
      } catch (error) {
        this.error = error.message || "Request failed.";
        console.error("请求出错:", error);
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.danmaku-fetcher-test-view {
  max-width: 800px;
  margin: 20px auto;
}
</style>
