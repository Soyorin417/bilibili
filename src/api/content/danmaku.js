import request from "@/utils/request";


export const danmakuApi = {
  // 获取本地弹幕列表
  getDanmakuById: (id) => {
    return request.get(`/danmaku/getDanmakuList?id=${id}`);
  },

  // 获取B站弹幕
  getBilibiliDanmaku: (oid) => {
    return request.get(`/danmaku/getBilibiliDanmaku?oid=${oid}`);
  }
};