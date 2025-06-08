import { formatCount,limitTextLength ,formatDate } from "@/utils/date";


export function parseVideoCard(raw) {
    return {
      id: raw.id,
      title: raw.title || "无标题",
      author: raw.author || "未知作者",
      description: raw.description || "",
      content: limitTextLength(raw.title || raw.description || ""),
      image: raw.image || "",
      videoUrl: raw.videoUrl || "",
      duration: raw.duration || "00:00",
      views: raw.views || 0,
      comments: raw.comments || 0,
      likeCount: raw.likeCount || 0,
      shareCount: raw.shareCount || 0,
      playCountText: formatCount(raw.views || 0),
      commentCountText: formatCount(raw.comments || 0),
      formattedTime: formatDate(raw.time),
    };
  }