import request from '@/utils/request';

/**
 * 弹幕相关的API服务
 */
const danmuApi = {
  /**
   * 添加弹幕
   * @param {Object} danmu - 弹幕对象
   * @param {number} danmu.videoId - 视频ID
   * @param {string} danmu.content - 弹幕内容
   * @param {number} danmu.timeInVideo - 弹幕在视频中的时间点（秒）
   * @param {string} danmu.color - 弹幕颜色
   * @param {number} danmu.type - 弹幕类型（0: 滚动, 1: 顶部, 2: 底部）
   * @returns {Promise} 返回添加结果
   */
  addDanmu(danmu) {
    return request({
      url: '/danmu/add',
      method: 'post',
      data: danmu
    });
  },

  /**
   * 获取视频的弹幕列表
   * @param {number} videoId - 视频ID
   * @returns {Promise<Array>} 返回弹幕列表，按时间点升序排序
   */
  getDanmuList(videoId) {
    return request({
      url: `/danmu/list/${videoId}`,
      method: 'get'
    });
  }
};

export default danmuApi; 