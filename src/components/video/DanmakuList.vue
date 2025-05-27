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
            <th>发送时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="!danmakuList || danmakuList.length === 0">
            <td colspan="3" class="no-danmaku">暂无弹幕</td>
          </tr>
          <tr v-else v-for="(danmaku, index) in danmakuList" :key="index">
            <td>{{ formatTime(danmaku.time) }}</td>
            <td>{{ danmaku.text || danmaku.content }}</td>
            <td>{{ formatSendTime(danmaku.timestamp) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "DanmakuList",
  props: {
    danmakuList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      isShow: false,
    };
  },
  methods: {
    toggleShow() {
      this.isShow = !this.isShow;
    },
    formatTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes}:${seconds.toString().padStart(2, "0")}`;
    },
    formatSendTime(timestamp) {
      if (!timestamp) return "";
      const date = new Date(timestamp * 1000); // 如果是秒级时间戳
      // const date = new Date(timestamp); // 如果是毫秒级时间戳
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const day = date.getDate().toString().padStart(2, "0");
      const hour = date.getHours().toString().padStart(2, "0");
      const minute = date.getMinutes().toString().padStart(2, "0");
      return `${month}-${day} ${hour}:${minute}`;
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

.no-danmaku {
  padding: 20px;
  text-align: center;
  color: #999;
}

.danmaku-items {
  padding: 0 16px;
}

.danmaku-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.danmaku-time {
  color: #999;
  font-size: 12px;
  margin-right: 8px;
  min-width: 45px;
}

.danmaku-content {
  font-size: 14px;
  color: #333;
  word-break: break-all;
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
