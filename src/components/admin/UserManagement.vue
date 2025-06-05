<template>
  <div>
    <h4>用户管理</h4>
    <div class="search-bar mb-3">
      <input
        type="text"
        v-model="localSearchQuery"
        class="form-control"
        placeholder="搜索用户..."
        @input="handleSearch"
      />
    </div>
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>邮箱</th>
          <th>注册时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ formatDate(user.registerTime) }}</td>
          <td>
            <span
              :class="['badge', user.status === 'active' ? 'bg-success' : 'bg-danger']"
            >
              {{ user.status === "active" ? "正常" : "禁用" }}
            </span>
          </td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="handleToggleStatus(user)">
              {{ user.status === "active" ? "禁用" : "启用" }}
            </button>
            <button class="btn btn-danger btn-sm" @click="handleDelete(user.id)">
              删除
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-if="users.length === 0" class="text-muted text-center py-4">暂无用户数据</div>
  </div>
</template>

<script>
export default {
  name: "UserManagement",
  props: {
    users: {
      type: Array,
      required: true,
    },
    searchQuery: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      localSearchQuery: this.searchQuery,
    };
  },
  watch: {
    searchQuery(newValue) {
      this.localSearchQuery = newValue;
    },
  },
  methods: {
    formatDate(date) {
      return new Date(date).toLocaleString();
    },
    handleSearch() {
      this.$emit("search", this.localSearchQuery);
    },
    handleToggleStatus(user) {
      this.$emit("toggle-status", user);
    },
    handleDelete(userId) {
      this.$emit("delete", userId);
    },
  },
};
</script>

<style scoped>
.search-bar {
  max-width: 300px;
}

.table th,
.table td {
  vertical-align: middle;
  text-align: center;
}
</style>
