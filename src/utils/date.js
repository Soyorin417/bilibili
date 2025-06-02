// 日期格式化工具
export function formatDate(dateInput) {
  const date = dateInput instanceof Date ? dateInput : new Date(dateInput);
  if (isNaN(date.getTime())) return "";
  const pad = (n) => n.toString().padStart(2, "0");
  return (
    `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ` +
    `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
  );
}

// 数字简化显示（万、亿）
export function formatCount(num) {
  if (typeof num !== "number") num = Number(num);
  if (isNaN(num)) return "";
  if (num >= 1e8) {
    return (num / 1e8).toFixed(1).replace(/\.0$/, "") + "亿";
  } else if (num >= 1e4) {
    return (num / 1e4).toFixed(1).replace(/\.0$/, "") + "万";
  } else {
    return num.toString();
  }
}

// 限制字符串不超过8字符，超出用省略号，且不允许换行
export function limitTextLength(str, maxLen = 8) {
  if (typeof str !== "string") str = String(str);
  // 去除换行符
  str = str.replace(/[\r\n]+/g, "");
  return str.length > maxLen ? str.slice(0, maxLen) + "…" : str;
}

// 跳转到视频详情页
export function goToVideo(router, videoId) {
  if (!videoId) return;
  router.push(`/video/${videoId}`);
}


