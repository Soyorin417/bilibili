import request from "@/utils/request";

// 轮播图管理API
export const carouselApi = {
  // 添加轮播图
  addCarousel: (formData) => {
    return request.post("/api/admin/carousel/add", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 获取所有轮播图
  getAllCarousels: () => {
    return request.get("/api/admin/carousel/all");
  },

  // 删除轮播图
  deleteCarousel: (id) => {
    return request.delete(`/api/admin/carousel/delete/${id}`);
  },

  // 更新轮播图
  updateCarousel: (formData) => {
    return request.put("/api/admin/carousel/update", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
};

export default carouselApi; 