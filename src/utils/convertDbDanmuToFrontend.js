export function convertDbDanmuToFrontend(d) {
  // 统一颜色格式
  let color = d.fontColor || "#ffffff";
  if (!color.startsWith("#")) color = "#" + color;

  const type = d.mode;
  const fontSize = d.fontSize ?? 25;
  const timestamp = d.sendTime ?? 0;
  const pool = d.pool ?? 0;
  const userHash = d.userHash || "";
  const rowID = d.dmid || "";
  const track = typeof d.track === "number" ? d.track : Math.floor(Math.random() * 10);
  const text = d.text?.trim() || "";

  let isAdvanced = type === 7;
  let position = null;

  const trackHeight = 100 / 10; // 假设轨道划分为 10 条

  if (isAdvanced && d.position && typeof d.position === "object") {
    // 使用 position 对象作为高级弹幕定义
    const scaleX = d.position.scaleX ?? 1;
    const scaleY = d.position.scaleY ?? 1;
    const font = d.position.font || d.font || "SimHei";
    const fontFamily = d.position.fontFamily || font;
    const alpha = d.position.alpha ?? 1;
    const rotation = d.position.rotation ?? 0;

    position = {
      x: d.position.x ?? 0,
      y: d.position.y ?? 0,
      scaleX,
      scaleY,
      duration: d.duration ?? d.position.duration ?? 8,
      alpha,
      rotation,
      size: fontSize,
      color,
      type,
      font,
      style: d.position.style ?? 1,
      fontFamily,
      isAdvanced: true,
    };
  } else {
    // 普通弹幕：按类型决定位置
    let y;
    if (type === 1) {
      // 滚动弹幕
      y = track * trackHeight + trackHeight / 2;
    } else if (type === 5) {
      // 顶部弹幕
      y = 10;
    } else if (type === 4) {
      // 底部弹幕
      y = 90;
    } else {
      y = 50; // 其他类型默认居中
    }

    position = {
      x: type === 1 ? 100 : 50, // 滚动弹幕从右侧开始，其他弹幕居中
      y,
      duration: d.duration ?? 8,
      font: d.font || "SimHei",
      fontFamily: d.font || "SimHei",
      scaleX: 1,
      scaleY: 1,
      alpha: 1,
      rotation: 0,
      isAdvanced: false,
    };

    isAdvanced = false;
  }

  return {
    text,
    time: d.timeInVideo,
    type,
    color,
    fontSize,
    timestamp,
    pool,
    userHash,
    rowID,
    track,
    isAdvanced,
    position,
    duration: position?.duration ?? 8,
    font: position?.font ?? "SimHei",
    isScrolling: type === 1 || type === 7, // 滚动或高级弹幕
    isSelf: false, // 根据当前登录用户可以设置
  };
}
