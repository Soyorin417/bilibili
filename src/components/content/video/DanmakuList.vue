<template>
  <div class="danmaku-list mt-1">
    <div class="danmaku-list-header">
      <h5>弹幕列表</h5>
      <button class="toggle-btn" @click="toggleShow">
        {{ isShow ? "˄" : "˅" }}
      </button>
    </div>
    <div class="danmaku-list-content" v-show="isShow">
      <table class="danmaku-table">
        <thead>
          <tr>
            <th>时间</th>
            <th>弹幕内容</th>
            <th>类型</th>
            <th>发送时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="!mergedDanmakuList || mergedDanmakuList.length === 0">
            <td colspan="4" class="no-danmaku">暂无弹幕</td>
          </tr>
          <tr v-else v-for="(danmaku, index) in mergedDanmakuList" :key="index">
            <td>{{ formatTime(danmaku.time) }}</td>
            <td>
              {{ limitTextLength(danmaku.text) || limitTextLength(danmaku.content) }}
            </td>
            <td>{{ getDanmakuType(danmaku.type) }}</td>
            <td>{{ formatDate(danmaku.timestamp) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { danmakuApi } from "@/api/content/danmaku";
import danmuApi from "@/api/danmu";
import { parseDanmakuXml } from "@/utils/danmakuParser";
import { convertDbDanmuToFrontend } from "@/utils/convertDbDanmuToFrontend";
import { limitTextLength, formatDate } from "@/utils/date";

export default {
  name: "DanmakuList",
  props: {
    videoId: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      isShow: false,
      xmlDanmaku: [],
      dbDanmaku: [],
    };
  },
  computed: {
    mergedDanmakuList() {
      // 合并 XML 和数据库弹幕
      const merged = [...this.xmlDanmaku, ...this.dbDanmaku];
      // 按时间排序
      return merged.sort((a, b) => a.time - b.time);
    },
  },
  methods: {
    limitTextLength,
    formatDate,
    async fetchDanmakuData() {
      try {
        // 1. 获取 XML 弹幕
        try {
          const responseData = await danmakuApi.getDanmakuById(this.videoId);
          if (responseData.data && responseData.data.url) {
            const url = responseData.data.url;
            const response = await fetch(url);
            const xmlText = await response.text();
            this.xmlDanmaku = parseDanmakuXml(xmlText);
          }
        } catch (xmlError) {
          console.warn("XML弹幕获取失败:", xmlError);
        }

        // 2. 获取数据库弹幕
        const dbResponse = await danmuApi.getDanmuList(this.videoId);
        this.dbDanmaku = (dbResponse.data || []).map(convertDbDanmuToFrontend);
      } catch (error) {
        console.error("获取弹幕数据失败:", error);
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
    getDanmakuType(type) {
      const types = {
        0: "滚动",
        1: "顶部",
        2: "底部",
        7: "高级",
      };
      return types[type] || "未知";
    },
  },
  watch: {
    videoId: {
      immediate: true,
      handler(newId) {
        if (newId) {
          this.fetchDanmakuData();
        }
      },
    },
  },
};
</script>

<style scoped>
.danmaku-list {
  background: #f1f2f3;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.danmaku-list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #f1f2f3;
}

.danmaku-list-header h5 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.danmaku-list-content {
  max-height: 625px;
  overflow-y: auto;
  padding: 8px 0;
}

.danmaku-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
}

.danmaku-table th,
.danmaku-table td {
  border-bottom: 1px solid #f5f5f5;
  padding: 6px 8px;
  font-size: 13px;
  text-align: left;
}

.danmaku-table th {
  background: #f8f9fa;
  color: #666;
  font-weight: 500;
}

.no-danmaku {
  text-align: center;
  color: #999;
  padding: 20px;
}

.toggle-btn {
  float: right;
  margin-top: 2px;
  margin-right: 8px;
  padding: 2px 10px;
  font-size: 13px;
  background: #e3e5e7;
  border: none;
  border-radius: 4px;
  color: #333;
  cursor: pointer;
  transition: background 0.2s;
}

.toggle-btn:hover {
  background: #d0d3d6;
}
</style>
