<template>
  <div class="danmaku-container mt-2 mb-3" :class="{ expanded: isShow }">
    <div class="danmaku-header d-flex justify-content-between align-items-center p-1">
      <div class="d-flex align-items-center">
        <h6 class="mb-0 ms-1">弹幕列表</h6>
        <small class="text-secondary ms-2">({{ danmakuList.length }}条)</small>
      </div>
      <div>
        <small class="text-secondary me-1" @click="toggleShow">
          <drop-down-list theme="outline" size="18" fill="#000000" />
        </small>
      </div>
    </div>
    <div v-if="isShow" class="danmaku-list">
      <table class="table table-sm table-hover borderless">
        <thead class="thead-light">
          <tr class="header-row">
            <th scope="col" width="20%">时间</th>
            <th scope="col" width="80%">内容</th>
          </tr>
        </thead>
        <tbody class="small">
          <tr v-for="(item, index) in danmakuList" :key="index">
            <td>{{ formatTime(item.time) }}</td>
            <td class="text-truncate" style="max-width: 160px">
              {{ item.text }}
            </td>
            <td>{{ item.sendTime }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { DropDownList } from "@icon-park/vue-next";
import { parseDanmakuXml } from "../danmaku/danmakuParser";
import axios from "axios";

export default {
  name: "DanmakuList",
  components: {
    DropDownList,
  },
  props: {
    videoId: {
      type: String,
      required: true,
      validator: (value) => {
        return !isNaN(value) && value > 0;
      },
    },
    danmakuUrl: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      isShow: false,
      danmakuList: [],
    };
  },
  methods: {
    async getDanmakuList(videoId) {
      if (!videoId) {
        console.error("无效的 videoId:", videoId);
        return;
      }

      const url = `http://127.0.0.1:8081/danmaku/getDanmakuList?id=${videoId}`;
      const token = localStorage.getItem("token");

      if (!token) {
        console.error("Token is missing");
        return;
      }

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          this.danmakuList = response.data;
        }
      } catch (error) {
        console.error("Error fetching danmaku list:", error);
      }
    },
    toggleShow() {
      this.isShow = !this.isShow;
    },
    formatTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes}:${seconds.toString().padStart(2, "0")}`;
    },

    async loadDanmaku() {
      const danmakuInfo = this.danmakuUrl;

      if (danmakuInfo) {
        try {
          const response = await axios.get(danmakuInfo, {
            transformResponse: [(data) => data],
          });

          this.danmakuList = parseDanmakuXml(response.data);
          this.$emit("danmaku-loaded", this.danmakuList);
        } catch (error) {
          console.error("获取弹幕数据失败:", error);
          this.danmakuList = [];
        }
      } else {
        console.log("未找到对应的弹幕信息:", this.videoId);
        this.danmakuList = [];
      }
    },
  },
  watch: {
    videoId: {
      immediate: true,
      handler(newVal) {
        console.log("videoId changed:", newVal);
        this.loadDanmaku();
      },
    },
    danmakuUrl: {
      immediate: true,
      handler(newVal) {
        console.log("danmakuUrl changed:", newVal);
        this.loadDanmaku();
      },
    },
  },
};
</script>

<style scoped>
.danmaku-container {
  border: 1px solid #eee;

  overflow: hidden;
  background-color: #fff;
  font-size: 10px;
  height: auto;
  min-height: 30px;
}

.danmaku-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
  padding: 2px 4px !important;
  min-height: 28px;
  display: flex;
  align-items: center;
}

.danmaku-header h6 {
  font-size: 13px;
  color: #333;
  margin-bottom: 0;
}

.danmaku-list {
  height: 625px;
  overflow-y: auto;
  max-height: none;
}

.danmaku-list table {
  margin-bottom: 0;
  border: none;
  font-size: 12px;
}

.danmaku-list th {
  font-size: 12px;
  font-weight: 600;
  color: #555;
  padding: 4px 8px;
  border: none;
  background-color: #f8f9fa;
  position: sticky;
  top: 0;
  z-index: 1;
}

.danmaku-list td {
  font-size: 12px;
  padding: 4px 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border: none;
  line-height: 1.2;
}

.borderless th,
.borderless td,
.borderless thead th,
.borderless tbody + tbody {
  border: none !important;
}

.table-hover > tbody > tr:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.header-row {
  border: none !important;
  background-color: #f8f9fa;
}

/* 调整容器高度 */
.danmaku-container.expanded {
  height: 725px;
}
</style>
