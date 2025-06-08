import request from "@/utils/request";

export function recordWatch(videoId) {
  return request.post("/api/watch-history/record", {
    params: {  videoId }
  });
}

export function getWatchHistory() {
  return request.get("/api/watch-history/");
}