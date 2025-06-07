export function convertDbDanmuToFrontend(d) {
  // 统一颜色格式
  let color = d.fontColor || "#ffffff";
  if (!color.startsWith("#")) {
    color = "#" + color;
  }

  // 设置弹幕类型
  const type = d.isAdvanced
    ? 7
    : d.isScrolling === false
    ? d.positionY <= 20
      ? 1
      : 2 // 用 Y 坐标判断是顶部还是底部
    : 0;

  // 高级弹幕位置和样式信息
  let position = null;

  if (d.isAdvanced) {
    position = {
      x: d.positionX ?? 50,
      y: d.positionY ?? 10,
      duration: d.duration ?? 8.0,
      scaleX: d.scaleX ?? 1,
      scaleY: d.scaleY ?? 1,
      rotation: d.rotation ?? 0,
      alpha: d.alpha ?? 1,
      font: d.font || "SimHei",
      fontFamily: d.fontFamily || d.font || "SimHei",
      style: d.style ?? 1,
      isAdvanced: true,
    };
  } else {
    const trackHeight = 100 / 10;
    position = {
      x: type === 1 ? 100 : 50,
      y:
        type === 1
          ? (d.track ?? Math.floor(Math.random() * 10)) * trackHeight + trackHeight / 2
          : type === 5
          ? 10
          : 90,
      duration: 8.0,
      font: d.font || "SimHei",
      fontFamily: d.font || "SimHei",
      scaleX: 1,
      scaleY: 1,
      rotation: 0,
      alpha: 1,
      isAdvanced: false,
    };
  }

  return {
    text: d.text,
    time: d.timeInVideo,
    type,
    color,
    fontSize: d.fontSize ?? 25,
    isSelf: false,
    timestamp:d.sendTime,
    isAdvanced: !!d.isAdvanced,
    position,
    track: d.track ?? 0,
  };
}
