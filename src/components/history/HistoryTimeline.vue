<template>
  <div class="history-timeline">
    <div v-for="group in groups" :key="group.label" class="timeline-group">
      <div class="timeline-label">{{ group.label }}</div>
      <div class="timeline-line"></div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    groups: Array,
  },
};
</script>

<style scoped>
.history-timeline {
  width: 60px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-right: 16px;
}
.timeline-group {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
}
.timeline-label {
  font-size: 14px;
  color: #8a8a8a;
  margin-right: 8px;
  white-space: nowrap;
}
.timeline-line {
  width: 4px;
  height: 40px;
  background: #e3e5e7;
  border-radius: 2px;
}
</style>
