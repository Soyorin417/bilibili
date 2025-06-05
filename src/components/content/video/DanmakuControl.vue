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
        <option value="scroll">滚动</option>
        <option value="fixed">固定</option>
      </select>
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
export default {
  name: "DanmakuControl",
  data() {
    return {
      danmakuText: "",
      currentDanmakuType: "scroll",
      showDanmaku: true,
    };
  },
  methods: {
    sendDanmaku() {
      if (!this.danmakuText.trim()) return;

      this.$emit("send-danmaku", {
        content: this.danmakuText,
        type: this.currentDanmakuType,
      });

      this.danmakuText = "";
    },
  },
  watch: {
    showDanmaku(newValue) {
      this.$emit("toggle-danmaku", newValue);
    },
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

.form-check-label {
  color: white;
  font-size: 13px;
}
</style>
