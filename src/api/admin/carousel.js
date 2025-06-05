import axios from "axios";

// 创建axios实例
const apiInstance = axios.create({
  baseURL: "http://localhost:8081",
  timeout: 15000,
});

// 请求拦截器
apiInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
apiInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // token过期或无效，清除本地存储并跳转到登录页
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// 获取所有轮播图
export function getAllCarousel() {
  return apiInstance.get("/api/admin/carousel/list");
}

// 添加轮播图
export function addCarousel(formData) {
  return apiInstance.post("/api/admin/carousel/add", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    params: {
      fileName: formData.get("image").name,
    },
  });
}

// 更新轮播图
export function updateCarousel(id, formData) {
  return apiInstance.put(`/api/admin/carousel/update/${id}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
    params: {
      fileName: formData.get("image").name,
    },
  });
}

// 删除轮播图
export function deleteCarousel(id) {
  return apiInstance.delete(`/api/admin/carousel/delete/${id}`);
}

// 获取单个轮播图详情
export function getCarouselById(id) {
  return apiInstance.get(`/api/admin/carousel/${id}`);
}
