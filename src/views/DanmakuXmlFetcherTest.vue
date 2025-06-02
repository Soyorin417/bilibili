<template>
  <div class="danmaku-xml-fetcher-test-view container mt-4">
    <h2>Bilibili Danmaku XML Fetcher Test (by OID)</h2>
    <div class="input-group mb-3">
      <input
        type="text"
        class="form-control"
        placeholder="Enter OID (e.g., 29718874563)"
        v-model="oid"
      />
      <button class="btn btn-primary" @click="fetchDanmakuXml">Fetch XML</button>
    </div>

    <div v-if="loading" class="alert alert-info">Loading...</div>
    <div v-if="error" class="alert alert-danger">Error: {{ error }}</div>

    <div v-if="parsedDanmaku.length > 0" class="mt-4">
      <h3>Parsed Danmaku ({{ parsedDanmaku.length }}):</h3>
      <div class="danmaku-list">
        <div v-for="(danmaku, index) in parsedDanmaku" :key="index" class="danmaku-item">
          <span class="time">{{ formatTime(danmaku.time) }}</span>
          <span class="type">{{ getDanmakuType(danmaku.type) }}</span>
          <span class="track" v-if="danmaku.track !== undefined"
            >轨道{{ danmaku.track }}</span
          >
          <span class="color" :style="{ color: danmaku.color }">■</span>
          <span class="content" :class="{ advanced: danmaku.isAdvanced }">{{
            danmaku.text
          }}</span>
          <span v-if="danmaku.isAdvanced" class="advanced-info">
            (高级弹幕: {{ danmaku.position?.duration || 8 }}秒)
          </span>
        </div>
      </div>
    </div>

    <div v-if="xmlData" class="mt-4">
      <h3>Raw XML Data:</h3>
      <pre class="xml-display">{{ xmlData }}</pre>
    </div>
    <div v-else-if="!loading && !error" class="alert alert-warning">
      Enter an OID and click "Fetch XML" to get the danmaku data.
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { parseDanmakuXml } from "@/components/danmaku/danmakuParser";

export default {
  name: "DanmakuXmlFetcherTestView",
  data() {
    return {
      oid: "",
      xmlData: null,
      parsedDanmaku: [],
      loading: false,
      error: null,
    };
  },
  methods: {
    async fetchDanmakuXml() {
      if (!this.oid) {
        this.error = "Please enter an OID.";
        this.xmlData = null;
        this.parsedDanmaku = [];
        return;
      }

      this.loading = true;
      this.error = null;
      this.xmlData = null;
      this.parsedDanmaku = [];

      try {
        const response = await axios.get(
          `http://localhost:8081/api/danmaku/${this.oid}`,
          {
            headers: {
              Accept: "application/xml;charset=UTF-8",
              "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
            },
            timeout: 5000,
          }
        );

        if (response.data.startsWith("读取失败：")) {
          throw new Error(response.data);
        }

        this.xmlData = response.data;
        this.parsedDanmaku = parseDanmakuXml(response.data);
        console.log(`Fetched XML for OID ${this.oid}:`, this.xmlData);
        console.log("Parsed danmaku:", this.parsedDanmaku);
      } catch (error) {
        if (error.code === "ECONNABORTED") {
          this.error = "请求超时，请稍后重试";
        } else if (error.response?.status === 500) {
          this.error = error.response.data || "服务器内部错误";
        } else {
          this.error = error.message || "请求失败";
        }

        console.error("请求出错:", error);
        if (error.response) {
          console.error("Response data:", error.response.data);
          console.error("Response status:", error.response.status);
          console.error("Response headers:", error.response.headers);
        } else if (error.request) {
          console.error("Request made but no response received:", error.request);
        } else {
          console.error("Something happened in setting up the request:", error.message);
        }
      } finally {
        this.loading = false;
      }
    },

    formatTime(seconds) {
      const minutes = Math.floor(seconds / 60);
      const remainingSeconds = (seconds % 60).toFixed(2);
      return `${minutes}:${remainingSeconds.padStart(5, "0")}`;
    },

    getDanmakuType(type) {
      const types = {
        1: "滚动",
        2: "底部",
        3: "顶部",
        4: "反向",
        5: "高级",
        6: "代码",
        7: "BAS",
        8: "特殊",
      };
      return types[type] || `类型${type}`;
    },
  },
};
</script>

<style scoped>
.danmaku-xml-fetcher-test-view {
  max-width: 800px;
  margin: 20px auto;
}

.xml-display {
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  padding: 15px;
  white-space: pre-wrap;
  word-wrap: break-word;
  max-height: 400px;
  overflow-y: auto;
}

.danmaku-list {
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  padding: 10px;
}

.danmaku-item {
  padding: 5px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 10px;
}

.danmaku-item:last-child {
  border-bottom: none;
}

.time {
  color: #666;
  font-family: monospace;
  min-width: 80px;
}

.type {
  color: #666;
  font-size: 0.9em;
  min-width: 60px;
}

.track {
  color: #666;
  font-size: 0.9em;
  min-width: 60px;
}

.color {
  font-size: 1.2em;
}

.content {
  flex: 1;
}

.content.advanced {
  font-weight: bold;
  color: #e83e8c;
}

.advanced-info {
  color: #666;
  font-size: 0.9em;
}
</style>
