<template>
  <div>
    <VideoBar />
    <div class="message-view">
      <div class="message-center">
        <MessageNav class="sidebar" />
        <MessageList
          class="list"
          @select="selectSession"
          :sessions="sessions"
          :selected="selectedSession"
        />
        <MessageDetail class="detail" :session="selectedSession" />
      </div>
    </div>
  </div>
</template>

<script>
import MessageNav from "@/components/message/MessageNav.vue";
import MessageList from "@/components/message/MessageList.vue";
import MessageDetail from "@/components/message/MessageDetail.vue";
import VideoBar from "@/components/navBar/VideoBar.vue";
export default {
  name: "MessageView",
  components: { MessageNav, MessageList, MessageDetail, VideoBar },
  data() {
    return {
      sessions: [
        {
          id: 1,
          avatar: "xxx.png",
          name: "UP主小助手",
          summary: "本周达成【收藏飙升】成就...",
          messages: [
            {
              id: 1,
              time: "2025年4月26日 13:32",
              content: "本周达成【收藏飙升】成就，快来看...",
              type: "system",
            },
            // ...更多消息
          ],
        },
        // ...更多会话
      ],
      selectedSession: null,
    };
  },
  methods: {
    selectSession(session) {
      this.selectedSession = session;
    },
  },
  created() {
    this.selectedSession = this.sessions[0];
  },
};
</script>

<style scoped>
.message-view {
  width: 100%;
  height: 100vh;
  background: url("http://113.45.69.13:9000/image/587F0CDDB7FD5ACBF630CE902FA89545.jpg")
    no-repeat center center;
}
.message-center {
  display: flex;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}
.sidebar {
  width: 200px;
  background: #eaf3fa;
  border-right: 1px solid #e3e5e7;
}
.list {
  width: 300px;
  background: #fff;
  border-right: 1px solid #e3e5e7;
  overflow-y: auto;
}
.detail {
  flex: 1;
  background: #fff;
  overflow-y: auto;
}
</style>
