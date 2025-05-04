<template>
  <div class="sidebar">
    <div class="sidebar-title">消息中心</div>
    <ul>
      <li
        v-for="item in items"
        :key="item"
        :class="{ active: item === active }"
        @click="active = item"
      >
        {{ item }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "MessageNav",
  data() {
    return {
      items: ["我的消息", "回复我的", "@我的", "收到的赞", "系统通知", "消息设置"],
      active: "我的消息",
    };
  },
};
</script>

<style scoped>
.sidebar {
  padding: 16px 0;
}
.sidebar-title {
  padding: 0 24px 16px 24px;
  font-weight: bold;
  color: #222;
  font-size: 18px;
}
ul {
  list-style: none;
  padding: 0 0 0 8px;
  margin: 0;
}
li {
  padding: 12px 24px;
  cursor: pointer;
  color: #555;
  border-radius: 6px 0 0 6px;
  margin-bottom: 4px;
  transition: background 0.2s;
}
li.active {
  background: #fff;
  color: #00a1d6;
  font-weight: bold;
}
</style>
