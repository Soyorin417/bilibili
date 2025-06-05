import request from "@/utils/request";
// 获取所有标签
export function getAllTags() {
  return request.get('/api/tags');
}

// 根据视频ID获取标签
export function getTagsByVideoId(videoId) {
  return request.get(`/api/tags/video/${videoId}`);
}

// 添加标签
export function createTag(tag) {
  return request.post('/api/tags', tag);
}

// 更新标签
export function updateTag(tag) {
  return request.put('/api/tags', tag);
}

// 删除标签
export function deleteTag(id) {
  return request.delete(`/api/tags/${id}`);
}

// 添加标签到视频
export function addTagToVideo(videoId, tagId) {
  return request.post('/api/video-tags/add', null, { params: { videoId, tagId } });
}

// 移除标签
export function removeTagFromVideo(videoId, tagId) {
  return request.post('/api/video-tags/remove', null, { params: { videoId, tagId } });
}
