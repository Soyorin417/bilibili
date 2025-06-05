import axios from "axios";

// 创建上传专用的 axios 实例
const uploadInstance = axios.create({
  baseURL: "http://localhost:8081",
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 添加请求拦截器
uploadInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 文件大小限制（5MB）
const MAX_FILE_SIZE = 5 * 1024 * 1024;

// 允许的文件类型
const ALLOWED_TYPES = ["image/jpeg", "image/png", "image/gif", "image/webp"];

/**
 * 检查文件是否符合要求
 * @param {File} file 文件对象
 * @returns {Promise<string>} 错误信息，如果没有错误返回空字符串
 */
export const validateFile = (file) => {
  return new Promise((resolve) => {
    if (!file) {
      resolve("请选择文件");
      return;
    }

    if (!ALLOWED_TYPES.includes(file.type)) {
      resolve("只支持 JPG、PNG、GIF、WEBP 格式的图片");
      return;
    }

    if (file.size > MAX_FILE_SIZE) {
      resolve("文件大小不能超过 5MB");
      return;
    }

    resolve("");
  });
};

/**
 * 压缩图片
 * @param {File} file 原始图片文件
 * @param {number} maxWidth 最大宽度
 * @param {number} maxHeight 最大高度
 * @param {number} quality 压缩质量（0-1）
 * @returns {Promise<File>} 压缩后的文件
 */
export const compressImage = (file, maxWidth = 800, maxHeight = 800, quality = 0.8) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (e) => {
      const img = new Image();
      img.src = e.target.result;
      img.onload = () => {
        const canvas = document.createElement("canvas");
        let width = img.width;
        let height = img.height;

        // 计算缩放比例
        if (width > height) {
          if (width > maxWidth) {
            height = Math.round((height * maxWidth) / width);
            width = maxWidth;
          }
        } else {
          if (height > maxHeight) {
            width = Math.round((width * maxHeight) / height);
            height = maxHeight;
          }
        }

        canvas.width = width;
        canvas.height = height;

        const ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, width, height);

        // 转换为文件
        canvas.toBlob(
          (blob) => {
            const compressedFile = new File([blob], file.name, {
              type: file.type,
              lastModified: Date.now(),
            });
            resolve(compressedFile);
          },
          file.type,
          quality
        );
      };
      img.onerror = reject;
    };
    reader.onerror = reject;
  });
};

/**
 * 获取预签名URL
 * @param {string} fileName - 文件名
 * @returns {Promise<string>} 预签名URL
 */
export const getPresignedUrl = async (fileName) => {
  try {
    const response = await uploadInstance.post('/api/upload/presigned-url', {
      fileName: fileName
    });
    return response.data.url;
  } catch (error) {
    console.error('获取预签名URL失败:', error);
    throw new Error('获取预签名URL失败');
  }
};

/**
 * 上传文件到服务器
 * @param {File} file - 文件对象
 * @param {string} url - 上传地址
 * @param {Object} additionalData - 额外的表单数据
 * @param {Function} onProgress - 上传进度回调
 * @returns {Promise<any>} 上传结果
 */
export const uploadFile = async (file, url, additionalData = {}, onProgress = null) => {
  try {
    const formData = new FormData();
    formData.append("file", file);

    // 添加额外数据
    Object.entries(additionalData).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        formData.append(key, value);
      }
    });

    // 上传文件
    const response = await uploadInstance.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress) {
          const percentCompleted = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          onProgress(percentCompleted);
        }
      },
    });

    return response.data;
  } catch (error) {
    console.error("文件上传失败:", error);
    throw error;
  }
};

/**
 * 上传用户头像
 * @param {File} file - 头像文件
 * @param {Object} userInfo - 用户信息
 * @param {Function} onProgress - 上传进度回调
 * @returns {Promise<any>} 上传结果
 */
export const uploadAvatar = async (file, userInfo = {}, onProgress = null) => {
  return uploadFile(
    file,
    "/api/user/update/avatar",
    {
      updateInfo: new Blob([JSON.stringify(userInfo)], {
        type: "application/json",
      }),
    },
    onProgress
  );
};
