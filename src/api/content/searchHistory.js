import request from "@/utils/request";

// 搜索历史API
export const searchHistoryApi = {
  // 添加搜索历史
  addSearchHistory: (keyword) => {
    return request.post("/api/search/history/add", null, {
      params: { keyword },
    });
  },

  // 获取搜索历史列表
  getSearchHistory: () => {
    return request.get("/api/search/history/list");
  },

  // 清空搜索历史
  clearSearchHistory: () => {
    return request.delete("/api/search/history/clear");
  },

  // 删除单条搜索历史
  deleteSearchHistory: (historyId) => {
    return request.delete("/api/search/history/delete", {
      params: { historyId },
    });
  },
};

export default searchHistoryApi; 