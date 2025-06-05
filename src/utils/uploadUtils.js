import uploadApi from "@/api/upload/upload";
import userApi from "@/api/user/user";

class UploadUtils {
  static generateRandomFileName(prefix) {
    const timestamp = new Date().getTime();
    const randomStr = Math.random().toString(36).substring(2, 8);
    return `${prefix}_${timestamp}_${randomStr}.jpg`;
  }

  static extractFileNameFromUrl(url) {
    if (!url) return "";
    const parts = url.split("/");
    return parts[parts.length - 1];
  }

  static async uploadFileToMinIO(file, onProgress = null) {
    const formData = new FormData();
    formData.append("file", file);

    const response = await uploadApi.uploadToMinIO(formData, onProgress);

    if (response.status !== 200) {
      throw new Error("文件上传失败");
    }

    return response.data;
  }

  static async uploadVideo(videoFile, onProgress = null) {
    try {
      const videoResponse = await this.uploadFileToMinIO(videoFile, onProgress);

      if (typeof videoResponse === "object" && videoResponse !== null) {
        const { fileName } = videoResponse;
        const videoFileName = this.extractFileNameFromUrl(fileName);

        if (!videoFileName || videoFileName === "undefined") {
          throw new Error("无法获取有效的视频文件名");
        }

        return videoFileName;
      }

      throw new Error("视频上传返回数据格式不正确");
    } catch (error) {
      console.error("视频上传失败:", error);
      throw error;
    }
  }

  static async uploadCover(coverBase64) {
    try {
      if (!coverBase64) {
        return this.generateRandomFileName("default_cover");
      }

      const coverBlob = await fetch(coverBase64).then((r) => r.blob());
      const coverFileName = this.generateRandomFileName("cover");
      const coverFile = new File([coverBlob], coverFileName, {
        type: coverBlob.type,
      });

      const coverResponse = await this.uploadFileToMinIO(coverFile);

      if (typeof coverResponse === "object" && coverResponse !== null) {
        const { fileName: coverFileNameRaw } = coverResponse;
        const coverFileName = this.extractFileNameFromUrl(coverFileNameRaw);

        if (!coverFileName || coverFileName === "undefined") {
          return this.generateRandomFileName("default_cover");
        }

        return coverFileName;
      }

      return this.generateRandomFileName("default_cover");
    } catch (error) {
      console.error("封面上传失败:", error);
      return this.generateRandomFileName("default_cover");
    }
  }

  static async uploadDanmaku(xmlContent, cid) {
    try {
      const xmlBlob = new Blob([xmlContent], {
        type: "application/xml;charset=UTF-8",
      });
      const xmlFile = new File([xmlBlob], `danmaku_${cid}.xml`, {
        type: "application/xml;charset=UTF-8",
      });

      const response = await this.uploadFileToMinIO(xmlFile);
      return this.extractFileNameFromUrl(response.fileName);
    } catch (error) {
      console.error("弹幕上传失败:", error);
      throw error;
    }
  }

  static async submitVideoInfo(videoInfo, userInfo) {
    const formData = new FormData();
    formData.append("title", videoInfo.title);
    formData.append("videoFileName", videoInfo.videoFileName);
    formData.append("coverFileName", videoInfo.coverFileName);
    formData.append("description", videoInfo.description || "");
    formData.append("authorId", Number(userInfo?.id));
    formData.append("avatar", userInfo?.avatar || "");
    formData.append("duration", String(videoInfo.duration));
    formData.append("danmakuFileName", videoInfo.danmakuFileName || "");

    const response = await uploadApi.submitVideo(formData);
    if (response.status !== 200) {
      throw new Error(response.data || "提交失败");
    }

    return response.data;
  }

  /**
   * 上传用户头像
   * @param {string} avatarBase64 - 头像的Base64字符串
   * @param {Object} userInfo - 用户信息
   * @param {Function} onProgress - 进度回调函数
   * @returns {Promise<Object>} 更新后的用户信息
   */
  static async uploadAvatar(avatarBase64, userInfo, onProgress = null) {
    try {
      if (!avatarBase64) {
        throw new Error("头像数据不能为空");
      }

      // 将Base64转换为Blob
      const avatarBlob = await fetch(avatarBase64).then((r) => r.blob());
      const avatarFileName = this.generateRandomFileName("avatar");
      const avatarFile = new File([avatarBlob], avatarFileName, {
        type: avatarBlob.type,
      });

      // 上传头像文件
      const avatarResponse = await this.uploadFileToMinIO(avatarFile, onProgress);

      if (typeof avatarResponse === "object" && avatarResponse !== null) {
        const { fileName } = avatarResponse;
        const avatarFileName = this.extractFileNameFromUrl(fileName);

        if (!avatarFileName || avatarFileName === "undefined") {
          throw new Error("无法获取有效的头像文件名");
        }

        // 更新用户信息
        const updateInfo = {
          ...userInfo,
          avatar: avatarFileName,
        };

        const response = await userApi.updateUser(updateInfo);
        return response;
      }

      throw new Error("头像上传返回数据格式不正确");
    } catch (error) {
      console.error("头像上传失败:", error);
      throw error;
    }
  }

  /**
   * 更新用户信息
   * @param {Object} userInfo - 要更新的用户信息
   * @returns {Promise<Object>} 更新后的用户信息
   */
  static async updateUserInfo(userInfo) {
    try {
      const response = await userApi.updateUser(userInfo);
      return response;
    } catch (error) {
      console.error("更新用户信息失败:", error);
      throw error;
    }
  }
}

export default UploadUtils;
