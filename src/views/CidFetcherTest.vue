<template>
  <div class="cid-fetcher-test-view container mt-4">
    <h2>Bilibili CID Fetcher Test</h2>
    <div class="input-group mb-3">
      <input
        type="text"
        class="form-control"
        placeholder="Enter BV ID (e.g., BV1eK411W7R7)"
        v-model="bvId"
      />
      <button class="btn btn-primary" @click="fetchCid">Fetch CID</button>
    </div>

    <div v-if="loading" class="alert alert-info">Loading...</div>
    <div v-if="error" class="alert alert-danger">Error: {{ error }}</div>

    <div v-if="cidData && cidData.length > 0">
      <h3>CID Results:</h3>
      <ul class="list-group">
        <li class="list-group-item" v-for="(item, index) in cidData" :key="item.cid">
          P{{ index + 1 }}: CID={{ item.cid }}, Title={{ item.part }}
        </li>
      </ul>
    </div>
    <div v-else-if="cidData && cidData.length === 0" class="alert alert-warning">
      No CID data found for this BV ID.
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CidFetcherTestView",
  data() {
    return {
      bvId: "",
      cidData: null,
      loading: false,
      error: null,
    };
  },
  methods: {
    async fetchCid() {
      if (!this.bvId) {
        this.error = "Please enter a BV ID.";
        this.cidData = null;
        return;
      }

      this.loading = true;
      this.error = null;
      this.cidData = null;

      try {
        const url = `https://api.bilibili.com/x/player/pagelist?bvid=${this.bvId}`;
        const response = await axios.get(url);
        const data = response.data;

        if (data.code === 0 && data.data && data.data.length > 0) {
          this.cidData = data.data;
          console.log(`BV${this.bvId} 的 CID 列表:`, this.cidData);
        } else {
          this.error = data.message || "Failed to fetch CID data.";
          console.error("获取失败:", data.message);
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
.cid-fetcher-test-view {
  max-width: 800px;
  margin: 20px auto;
}
</style>
