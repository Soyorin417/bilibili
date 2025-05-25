export function parseDanmakuXml(xmlText) {
  try {
    // 检查并移除第一行的完整样式信息提示
    if (xmlText.trim().startsWith('This XML file does not appear to have any style information associated with it. The document tree is shown below.')) {
      xmlText = xmlText.split('\n').slice(1).join('\n').trim();
    }

    const parser = new DOMParser();
    const xml = parser.parseFromString(xmlText, "text/xml");
    
    // 尝试从根节点i获取d节点
    let dNodes = [];
    const root = xml.getElementsByTagName("i")[0];
    if (root) {
      dNodes = root.getElementsByTagName("d");
    } else {
      // 如果没有找到i节点，直接获取d节点
      dNodes = xml.getElementsByTagName("d");
    }
    
    const danmakus = [];

    // 统计弹幕类型
    const typeStats = {
      1: 0, // 滚动弹幕
      4: 0, // 底部弹幕
      5: 0, // 顶部弹幕
      7: 0, // 高级弹幕
      advanced: 0, // 高级弹幕
    };

    // 统计轨道使用情况
    const trackStats = new Array(10).fill(0);

    for (let d of dNodes) {
      try {
        const pAttr = d.getAttribute("p");
        const content = d.textContent.trim();

        // 解析 p 属性
        const p = pAttr.split(",");
        // 检查基本属性是否存在
        if (!p[0] || !p[1] || !p[2] || !p[3]) {
          console.warn("弹幕缺少基本属性:", pAttr);
          continue;
        }

        const time = parseFloat(p[0]);
        const type = parseInt(p[1]); // 弹幕类型
        const fontSize = parseInt(p[2]);
        const color = parseColor(p[3]);
        const timestamp = parseInt(p[4]) || 0;
        const pool = parseInt(p[5]) || 0; // 弹幕池类型
        const userHash = p[6] || "";
        const rowID = p[7] || "";
        const track = parseInt(p[8]) || 0; // 弹幕轨道

        // 统计弹幕类型
        if (Object.prototype.hasOwnProperty.call(typeStats, type)) {
          typeStats[type]++;
        }

        // 统计轨道使用情况
        if (type === 1) {
          // 只统计滚动弹幕的轨道
          trackStats[track]++;
        }

        // 解析高级弹幕内容
        let text = content;
        let position = null;
        let isAdvanced = false;

        if (content.startsWith("[") && content.endsWith("]")) {
          try {
            const jsonData = JSON.parse(content);
            isAdvanced = true;
            typeStats.advanced++;
            typeStats[7]++; // 统计类型7的高级弹幕

            // 解析高级弹幕数据
            text = jsonData[4] || content; // 弹幕文本

            // 解析缩放比例
            const scaleMatch = (jsonData[2] || "1-1").match(/(\d+)-(\d+)/);
            const scaleX = scaleMatch ? parseFloat(scaleMatch[1]) : 1;
            const scaleY = scaleMatch ? parseFloat(scaleMatch[2]) : 1;

            position = {
              x: jsonData[0] || 0, // x坐标
              y: jsonData[1] || 0, // y坐标
              scaleX, // x轴缩放
              scaleY, // y轴缩放
              duration: jsonData[3] || 8, // 持续时间
              alpha: jsonData[5] || 1, // 透明度
              rotation: jsonData[6] || 0, // 旋转角度
              size: jsonData[7] || fontSize, // 字体大小
              color: jsonData[8] || color, // 颜色
              type: jsonData[9] || type, // 类型
              font: jsonData[10] || "SimHei", // 字体
              style: jsonData[11] || 1, // 样式
              fontFamily: jsonData[12] || "SimHei", // 字体族
              isAdvanced: jsonData[13] || 1, // 是否高级弹幕
            };

            
          } catch (e) {
            console.warn("高级弹幕解析失败:", content, e);
            // 解析失败时，将其视为普通弹幕
            isAdvanced = false;
          }
        }

        // 根据弹幕类型设置位置
        if (!isAdvanced) {
          const trackHeight = 100 / 10; // 每个轨道的高度
          position = {
            x: type === 1 ? 100 : 50, // 滚动弹幕从右侧开始，其他类型居中
            y: type === 1 ? track * trackHeight + trackHeight / 2 : type === 5 ? 10 : 90, // 使用XML中的轨道数据
            duration: 8, // 默认持续时间
            font: "SimHei", // 默认字体
          };
        }

        const danmaku = {
          time,
          type,
          fontSize,
          color,
          timestamp,
          pool,
          userHash,
          rowID,
          track, // 添加轨道信息
          text,
          position,
          isAdvanced,
          duration: position?.duration || 8,
          font: position?.font || "SimHei",
          isScrolling: type === 1 || type === 7, // 标记是否为滚动弹幕
        };


        danmakus.push(danmaku);
      } catch (e) {
        console.warn("弹幕解析失败:", e);
      }
    }

    return danmakus;
  } catch (e) {
    console.error("XML解析失败:", e);
    return [];
  }
}

function parseColor(colorNum) {
  if (!colorNum || isNaN(colorNum)) return "#ffffff";

  const num = parseInt(colorNum);
  if (num > 0xffffff) {
    return `#${(num & 0xffffff).toString(16).padStart(6, "0")}`;
  }
  return `#${num.toString(16).padStart(6, "0")}`;
}
