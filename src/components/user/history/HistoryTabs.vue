<template>
  <div class="history-tabs">
    <div
      v-for="tab in tabs"
      :key="tab"
      :class="['tab', { active: tab === activeTab }]"
      @click="$emit('change', tab)"
    >
      {{ tab }}
    </div>
    <el-dropdown>
      <span class="el-dropdown-link"> 更多筛选 <i class="bi bi-chevron-down"></i> </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item>专栏</el-dropdown-item>
          <el-dropdown-item>课程</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  props: {
    activeTab: String,
  },
  data() {
    return {
      tabs: ["综合", "视频", "直播", "专栏"],
    };
  },
};
</script>

<style scoped>
.history-tabs {
  display: flex;
  align-items: center;
  gap: 24px;
  margin: 24px 0 16px 0;
  font-size: 16px;
}
.tab {
  cursor: pointer;
  padding-bottom: 6px;
  border-bottom: 2px solid transparent;
  color: #61666d;
  transition: all 0.2s;
}
.tab.active {
  color: #00aeec;
  border-bottom: 2px solid #00aeec;
  font-weight: bold;
}
.el-dropdown-link {
  cursor: pointer;
  color: #61666d;
  margin-left: 12px;
}
</style>
