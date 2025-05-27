import axios from 'axios';

// 创建axios实例
const api = axios.create({
  baseURL: 'http://127.0.0.1:8081',
  timeout: 5000
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 动态和轮播图API
export const activityApi = {
  // 获取右侧动态列表
  getNavbarDynamics: () => {
    return api.get('/api/activity/right');
  },

  // 获取轮播图列表
  getCarouselImages: () => {
    return api.get('/getAllCarousel');
  },

  // 添加轮播图
  addCarouselImage: (formData) => {
    return api.post('/addCarousel', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  },

  // 删除轮播图
  deleteCarouselImage: (imageId) => {
    return api.delete(`/deleteCarousel/${imageId}`);
  },

  // 更新轮播图顺序
  updateCarouselOrder: (orderData) => {
    return api.put('/updateCarouselOrder', orderData);
  }
};

export default activityApi;

