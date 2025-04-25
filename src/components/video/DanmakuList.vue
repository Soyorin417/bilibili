<template>
  <div class="danmaku-container mb-3" :class="{ expanded: isShow }">
    <div class="danmaku-header d-flex justify-content-between align-items-center p-1">
      <div class="d-flex align-items-center">
        <h6 class="mb-0 ms-1">弹幕列表</h6>
      </div>
      <div>
        <small class="text-secondary me-1" @click="toggleShow">
          <drop-down-list theme="outline" size="18" fill="#000000" />
        </small>
      </div>
    </div>
    <div v-if="isShow" class="danmaku-list">
      <table class="table table-sm table-hover borderless">
        <thead class="thead-light">
          <tr class="header-row">
            <th scope="col" width="10%">时间</th>
            <th scope="col" width="70%">弹幕内容</th>
            <th scope="col" width="20%">发送时间</th>
          </tr>
        </thead>
        <tbody class="small">
          <tr v-for="(item, index) in danmakuList" :key="index">
            <td>{{ item.time }}</td>
            <td class="text-truncate" style="max-width: 160px">
              {{ item.content }}
            </td>
            <td>{{ item.sendTime }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="isShow" class="danmaku-footer text-center">
      <small
        ><a href="#" class="text-decoration-none text-secondary">查看历史弹幕</a></small
      >
    </div>
  </div>
</template>

<script>
import { DropDownList } from "@icon-park/vue-next";

export default {
  name: "DanmakuList",
  components: {
    DropDownList,
  },
  props: {
    danmakuList: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      isShow: false,
    };
  },
  methods: {
    toggleShow() {
      this.isShow = !this.isShow;
    },
  },
};
</script>

<style scoped>
/* 弹幕列表样式 */
/* 弹幕列表样式 */
.danmaku-container {
  border: 1px solid #eee;
  border-radius: 3px;
  overflow: hidden;
  background-color: #fff;
  font-size: 10px;
  height: auto;
  min-height: 30px;
}

.danmaku-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
  padding: 2px 4px !important;
  min-height: 28px;
  display: flex;
  align-items: center;
}

.danmaku-header h6 {
  font-size: 13px;
  color: #333;
  margin-bottom: 0;
}

.danmaku-list {
  height: 700px;
  overflow-y: auto;
  max-height: none;
}

.danmaku-list table {
  margin-bottom: 0;
  border: none;
}

.danmaku-list th {
  font-size: 12px;
  font-weight: 600;
  color: #555;
  padding: 1px 3px;
  border: none;
  background-color: #f8f9fa;
}

.danmaku-list td {
  font-size: 12px;
  padding: 1px 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border: none;
  line-height: 1.2;
}

.danmaku-footer {
  background-color: #f8f9fa;
  border-top: 1px solid #eee;
  font-size: 9px;
  padding: 1px !important;
}

.borderless th,
.borderless td,
.borderless thead th,
.borderless tbody + tbody {
  border: none !important;
}

.table-hover > tbody > tr:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.header-row {
  border: none !important;
  background-color: #f8f9fa;
}

/* 当列表显示时，容器才应用固定高度 */
.danmaku-container.expanded {
  height: 634px;
}
</style>
