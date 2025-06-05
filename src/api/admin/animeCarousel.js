import request from "@/utils/request";

export const animeCarouselApi = {
  // 获取所有轮播图
  getAllCarousel: () => {
    return request.get("/api/admin/carousel/list");
  },

  // 添加轮播图
  addCarousel: (formData) => {
    return request.post("/api/admin/carousel/add", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      params: {
        fileName: formData.get("image").name,
      },
    });
  },

  // 更新轮播图
  updateCarousel: (id, formData) => {
    return request.put(`/api/admin/carousel/update/${id}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 删除轮播图
  deleteCarousel: (id) => {
    return request.delete(`/api/admin/carousel/delete/${id}`);
  },

  // 获取单个轮播图详情
  getCarouselById: (id) => {
    return request.get(`/api/admin/carousel/${id}`);
  },
};

export default animeCarouselApi;
