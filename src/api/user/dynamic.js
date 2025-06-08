import request from "@/utils/request";

export const dynamicApi = {
  // 获取用户动态视频列表
  getUserDynamics: (userId) => {
    return request.get("/api/dynamic/list",{params: { userId }});
  },
};
