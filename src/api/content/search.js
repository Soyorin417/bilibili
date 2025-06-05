import request from "@/utils/request";

// 搜索API
export const searchApi = {
  // 搜索视频
  searchVideo: (keyword) => {
    return request.get("/api/search/video", {
      params: { keyword },
    });
  },

  // 搜索用户
  searchUser: (keyword, uid) => {
    return request.get("/api/search/user", {
      params: { keyword, uid },
    });
  },

  // 获取热门搜索
  getHotSearches: () => {
    return request.get("/search/hot");
  },

  // 获取搜索历史
  getSearchHistory: () => {
    return request.get("/search/history");
  },

  // 清除搜索历史
  clearSearchHistory: () => {
    return request.delete("/search/history");
  },

  // 获取搜索建议
  getSearchSuggestions: (keyword) => {
    return request.get("/search/suggestions", {
      params: { keyword },
    });
  },

  // 获取搜索结果统计
  getSearchStats: (keyword) => {
    return request.get("/search/stats", {
      params: { keyword },
    });
  },
};

export default searchApi;
