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

// 动漫API
export const animeApi = {
  // 获取所有动漫
  getAllAnime: () => {
    return api.get("/getAllAnime");
  },

  // 获取动漫时间表
  getAnimeSchedule: () => {
    return api.get("/getAllAnimeSchedule");
  },

  // 获取动漫详情
  getAnimeDetail: (animeId) => {
    return api.get(`/anime/${animeId}`);
  },

  // 获取动漫评论
  getAnimeComments: (animeId) => {
    return api.get(`/anime/${animeId}/comments`);
  },

  // 添加动漫评论
  addAnimeComment: (animeId, data) => {
    return api.post(`/anime/${animeId}/comments`, data);
  },

  // 删除动漫评论
  deleteAnimeComment: (animeId, commentId) => {
    return api.delete(`/anime/${animeId}/comments/${commentId}`);
  },

  // 获取动漫分类
  getAnimeCategories: () => {
    return api.get("/anime/categories");
  },

  // 获取分类下的动漫
  getAnimeByCategory: (categoryId) => {
    return api.get(`/anime/category/${categoryId}`);
  },

  // 搜索动漫
  searchAnime: (keyword) => {
    return api.get("/anime/search", {
      params: { keyword },
    });
  },

  // 获取推荐动漫
  getRecommendedAnime: () => {
    return api.get("/anime/recommended");
  },

  // 获取热门动漫
  getHotAnime: () => {
    return api.get("/anime/hot");
  },

  // 获取最新动漫
  getLatestAnime: () => {
    return api.get("/anime/latest");
  },

  // 获取动漫统计数据
  getAnimeStats: (animeId) => {
    return api.get(`/anime/${animeId}/stats`);
  },

  // 获取轮播图
  getCarouselImages: () => {
    return api.get("/getAllCarousel");
  },
};

export default animeApi;
