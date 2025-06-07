import request from "@/utils/request";

// 热搜API
export const hotSearchApi = {
  // 获取热搜榜
  getHotSearches: (limit = 10) => {
    return request.get("/api/hot/top", {
      params: { limit },
    });
  },

  // 增加热搜词搜索次数
  incrementHotSearch: (keyword) => {
    return request.post("/api/hot/add", { keyword });
  },
};

export default hotSearchApi;
