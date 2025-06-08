import request from "@/utils/request";

export const watchApi = {
  // 记录观看历史
  recordWatch(videoId) {
    return request({
      url: "/api/watch-history/record",
      method: "post",
      params: { videoId }
    });
  },

  // 获取观看历史
  getWatchHistory() {
    return request({
      url: "/api/watch-history",
      method: "get"
    });
  }
};