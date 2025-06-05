<template>
  <div class="admin-sidebar">
    <h2 class="mt-4">管理员后台</h2>
    <div class="admin-nav">
      <div v-for="tab in tabs" :key="tab.id" class="nav-item">
        <button
          :class="[
            'nav-btn',
            {
              active: currentTab === tab.id,
              'has-children': tab.children && tab.children.length > 0,
            },
          ]"
          @click="handleTabClick(tab)"
        >
          <i :class="['icon', tab.icon]"></i>
          {{ tab.name }}
          <i
            v-if="tab.children && tab.children.length > 0"
            :class="['arrow', expandedTabs.includes(tab.id) ? 'expanded' : '']"
          ></i>
        </button>
        <div
          v-if="tab.children && tab.children.length > 0"
          :class="['sub-nav', { expanded: expandedTabs.includes(tab.id) }]"
        >
          <button
            v-for="child in tab.children"
            :key="child.id"
            :class="['nav-btn sub-nav-btn', { active: currentTab === child.id }]"
            @click="handleChildClick(child.id)"
          >
            {{ child.name }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminSidebar",
  props: {
    tabs: {
      type: Array,
      required: true,
    },
    currentTab: {
      type: String,
      required: true,
    },
    expandedTabs: {
      type: Array,
      required: true,
    },
  },
  methods: {
    handleTabClick(tab) {
      if (tab.children && tab.children.length > 0) {
        this.$emit("toggle-expand", tab.id);
      } else {
        this.$emit("tab-click", tab.id);
      }
    },
    handleChildClick(tabId) {
      this.$emit("tab-click", tabId);
    },
  },
};
</script>

<style scoped>
.admin-sidebar {
  width: 240px;
  background: #fff;
  padding: 24px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: fixed;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.admin-sidebar h2 {
  margin-bottom: 24px;
  font-size: 1.5rem;
  color: #333;
  text-align: center;
}

.admin-nav {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item {
  margin-bottom: 4px;
  text-align: center;
  width: 100%;
}

.nav-btn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 16px;
  border: none;
  background: none;
  color: #666;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s;
  text-align: center;
  font-size: 1rem;
}

.nav-btn .icon {
  margin-right: 8px;
  font-size: 1.1rem;
}

.nav-btn .arrow {
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #666;
  transition: transform 0.3s;
  margin-left: 8px;
}

.nav-btn .arrow.expanded {
  transform: rotate(180deg);
}

.sub-nav {
  width: 100%;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease-out;
  text-align: center;
}

.sub-nav.expanded {
  max-height: 500px;
}

.sub-nav-btn {
  padding: 8px 16px !important;
  font-size: 0.9rem !important;
  justify-content: center;
}

.nav-btn:hover {
  background: #f5f5f5;
}

.nav-btn.active {
  background: #00aeec;
  color: white;
}

.nav-btn.active .arrow {
  border-top-color: white;
}
</style>
