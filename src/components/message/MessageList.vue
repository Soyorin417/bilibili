<template>
  <div>
    <div
      v-for="session in sessions"
      :key="session.id"
      :class="['session', { active: session === selected }]"
      @click="$emit('select', session)"
    >
      <img :src="session.avatar" class="avatar" />
      <div class="info">
        <div class="name">{{ session.name }}</div>
        <div class="summary">{{ session.summary }}</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: ["sessions", "selected"],
};
</script>
<style scoped>
.session {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
}
.session.active {
  background: #f0faff;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}
.info {
  flex: 1;
}
.name {
  font-weight: bold;
}
.summary {
  color: #888;
  font-size: 13px;
}
</style>
