<template>
  <div>
    <img :src="url" class="card" alt="" />
    <div>{{ title }}</div>
    <div>{{ message }}</div>
  </div>
</template>

<script>
export default {
  name: "VideoCard",
  props: {
    url: {
      type: String,
      required: true,
    },
    title: {
      type: String,
      default: "默认标题",
    },
    message: {
      type: String,
      default: "默认消息",
    },
  },
};
</script>

<style scoped>
.card {
  width: 320px;
  height: 160px;
}
</style>
