import request from "@/utils/request";

export const watchApi = {
  // 记录观看历史
  recordWatch: (videoId) => {
    return request.post("/api/watch-history/record", null, { params: { videoId } });
  },

  // 获取观看历史
  getWatchHistory: () => {
    return request.get("/api/watch-history");
  },

  // 删除单个观看记录
  deleteOne: (videoId) => {
    return request.delete(`/api/watch-history/${videoId}`);
  },

  // 清空所有观看历史
  clearAll: () => {
    return request.delete("/api/watch-history/clear");
  },

  // 更新观看时间
  updateTime: (videoId) => {
    return request.put("/api/watch-history/update", null, { params: { videoId } });
  },
};
