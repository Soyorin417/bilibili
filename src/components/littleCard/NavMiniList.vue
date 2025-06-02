<template>
  <div class="dynamic-list">
    <div class="dynamic-title ms-2">{{ title }}</div>
    <div class="dynamic-divider-with-text">
      <span class="divider-text">{{ littleTitle }}</span>
    </div>
    <VideoCard v-for="item in list" :key="item.id" :item="item" type="dynamic" />
  </div>
</template>

<script>
import VideoCard from "@/components/common/VideoCard.vue";

export default {
  name: "NavMiniList",

  components: {
    VideoCard,
  },
  props: {
    title: {
      type: String,
      required: true,
    },
    littleTitle: {
      type: String,
      required: true,
    },
    list: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.dynamic-list {
  margin: 10px 0;
  padding: 0 16px;
}

.dynamic-divider-with-text {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 8px 0 12px 0;
}

.divider-text {
  color: #b8b8b8;
  font-size: 14px;
  margin: 0 12px;
  white-space: nowrap;
}

.dynamic-divider-with-text::before,
.dynamic-divider-with-text::after {
  content: "";
  flex: 1;
  height: 1px;
  background: #e3e5e7;
}

.dynamic-title {
  font-weight: bold;
  font-size: 16px;
  color: #222;
  margin-bottom: 6px;
  margin-left: 2px;
}
</style>
