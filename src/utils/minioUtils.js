import { getPresignedUrl } from './upload';

class MinioUtils {
  // 文件限制配置
  static FILE_LIMITS = {
    IMAGE: {
      MAX_SIZE: 5 * 1024 * 1024, // 5MB
      ALLOWED_TYPES: ["image/jpeg", "image/png", "image/gif", "image/webp"],
    },
    VIDEO: {
      MAX_SIZE: 5000 * 1024 * 1024, // 5GB
      ALLOWED_TYPES: ["video/mp4", "video/webm", "video/ogg"],
    },
  };

  /**
   * 生成随机文件名
   * @param {string} prefix - 文件名前缀
   * @param {string} extension - 文件扩展名
   * @returns {string} 生成的文件名
   */
  static generateRandomFileName(prefix, extension = 'jpg') {
    const timestamp = new Date().getTime();
    const randomStr = Math.random().toString(36).substring(2, 8);
    return `${prefix}_${timestamp}_${randomStr}.${extension}`;
  }

  /**
   * 从URL中提取文件名
   * @param {string} url - 文件URL
   * @returns {string} 提取的文件名
   */
  static extractFileNameFromUrl(url) {
    if (!url) return "";
    const parts = url.split("/");
    return parts[parts.length - 1];
  }

  /**
   * 验证文件
   * @param {File} file - 文件对象
   * @param {string} type - 文件类型 ('IMAGE' 或 'VIDEO')
   * @returns {Promise<string>} 错误信息，如果没有错误返回空字符串
   */
  static validateFile(file, type = 'IMAGE') {
    return new Promise((resolve) => {
      if (!file) {
        resolve("请选择文件");
        return;
      }

      const limits = this.FILE_LIMITS[type];
      if (!limits.ALLOWED_TYPES.includes(file.type)) {
        resolve(`不支持的文件类型: ${file.type}`);
        return;
      }

      if (file.size > limits.MAX_SIZE) {
        resolve(`文件大小不能超过 ${limits.MAX_SIZE / 1024 / 1024}MB`);
        return;
      }

      resolve("");
    });
  }

  /**
   * 压缩图片
   * @param {File} file - 原始图片文件
   * @param {number} maxWidth - 最大宽度
   * @param {number} maxHeight - 最大高度
   * @param {number} quality - 压缩质量（0-1）
   * @returns {Promise<File>} 压缩后的文件
   */
  static compressImage(file, maxWidth = 800, maxHeight = 800, quality = 0.8) {
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
  }

  /**
   * 将Base64转换为File对象
   * @param {string} base64 - Base64字符串
   * @param {string} fileName - 文件名
   * @param {string} mimeType - MIME类型
   * @returns {Promise<File>} File对象
   */
  static async base64ToFile(base64, fileName, mimeType) {
    const response = await fetch(base64);
    const blob = await response.blob();
    return new File([blob], fileName, { type: mimeType });
  }

  /**
   * 上传文件到MinIO
   * @param {File} file - 要上传的文件
   * @param {Function} onProgress - 进度回调函数
   * @returns {Promise<Object>} 上传响应
   */
  static async uploadToMinIO(file = null) {
    try {
      // 获取预签名URL
      const presignedUrl = await getPresignedUrl(file.name);
      if (!presignedUrl) {
        throw new Error('获取预签名URL失败');
      }

      // 使用预签名URL上传文件
      const response = await fetch(presignedUrl, {
        method: 'PUT',
        body: file,
        headers: {
          'Content-Type': file.type,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`文件上传失败: ${errorText || response.statusText}`);
      }

      // 从预签名URL中提取基础URL（移除查询参数）
      const baseUrl = presignedUrl.split('?')[0];
      if (!baseUrl) {
        throw new Error('无法从预签名URL中提取基础URL');
      }

      return {
        fileName: file.name,
        url: baseUrl,
      };
    } catch (error) {
      console.error('文件上传失败:', error);
      throw new Error(`文件上传失败: ${error.message}`);
    }
  }

  /**
   * 将XML内容转换为File对象
   * @param {string} xmlContent - XML内容
   * @param {string} fileName - 文件名
   * @returns {File} File对象
   */
  static xmlToFile(xmlContent, fileName) {
    const blob = new Blob([xmlContent], {
      type: "application/xml;charset=UTF-8",
    });
    return new File([blob], fileName, {
      type: "application/xml;charset=UTF-8",
    });
  }
}

export default MinioUtils; 