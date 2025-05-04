<template>
  <div>
    <div v-if="session">
      <div v-for="msg in session.messages" :key="msg.id" class="msg-block">
        <div class="msg-time">{{ msg.time }}</div>
        <div class="msg-content">{{ msg.content }}</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: ["session"],
};
</script>
<style scoped>
.msg-block {
  margin: 24px 0;
}
.msg-time {
  color: #aaa;
  font-size: 12px;
  margin-bottom: 4px;
}
.msg-content {
  background: #f6fafd;
  border-radius: 8px;
  padding: 16px;
}
</style>
