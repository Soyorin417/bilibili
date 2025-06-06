import request from "@/utils/request";

// 动漫API
export const animeApi = {
  // 获取所有动漫
  getAllAnime: () => {
    return request.get("/api/getAllAnime");
  },

  // 获取动漫时间表
  getAnimeSchedule: () => {
    return request.get("/getAllAnimeSchedule");
  },

  // 获取动漫详情
  getAnimeDetail: (animeId) => {
    return request.get(`/anime/${animeId}`);
  },

  // 获取动漫评论
  getAnimeComments: (animeId) => {
    return request.get(`/anime/${animeId}/comments`);
  },

  // 添加动漫评论
  addAnimeComment: (animeId, data) => {
    return request.post(`/anime/${animeId}/comments`, data);
  },

  // 删除动漫评论
  deleteAnimeComment: (animeId, commentId) => {
    return request.delete(`/anime/${animeId}/comments/${commentId}`);
  },

  // 获取动漫分类
  getAnimeCategories: () => {
    return request.get("/anime/categories");
  },

  // 获取分类下的动漫
  getAnimeByCategory: (categoryId) => {
    return request.get(`/anime/category/${categoryId}`);
  },

  // 搜索动漫
  searchAnime: (keyword) => {
    return request.get("/anime/search", {
      params: { keyword },
    });
  },

  // 获取推荐动漫
  getRecommendedAnime: () => {
    return request.get("/anime/recommended");
  },

  // 获取热门动漫
  getHotAnime: () => {
    return request.get("/anime/hot");
  },

  // 获取最新动漫
  getLatestAnime: () => {
    return request.get("/anime/latest");
  },

  // 获取动漫统计数据
  getAnimeStats: (animeId) => {
    return request.get(`/anime/${animeId}/stats`);
  },

  // 获取轮播图
  getCarouselImages: () => {
    return request.get("/api/getAllCarousel");
  },

  // 根据类型获取轮播图
  getCarouselByType: (type) => {
    return request.get("/api/getCarouselByType", {
      params: { type }
    });
  },
};

export default animeApi;
