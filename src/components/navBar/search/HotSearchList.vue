<template>
  <div class="search-container ms-4">
    <div class="search-wrapper" :class="{ 'is-focused': isSearchFocused }">
      <input
        type="text"
        class="search-input"
        placeholder="I really want to stay at your house"
        :value="searchQuery"
        @input="$emit('update:searchQuery', $event.target.value)"
        @focus="$emit('update:isSearchFocused', true)"
        @blur="onBlur"
      />
      <button class="search-button" @click="$emit('search')">
        <i class="bi bi-search"></i>
      </button>
    </div>

    <!-- Search Dropdown -->
    <div class="search-dropdown" v-if="isSearchFocused">
      <div class="search-history">
        <div class="history-header">
          <span>搜索历史</span>
          <span class="clear-history" @click="$emit('clearHistory')">清空</span>
        </div>
        <div class="history-tags">
          <span
            v-for="(tag, index) in searchHistory"
            :key="`history-${index}`"
            class="history-tag"
            @click="$emit('setSearchQuery', tag)"
          >
            {{ tag }}
          </span>
        </div>
      </div>

      <div class="hot-search">
        <div class="hot-search-header">
          <span>bilibili热搜</span>
        </div>
        <div class="hot-search-list-2col">
          <div
            class="hot-search-col"
            v-for="(col, colIdx) in hotSearchColumns"
            :key="colIdx"
          >
            <div
              v-for="(item, index) in col"
              :key="`hot-${colIdx}-${index}`"
              class="hot-search-item"
              @click="$emit('setSearchQuery', item.title)"
            >
              <span class="hot-number" :class="{ 'top-rank': colIdx * 5 + index < 3 }">
                {{ colIdx * 5 + index + 1 }}
              </span>
              <span class="hot-title">{{ formatHotTitle(item.title) }}</span>
              <span v-if="item.tag" class="hot-tag" :class="item.tagType">{{
                item.tag
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HotSearchList",
  props: {
    searchHistory: {
      type: Array,
      required: true,
    },
    historyTags: {
      type: Array,
      required: true,
    },
    searchQuery: {
      type: String,
      required: true,
    },
    isSearchFocused: {
      type: Boolean,
      required: true,
    },
    hotSearchItems: {
      type: Array,
      required: true,
    },
  },
  emits: [
    "update:searchQuery",
    "update:isSearchFocused",
    "search",
    "clearHistory",
    "setSearchQuery",
  ],

  methods: {
    onBlur() {
      setTimeout(() => {
        this.$emit("update:isSearchFocused", false);
      }, 200);
    },
    formatHotTitle(title) {
      if (!title) return "";
      return title.length > 8 ? title.slice(0, 8) + "..." : title;
    },
  },
  computed: {
    hotSearchColumns() {
      const items = this.hotSearchItems.slice(0, 10);
      const col1 = items.slice(0, 5);
      const col2 = items.slice(5, 10);
      return [col1, col2];
    },
  },
};
</script>

<style scoped>
.search-container {
  position: relative;
  width: 450px;
  z-index: 100;
}

.search-wrapper {
  display: flex;
  align-items: center;
  border: 1px solid #e3e5e7;
  border-radius: 8px;
  padding: 0 0.5rem;
  height: 36px;
  background-color: #f1f2f3;
  transition: all 0.3s ease;
}

.search-wrapper.is-focused {
  border-color: var(--bs-primary, #0d6efd);
  background-color: #fff;
  box-shadow: 0 0 5px rgba(13, 110, 253, 0.2);
}

.search-input {
  flex-grow: 1;
  border: none;
  background-color: transparent;
  padding: 0.5rem;
  font-size: 0.9rem;
  outline: none;
  color: #333;
}

.search-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.search-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  margin-top: 0.5rem;
  z-index: 1000;
  max-height: 500px;
  overflow-y: auto;
  border: 1px solid #e3e5e7;
  animation: dropdownFade 0.2s ease-in-out;
}

.search-history,
.hot-search {
  padding: 1rem;
}

.hot-search {
  border-top: 1px solid #e3e5e7;
}

.history-header,
.hot-search-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.8rem;
  color: #666;
  font-size: 0.9rem;
  font-weight: 500;
}

.clear-history {
  color: #0cb1ec;
  cursor: pointer;
}

.history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
}

.history-tag {
  background-color: #f6f7f8;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.8rem;
  color: #666;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.hot-search-list-2col {
  display: flex;
  gap: 24px;
}

.hot-search-col {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.hot-search-item {
  text-align: left;
  cursor: pointer;
}

.hot-number {
  width: 20px;
  text-align: center;
  font-weight: 600;
  color: #999;
  margin-right: 0.8rem;
}

.hot-number.top-rank {
  color: #f9716c;
}

.hot-title {
  flex-grow: 1;
  font-size: 0.9rem;
}

.hot-tag {
  padding: 0.1rem 0.4rem;
  border-radius: 2px;
  font-size: 0.7rem;
  margin-left: 0.5rem;
  font-size: 10px;
  display: inline-block;
}

.hot-tag-red {
  background-color: #f9716c;
  color: #fff;
}

.hot-tag-new {
  background-color: #00a1d6;
  color: #fff;
}

@keyframes dropdownFade {
  from {
    opacity: 0;
    transform: translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.container-fluid {
  display: flex;
  align-items: center;
}
.flex-grow-1 {
  flex-grow: 1;
  display: flex;
  justify-content: center;
}
.flex-shrink-0 {
  flex-shrink: 0;
}
</style>
