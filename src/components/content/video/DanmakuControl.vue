<template>
  <div class="danmaku-control-bar mt-2">
    <input
      v-model="danmakuText"
      class="danmaku-input me-2"
      placeholder="发个友善的弹幕吧~"
      @keyup.enter="sendDanmaku"
    />

    <!-- 弹幕类型选择 -->
    <div class="danmaku-type-selector me-2">
      <select v-model="currentDanmakuType" class="form-select form-select-sm">
        <option :value="1">滚动</option>
        <option :value="4">顶部</option>
        <option :value="5">底部</option>
      </select>
    </div>

    <!-- 弹幕颜色选择 -->
    <div class="danmaku-color-selector me-2">
      <input
        type="color"
        v-model="currentColor"
        class="form-control form-control-sm"
        title="选择弹幕颜色"
      />
    </div>

    <button class="btn btn-sm btn-primary" @click="sendDanmaku">发送</button>
    <div class="form-check form-switch ms-2">
      <input
        class="form-check-input"
        type="checkbox"
        v-model="showDanmaku"
        id="danmakuSwitch"
      />
      <label class="form-check-label" for="danmakuSwitch">弹幕</label>
    </div>
  </div>
</template>

<script>
import danmuApi from "@/api/danmu";

export default {
  name: "DanmakuControl",
  props: {
    videoId: {
      type: String,
      required: true,
    },
    currentTime: {
      type: Number,
      default: 0,
    },
  },
  data() {
    console.log("DanmakuControl data initialized");
    return {
      danmakuText: "",
      currentDanmakuType: 1,
      currentColor: "#ffffff",
      showDanmaku: true,
    };
  },
  methods: {
    hexToNumber(hex) {
      hex = hex.replace("#", "");
      return parseInt(hex, 16);
    },

    getRndInteger(min, max) {
      return Math.floor(Math.random() * (max - min + 1)) + min;
    },

    async sendDanmaku() {
      if (!this.danmakuText.trim()) return;

      if (!this.videoId) {
        console.error("Cannot send danmaku: videoId is missing");
        alert("视频ID缺失，无法发送弹幕");
        return;
      }

      console.log("Sending danmaku with currentTime:", this.currentTime);

      const mode = this.currentDanmakuType;
      const fontSize = 25;

      let positionX = 50;
      let positionY = 50;
      let track = 0;

      //0高0低
      if (mode === 1) {
        // 滚动弹幕
        track = this.getRndInteger(0, 8);
        positionX = 100; // 从右边出现
        positionY = track * 10 + 5;
      } else if (mode === 5) {
        // 顶部弹幕
        track = this.getRndInteger(5, 8);
        positionX = 50;
        positionY = 10; // 靠近顶部
      } else if (mode === 4) {
        // 底部弹幕
        track = this.getRndInteger(0, 4);
        positionX = 50;
        positionY = 90;
      } else {
        track = 0;
        positionX = 50;
        positionY = 50;
      }
      // 组装对象
      const danmu = {
        videoId: this.videoId,
        text: this.danmakuText.trim(),
        timeInVideo: this.currentTime,
        mode,
        fontColor: this.currentColor,
        fontSize,
        status: 1,
        duration: 8.0,
        font: "SimHei",
        pool: 0,
        isAdvanced: mode === 7,
        isScrolling: mode === 1,
        track, // 所有类型都使用 track
        positionX,
        positionY,
      };

      console.log("Emitting send-danmaku event with data:", danmu);

      // 立即显示弹幕
      this.$emit("send-danmaku", {
        ...danmu,
        isSelf: true, // 标记为自己发送的弹幕
        textDecoration: "underline", // 添加下划线
      });
      this.danmakuText = "";

      // 异步发送到后端
      try {
        const response = await danmuApi.addDanmu(danmu);
        console.log("Danmu API response:", response);

        if (response.data !== "发送成功") {
          console.warn("Danmu send failed:", response.data);
        }
      } catch (error) {
        console.error("发送弹幕失败:", error);
        console.error("Error details:", {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status,
        });
      }
    },
  },
  watch: {
    showDanmaku(newValue) {
      this.$emit("toggle-danmaku", newValue);
    },
    videoId(newValue) {
      if (!newValue) {
        console.warn("videoId is missing or invalid");
      }
    },
  },
  created() {
    if (!this.videoId) {
      console.error("DanmakuControl: videoId prop is required but not provided");
    }
  },
};
</script>

<style scoped>
/* 弹幕控制条 */
.danmaku-control-bar {
  display: flex;
  align-items: center;
  padding: 8px;
  background-color: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 20px;
  position: relative;
  z-index: 1;
}

.danmaku-input {
  flex: 1;
  height: 30px;
  border-radius: 15px;
  border: 1px solid #ddd;
  padding: 0 15px;
  font-size: 13px;
  color: #333;
  background-color: #fff;
}

.danmaku-type-selector select {
  height: 30px;
  border-radius: 4px;
  font-size: 12px;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
}

.danmaku-color-selector input[type="color"] {
  width: 40px;
  height: 30px;
  padding: 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.form-check-label {
  color: white;
  font-size: 13px;
}
</style>
