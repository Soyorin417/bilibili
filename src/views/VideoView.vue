<template>
  <div>
    <VideoBar></VideoBar>
    <div class="container-fluid mt-3">
      <div class="page mx-auto">
        <div class="row">
          <!-- 左侧区域：视频播放器和视频信息 -->
          <div class="col-lg-9 mt-2">
            <div class="row mb-3">
              <div class="col-8">
                <h4 class="d-flex">
                  {{ videoInfo.title }}
                </h4>
                <div class="d-flex align-items-center video-stats">
                  <play-two
                    theme="outline"
                    size="18"
                    fill="#666666"
                    :strokeWidth="2"
                    class="me-1"
                  />
                  <span class="me-3">{{ videoInfo.views }}</span>
                  <comment-one
                    theme="outline"
                    size="18"
                    fill="#666666"
                    :strokeWidth="2"
                    class="me-1"
                  />
                  <span class="me-3">{{ videoInfo.comments }}</span>
                  <span class="me-3">{{ videoInfo.time }}</span>
                  <span v-if="!videoInfo.show_right" class="ms-2">
                    <forbid theme="outline" size="18" fill="#fc0404" class="me-1" />
                    <span class="text-danger">未经作者授权，禁止转载</span>
                  </span>
                </div>
              </div>
            </div>

            <!-- 视频播放器 -->
            <div class="video-player-container">
              <div class="video-player bg-dark position-relative">
                <video
                  class="w-100"
                  controls
                  :poster="videoInfo.image"
                  :src="videoInfo.video_url"
                  ref="videoPlayer"
                  @timeupdate="onTimeUpdate"
                >
                  <source :src="videoInfo.video_url" type="video/mp4" />
                </video>

                <!-- 弹幕显示区域 -->
                <div class="danmaku-display-area" v-show="showDanmaku">
                  <div
                    v-for="(item, index) in activeDanmaku"
                    :key="index"
                    :style="item.style"
                    :class="[
                      'danmaku-item',
                      item.type === 'scroll' ? 'danmaku-running' : 'danmaku-fixed',
                    ]"
                  >
                    {{ item.content }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 弹幕控制条 -->
            <div class="danmaku-control-bar mt-2">
              <input
                v-model="danmakuText"
                class="danmaku-input me-2"
                placeholder="发个友善的弹幕吧~"
                @keyup.enter="sendDanmaku"
              />

              <!-- 弹幕类型选择 -->
              <div class="danmaku-type-selector me-2">
                <select v-model="currentDanmakuType" class="form-select form-select-sm">
                  <option value="scroll">滚动</option>
                  <option value="fixed">固定</option>
                </select>
              </div>

              <button class="btn btn-sm btn-primary" @click="sendDanmaku">发送</button>
              <div class="form-check form-switch ms-2">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="showDanmaku"
                  id="danmakuSwitch"
                />
                <label class="form-check-label" for="danmakuSwitch">弹幕</label>
              </div>
            </div>

            <!-- 视频互动区域 -->
            <div class="video-actions d-flex align-items-center mt-3 pb-3 border-bottom">
              <div class="d-flex align-items-center me-4">
                <button class="btn btn-outline-secondary me-2">
                  <i class="bi bi-hand-thumbs-up"></i> 30.0万
                </button>
                <button class="btn btn-outline-secondary">
                  <i class="bi bi-hand-thumbs-down"></i> 8.3万
                </button>
              </div>
              <button class="btn btn-outline-secondary me-3">
                <i class="bi bi-star"></i> 收藏 7.4万
              </button>
              <button class="btn btn-outline-secondary">
                <i class="bi bi-share"></i> 分享 3.2万
              </button>
            </div>

            <!-- 视频简介 -->
            <div class="video-info mt-3">
              <div class="d-flex">
                <div class="user-avatar me-3">
                  <img
                    src="http://113.45.69.13:9000/image/lucy_moon.jpg"
                    class="rounded-circle"
                    width="48"
                    height="48"
                    alt="user avatar"
                  />
                </div>
                <div>
                  <h6 class="mb-0">陈暖暖</h6>
                  <small class="text-muted">发布于 2023-03-02 17:17:30</small>
                  <p class="mt-2">这是一个神奇的视频，希望大家喜欢！</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧区域：推荐视频 -->
          <div class="col-lg-3">
            <!-- 作者信息区域 -->
            <div class="author-info">
              <div class="d-flex align-items-center">
                <div class="author-avatar me-2">
                  <img
                    :src="videoInfo.avatar"
                    class="rounded-circle"
                    width="80"
                    height="80"
                    alt="author avatar"
                  />
                </div>
                <div class="author-details">
                  <div class="d-flex align-items-center mb-1">
                    <h6>{{ videoInfo.author }}</h6>
                    <el-icon><Message /></el-icon>
                    <div>发消息</div>
                  </div>
                  <p class="small text-start">{{ videoInfo.description }}</p>
                  <span>
                    <button class="btn btn-sm btn-outline-secondary me-2">
                      <i class="bi bi-lightning-charge"></i> 充电
                    </button>

                    <button
                      class="btn btn-sm btn-primary"
                      @click="is_follow"
                      v-if="!userInfo.is_follow"
                    >
                      + 关注 {{ videoInfo.follow }}
                    </button>
                    <button
                      class="btn btn-sm btn-secondary"
                      @click="is_follow"
                      v-if="userInfo.is_follow"
                    >
                      已关注 {{ videoInfo.follow }}
                    </button>
                  </span>
                </div>
              </div>
            </div>

            <!-- 弹幕列表 -->
            <div class="danmaku-container mb-3" :class="{ expanded: is_show }">
              <div
                class="danmaku-header d-flex justify-content-between align-items-center p-1"
              >
                <div class="d-flex align-items-center">
                  <h6 class="mb-0 ms-1">弹幕列表</h6>
                </div>
                <div>
                  <small class="text-secondary me-1" @click="is_show = !is_show">
                    <drop-down-list theme="outline" size="18" fill="#000000" />
                  </small>
                </div>
              </div>
              <div v-if="is_show" class="danmaku-list">
                <table class="table table-sm table-hover borderless">
                  <thead class="thead-light">
                    <tr class="header-row">
                      <th scope="col" width="10%">时间</th>
                      <th scope="col" width="70%">弹幕内容</th>
                      <th scope="col" width="20%">发送时间</th>
                    </tr>
                  </thead>
                  <tbody class="small">
                    <tr v-for="(item, index) in danmakuList" :key="index">
                      <td>{{ item.time }}</td>
                      <td class="text-truncate" style="max-width: 160px">
                        {{ item.content }}
                      </td>
                      <td>{{ item.sendTime }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div v-if="is_show" class="danmaku-footer text-center">
                <small
                  ><a href="#" class="text-decoration-none text-secondary"
                    >查看历史弹幕</a
                  ></small
                >
              </div>
            </div>

            <!-- 推荐视频列表 -->
            <div
              class="recommended-header d-flex justify-content-between align-items-center mb-2"
            >
              <h6 class="mb-0">推荐列表</h6>
              <button class="btn btn-link text-secondary p-0">
                <i class="bi bi-three-dots"></i>
              </button>
            </div>
            <div class="recommended-videos">
              <div
                v-for="(item, index) in recommendedVideos"
                :key="index"
                class="recommended-video-item d-flex mb-3"
              >
                <div class="recommended-video-thumbnail position-relative me-2">
                  <img
                    :src="item.image"
                    class="recommended-thumbnail"
                    alt="video thumbnail"
                  />
                  <span
                    class="video-duration position-absolute bottom-0 end-0 bg-dark text-white px-1 rounded-1"
                    >{{ item.duration }}</span
                  >
                </div>
                <div class="recommended-video-info">
                  <h6 class="recommended-video-title mb-1">{{ item.title }}</h6>
                  <small class="text-muted d-block">{{ item.author }}</small>
                  <small class="text-muted">{{ item.views }}次观看</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import { PlayTwo, CommentOne, Forbid, DropDownList } from "@icon-park/vue-next";

export default {
  components: { VideoBar, PlayTwo, CommentOne, Forbid, DropDownList },
  props: ["id"],
  data() {
    return {
      is_show: false,
      showDanmaku: true,
      danmakuText: "",
      activeDanmaku: [],
      currentTime: 0,
      currentDanmakuType: "scroll", // 默认滚动弹幕
      danmakuList: [
        {
          time: "01:32",
          content: "你我都是孤独之人",
          sendTime: "09-30 19:02",
          type: "scroll",
        },
        {
          time: "00:51",
          content: "老婆qwq不要死",
          sendTime: "09-30 19:01",
          type: "scroll",
        },
        {
          time: "00:31",
          content: "哗",
          sendTime: "09-30 19:03",
          type: "fixed",
        },
        {
          time: "01:19",
          content: "阿梅离开后的粒子特效",
          sendTime: "09-30 19:03",
          type: "scroll",
        },
        {
          time: "01:32",
          content: "靠癫火取暖（doge）",
          sendTime: "09-30 19:03",
          type: "scroll",
        },
        {
          time: "00:31",
          content: "???",
          sendTime: "09-30 19:04",
          type: "fixed",
        },
        {
          time: "00:32",
          content: "wc",
          sendTime: "09-30 19:03",
          type: "fixed",
        },
        {
          time: "00:00",
          content: "你更新的消息从漂流墓地到求学洞窟无人不晓",
          sendTime: "09-30 19:04",
          type: "scroll",
        },
        {
          time: "00:28",
          content: "好！！！",
          sendTime: "09-30 19:07",
          type: "scroll",
        },
        {
          time: "01:03",
          content: "小木头：痛，太痛了",
          sendTime: "09-30 19:14",
          type: "scroll",
        },
        {
          time: "00:24",
          content: "我的女巫啊啊啊啊啊",
          sendTime: "09-30 19:31",
          type: "fixed",
        },
        {
          time: "01:20",
          content: "痛，太痛了",
          sendTime: "09-30 19:31",
          type: "scroll",
        },
        {
          time: "01:29",
          content: "把那个发癫的叉出去",
          sendTime: "09-30 19:23",
          type: "scroll",
        },
        {
          time: "00:17",
          content: "即使引导已经破碎，也请您当上艾尔登之王",
          sendTime: "09-30 19:11",
          type: "scroll",
        },
        {
          time: "00:07",
          content: "the fallen leaves tell a story",
          sendTime: "09-30 19:23",
          type: "scroll",
        },
        {
          time: "00:45",
          content: "梅琳娜我恨你是块木头啊！",
          sendTime: "09-30 19:32",
          type: "scroll",
        },
        {
          time: "00:40",
          content: "啊啊啊，居然不是整活视频",
          sendTime: "09-30 19:18",
          type: "scroll",
        },
        {
          time: "00:25",
          content: "托雷特和小木头",
          sendTime: "09-30 19:21",
          type: "scroll",
        },
        {
          time: "01:26",
          content: "绝了",
          sendTime: "09-30 19:11",
          type: "scroll",
        },
        {
          time: "01:31",
          content: "痛  太痛了",
          sendTime: "09-30 19:11",
          type: "scroll",
        },
        {
          time: "01:49",
          content: "恶兆王——蒙葛特",
          sendTime: "09-30 19:55",
          type: "scroll",
        },
        {
          time: "01:31",
          content: "泪目",
          sendTime: "09-30 19:09",
          type: "scroll",
        },
        {
          time: "01:29",
          content: "痛，太痛了",
          sendTime: "09-30 19:32",
          type: "scroll",
        },
        {
          time: "01:20",
          content: "又到了。。。白。。",
          sendTime: "09-30 19:54",
          type: "scroll",
        },
        {
          time: "00:30",
          content: "厚礼谢",
          sendTime: "09-30 20:03",
          type: "scroll",
        },
        {
          time: "00:31",
          content: "巨大宝箱",
          sendTime: "09-30 19:05",
          type: "scroll",
        },
        {
          time: "01:58",
          content: "为了你，我要在这里发癫",
          sendTime: "09-30 19:45",
          type: "scroll",
        },
        {
          time: "00:03",
          content: "泪目",
          sendTime: "09-30 19:17",
          type: "scroll",
        },
        {
          time: "01:24",
          content: "卧槽，这个场景",
          sendTime: "09-30 20:07",
          type: "scroll",
        },
        {
          time: "00:02",
          content: "落叶捎来讯息",
          sendTime: "09-30 19:10",
          type: "scroll",
        },
        {
          time: "00:03",
          content: "最喜欢的一集",
          sendTime: "09-30 19:38",
          type: "scroll",
        },
        {
          time: "01:12",
          content: "梅琳娜你这块木头",
          sendTime: "09-30 19:06",
          type: "scroll",
        },
        {
          time: "01:58",
          content: "阿梅，为了你，我要烧尽世间隔绝你我的一切！",
          sendTime: "09-30 21:31",
          type: "scroll",
        },
        {
          time: "01:05",
          content: "老婆！！！",
          sendTime: "09-30 19:17",
          type: "scroll",
        },
        {
          time: "01:33",
          content: "痛 太痛了 痛贯天灵",
          sendTime: "09-30 20:05",
          type: "scroll",
        },
        {
          time: "01:35",
          content: "小木头   我哭死",
          sendTime: "09-30 20:10",
          type: "scroll",
        },
        {
          time: "00:02",
          content: "落叶捎来讯息",
          sendTime: "09-30 19:17",
          type: "scroll",
        },
        {
          time: "01:47",
          content: "活下去！木头！",
          sendTime: "09-30 21:14",
          type: "scroll",
        },
        {
          time: "01:33",
          content: "泪目",
          sendTime: "09-30 19:26",
          type: "scroll",
        },
        {
          time: "01:11",
          content: "好家伙，太厉害了吧",
          sendTime: "09-30 20:49",
          type: "scroll",
        },
        {
          time: "01:12",
          content: "难蚌",
          sendTime: "09-30 20:20",
          type: "scroll",
        },
        {
          time: "01:53",
          content: "褪色者：那么……只有这一条路了。",
          sendTime: "09-30 20:16",
          type: "scroll",
        },
        {
          time: "00:06",
          content: "最喜欢的一集",
          sendTime: "10-01 09:31",
          type: "scroll",
        },
        {
          time: "00:45",
          content: "刚到王城，梅玲娜走掉我真的愣住了，跟老婆跑调了一样。",
          sendTime: "09-30 19:17",
          type: "scroll",
        },
        {
          time: "00:01",
          content: "我等的可是望眼欲穿呀",
          sendTime: "09-30 20:33",
          type: "scroll",
        },
        {
          time: "01:22",
          content: "淦！她要是真愿意来看我，我可得感动哭了",
          sendTime: "09-30 19:18",
          type: "scroll",
        },
        {
          time: "00:33",
          content: "我来帮你把卢恩转化成力量",
          sendTime: "09-30 20:23",
          type: "scroll",
        },
        {
          time: "00:00",
          content: "催泪神作",
          sendTime: "09-30 20:41",
          type: "scroll",
        },
        {
          time: "00:50",
          content: "好美啊啊啊啊啊",
          sendTime: "09-30 19:38",
          type: "scroll",
        },
        {
          time: "00:01",
          content: "The fallen leaves tell a story",
          sendTime: "09-30 19:21",
          type: "scroll",
        },
      ],
      videoInfo: {
        title: "《一等情事》",
        views: "1312.8万",
        comments: "1.0万",
        time: "2020-09-30 19:00:00",
        description: "前有无畏",
        avatar:
          "http://113.45.69.13:9000/image/70f8a4e6046ea9795fc96784ba7cb69bec9832c1.jpg",
        video_url: "http://113.45.69.13:9000/image/《 一等情事 》.mp4",
        image:
          "http://113.45.69.13:9000/image/78845e847febc6fdf4f41eeb6306c25c23396569.jpg",
        show_right: false,
        author: "红宝石戒指",
        follow: "13.6万",
      },
      userInfo: {
        name: "红宝石戒指",
        avatar:
          "http://113.45.69.13:9000/image/70f8a4e6046ea9795fc96784ba7cb69bec9832c1.jpg",
        is_follow: false,
        follow: "13.6万",
      },
      recommendedVideos: [
        {
          id: 1,
          title: "苏士比亚",
          author: "陈暖暖",
          views: "626.3万",
          duration: "01:41",
          image:
            "http://113.45.69.13:9000/image/a29d12bfc0664b4a62dc5de95d42f14c65e6f303.jpg",
        },
        {
          id: 2,
          title: "MVP唱法",
          author: "白小鱼",
          views: "551.2万",
          duration: "01:31",
          image:
            "http://113.45.69.13:9000/image/23b06d02882bcf31d6d044339796693b7d02ed2a.jpg",
        },
        {
          id: 3,
          title: "Look in my eyes",
          author: "白小鱼",
          views: "551.2万",
          duration: "01:11",
          image:
            "http://113.45.69.13:9000/image/f94ec129e83bfca078e5e84277d8a69ea932732e.jpg",
        },
        {
          id: 4,
          title: "八勒比寥斯",
          author: "陌生人Ming",
          views: "1269.4万",
          duration: "01:24",
          image:
            "http://113.45.69.13:9000/image/d1183a9170bd267f19f748ad874ad5467b4dcca2.jpg",
        },
        {
          id: 5,
          title: "满坑大佬痛哭新手村",
          author: "Hermoe",
          views: "6222.9万",
          duration: "02:06",
          image:
            "http://113.45.69.13:9000/image/33e9ef5fc74dc0057dd50cf73e119bcd475d4f56.jpg",
        },
      ],
    };
  },
  methods: {
    is_follow() {
      this.userInfo.is_follow = !this.userInfo.is_follow;
    },
    // 时间格式化为秒
    timeToSeconds(timeStr) {
      const parts = timeStr.split(":");
      return parseInt(parts[0]) * 60 + parseInt(parts[1]);
    },
    // 视频时间更新事件
    onTimeUpdate() {
      if (!this.$refs.videoPlayer) return;

      this.currentTime = this.$refs.videoPlayer.currentTime;

      // 查找当前时间点附近的弹幕
      this.checkDanmakuTiming();
      console.log("当前时间：", this.currentTime);
    },
    // 检查是否有弹幕需要显示
    checkDanmakuTiming() {
      if (!this.showDanmaku) return;

      const currentTimeInt = Math.floor(this.currentTime);
      console.log("当前视频时间：", currentTimeInt);

      this.danmakuList.forEach((danmaku) => {
        const danmakuTime = this.timeToSeconds(danmaku.time);
        console.log("检查弹幕时间：", danmakuTime, "弹幕内容：", danmaku.content);

        if (Math.abs(danmakuTime - currentTimeInt) < 0.3) {
          console.log("触发弹幕显示：", danmaku);
          this.showNewDanmaku(danmaku.content, danmaku.type);
        }
      });
    },

    // 显示新弹幕
    showNewDanmaku(content, type = "fixed") {
      console.log("开始创建新弹幕：", { content, type });

      const trackPosition = Math.floor(Math.random() * 80) + 10;
      console.log("弹幕位置：", trackPosition);

      let style = {};
      if (type === "scroll") {
        style = {
          top: `${trackPosition}%`,
          color: this.getRandomColor(),
          right: "-100%",
          transform: "translateX(0)",
          whiteSpace: "nowrap",
        };
      } else {
        style = {
          top: `${trackPosition}%`,
          color: this.getRandomColor(),
          left: "50%",
          transform: "translateX(-50%)",
          animationName: "danmakuFade",
          animationDuration: "5s",
        };
      }
      console.log("弹幕样式：", style);

      const newDanmaku = {
        content,
        style,
        type,
        key: Date.now(),
      };
      console.log("新弹幕：", newDanmaku);

      this.activeDanmaku.push(newDanmaku);
      console.log("当前活跃弹幕列表：", this.activeDanmaku);

      setTimeout(
        () => {
          const index = this.activeDanmaku.findIndex((d) => d.key === newDanmaku.key);
          if (index !== -1) {
            console.log("移除弹幕：", newDanmaku);
            this.activeDanmaku.splice(index, 1);
            console.log("移除后的弹幕列表：", this.activeDanmaku);
          }
        },
        type === "scroll" ? 14000 : 5000
      );
    },
    // 获取随机颜色
    getRandomColor() {
      const colors = [
        "#ffffff", // 白色
        "#ff9999", // 浅红色
        "#99ff99", // 浅绿色
        "#9999ff", // 浅蓝色
        "#ffff99", // 浅黄色
      ];
      return colors[Math.floor(Math.random() * colors.length)];
    },
    // 发送弹幕
    sendDanmaku() {
      if (!this.danmakuText.trim()) return;
      console.log("发送弹幕：", this.danmakuText);
      // 获取当前视频时间
      const minutes = Math.floor(this.currentTime / 60);
      const seconds = Math.floor(this.currentTime % 60);
      const timeStr = `${minutes
        .toString()
        .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;

      // 创建新弹幕
      const now = new Date();
      const month = (now.getMonth() + 1).toString().padStart(2, "0");
      const day = now.getDate().toString().padStart(2, "0");
      const hours = now.getHours().toString().padStart(2, "0");
      const mins = now.getMinutes().toString().padStart(2, "0");

      const newDanmaku = {
        time: timeStr,
        content: this.danmakuText,
        sendTime: `${month}-${day} ${hours}:${mins}`,
        type: this.currentDanmakuType,
      };

      // 添加到弹幕列表
      this.danmakuList.unshift(newDanmaku);

      // 显示新发送的弹幕
      this.showNewDanmaku(this.danmakuText);

      // 清空输入框
      this.danmakuText = "";
    },
  },
  computed: {
    videoId() {
      return this.id;
    },
  },
  mounted() {
    console.log("加载视频ID:", this.videoId);
  },
};
</script>

<style scoped>
.page {
  max-width: 1500px;
}

.video-player-container {
  position: relative;
  background-color: #000;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 20px;
}

.video-player {
  width: 100%;
  aspect-ratio: 16/9;
  position: relative;
}

.video-player video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-player::before {
  content: "";
  display: block;
  padding-top: 56.25%;
}

.video-player video,
.video-player img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 弹幕显示区域 */
.danmaku-display-area {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  z-index: 9999;
}

.danmaku-item {
  position: absolute;
  white-space: nowrap;
  font-size: 20px;
  font-weight: 500;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
  will-change: transform;
  user-select: none;
}

/* 滚动弹幕动画 */
@keyframes running {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-3000px);
  }
}

/* 固定弹幕动画 */
@keyframes danmakuFade {
  0% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.8);
  }
  20% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  80% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
  100% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(1.2);
  }
}

.danmaku-running {
  animation: running 12s linear forwards;
  position: absolute;
  right: -100%;
}

.danmaku-fixed {
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  animation: danmakuFade 5s forwards;
}

/* 弹幕控制条 */
.danmaku-control-bar {
  display: flex;
  align-items: center;
  padding: 8px;
  background-color: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 20px;
}

.danmaku-input {
  flex: 1;
  height: 30px;
  border-radius: 15px;
  border: 1px solid #ddd;
  padding: 0 15px;
  font-size: 13px;
  color: #333;
  background-color: #fff;
}

.danmaku-type-selector select {
  height: 30px;
  border-radius: 4px;
  font-size: 12px;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
}

.form-check-label {
  color: white;
  font-size: 13px;
}

/* 弹幕列表样式 */
.danmaku-container {
  border: 1px solid #eee;
  border-radius: 3px;
  overflow: hidden;
  background-color: #fff;
  font-size: 10px;
  height: auto;
  min-height: 30px;
}

.danmaku-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
  padding: 2px 4px !important;
  min-height: 28px;
  display: flex;
  align-items: center;
}

.danmaku-header h6 {
  font-size: 13px; /* 缩小标题字体 */
  color: #333;
  margin-bottom: 0;
}

.danmaku-list {
  height: 700px; /* 固定高度   */
  overflow-y: auto;
  max-height: none;
}

.danmaku-list table {
  margin-bottom: 0;
  border: none;
}

.danmaku-list th {
  font-size: 12px; /* 缩小表头字体 */
  font-weight: 600;
  color: #555;
  padding: 1px 3px; /* 减小padding */
  border: none;
  background-color: #f8f9fa;
}

.danmaku-list td {
  font-size: 12px; /* 缩小单元格字体 */
  padding: 1px 3px; /* 减小padding */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border: none;
  line-height: 1.2; /* 减小行高 */
}

.danmaku-footer {
  background-color: #f8f9fa;
  border-top: 1px solid #eee;
  font-size: 9px; /* 缩小底部字体 */
  padding: 1px !important; /* 减小padding */
}

.borderless th,
.borderless td,
.borderless thead th,
.borderless tbody + tbody {
  border: none !important; /* 确保移除所有边框 */
}

.table-hover > tbody > tr:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.header-row {
  border: none !important; /* 移除表头边框 */
  background-color: #f8f9fa;
}

.recommended-thumbnail {
  width: 140px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.video-duration {
  font-size: 11px;
  padding: 0px 3px;
  border-radius: 2px;
}

.recommended-video-title {
  font-size: 13px;
  line-height: 1.3;
  margin-bottom: 4px;
}

.recommended-video-info {
  width: calc(100% - 150px);
}

.video-stats {
  font-size: 13px;
  margin-bottom: 12px;
}

.video-title {
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
  margin-bottom: 8px;
}

.author-info {
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 16px;
}

.author-avatar img {
  width: 46px;
  height: 46px;
  border: 1px solid #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.author-details h6 {
  font-size: 13px;
  font-weight: 600;
  margin-bottom: 0;
}

.author-details .text-muted {
  font-size: 12px;
  margin-bottom: 6px;
}

.author-details .btn-sm {
  padding: 0.2rem 0.4rem;
  font-size: 11px;
}

.author-details .el-icon {
  font-size: 14px;
  margin-left: 4px;
  color: #666;
}

.recommended-video-item {
  margin-bottom: 12px;
}

/* 视频互动区域 */
.video-actions {
  margin-top: 20px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

/* 当列表显示时，容器才应用固定高度 */
.danmaku-container.expanded {
  height: 600px; /* 列表高度 + header/footer 空间 */
}
</style>
