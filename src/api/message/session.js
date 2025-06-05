import request from "@/utils/request";

// 会话API
export const sessionApi = {
  // 创建会话
  createSession: (data) => request.post("/api/sessions", data),

  // 获取所有会话列表（不推荐用）
  getSessions: () => request.get("/api/sessions"),

  // 根据用户ID获取会话列表
  getSessionsByUserId: (userId) => request.get("/api/sessions/byUserId", { params: { userId } }),

  // 获取单个会话
  getSession: (id) => request.get(`/api/sessions/${id}`),

  // 删除会话
  deleteSession: (id) => request.delete(`/api/sessions/${id}`),
};

export default sessionApi;
