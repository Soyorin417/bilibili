import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://127.0.0.1:8081",
  timeout: 5000,
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// 搜索API
export const searchApi = {
  // 搜索视频
  searchVideo: (keyword) => {
    return api.get("/api/search/video", {
      params: { keyword },
    });
  },

  // 搜索用户
  searchUser: (keyword, uid) => {
    return api.get("/api/search/user", {
      params: { keyword, uid },
    });
  },

  // 获取热门搜索
  getHotSearches: () => {
    return api.get("/search/hot");
  },

  // 获取搜索历史
  getSearchHistory: () => {
    return api.get("/search/history");
  },

  // 清除搜索历史
  clearSearchHistory: () => {
    return api.delete("/search/history");
  },

  // 获取搜索建议
  getSearchSuggestions: (keyword) => {
    return api.get("/search/suggestions", {
      params: { keyword },
    });
  },

  // 获取搜索结果统计
  getSearchStats: (keyword) => {
    return api.get("/search/stats", {
      params: { keyword },
    });
  },
};

export default searchApi;
