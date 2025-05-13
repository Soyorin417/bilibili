<template>
  <div class="chat-container">
    <!-- 消息列表区域 -->
    <div class="message-list">
      <div class="messages-wrapper">
        <div
          v-for="(msg, index) in messages"
          :key="msg.id"
          :class="['msg-block', msg.senderId === user.id ? 'msg-right' : 'msg-left']"
        >
          <div class="msg-time" v-if="shouldShowTime(index)">
            {{ formatTime(msg.sendTime) }}
          </div>
          <div class="d-flex" :class="msg.senderId === user.id ? 'flex-row-reverse' : ''">
            <img :src="msg.avatar" class="msg-avatar" alt="用户头像" />
            <div class="msg-content">{{ msg.content }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入框 -->
    <div class="input-area">
      <div class="input-group">
        <input
          v-model="inputMessage"
          type="text"
          class="form-control"
          placeholder="输入消息内容"
          @keyup.enter="sendMessage"
        />
        <button class="btn btn-primary" type="button" @click="sendMessage">发送</button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import dayjs from "dayjs";

export default {
  props: {
    session: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      inputMessage: "",
      messages: [],
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo", "isLogin"]),
    user() {
      console.log("Current user:", this.userInfo);
      return this.userInfo;
    },
  },
  mounted() {
    console.log("Session data:", this.session);
    console.log("Messages:", this.session?.messages);
  },
  methods: {
    shouldShowTime(index) {
      if (!this.session || !this.session.messages) return true;

      const messages = this.session.messages;

      const curr = messages[index];
      const prev = messages[index - 1];

      if (!curr || !prev || !curr.sendTime || !prev.sendTime) return true;

      const currTime = dayjs(curr.sendTime);
      const prevTime = dayjs(prev.sendTime);

      if (!currTime.isValid() || !prevTime.isValid()) return true;

      return currTime.diff(prevTime, "minute") > 5;
    },

    formatTime(t) {
      return dayjs(t).format("HH:mm");
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return;

      const newMsg = {
        id: Date.now(),
        content: this.inputMessage,
        sendTime: new Date().toISOString(),
        senderId: this.user.id,
        sessionId: this.session.id,
        avatar: this.user.avatar,
      };

      this.$emit("send-message", newMsg);
      this.inputMessage = "";

      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    scrollToBottom() {
      const list = this.$refs.messageList;
      if (list) {
        list.scrollTop = list.scrollHeight;
      }
    },
  },
  watch: {
    session: {
      handler(newSession) {
        if (newSession && Array.isArray(newSession.messages)) {
          this.messages = newSession.messages.map((msg) => ({
            ...msg,
            senderAvatar: msg.avatar,
          }));
        }
      },
      immediate: true,
    },
  },
};
</script>

<style scoped>
.chat-container {
  height: 80vh;
  display: flex;
  flex-direction: column;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f4f5f7;
}

.messages-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

.input-area {
  background: #fff;
  padding: 16px 20px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.input-group {
  max-width: 800px;
  margin: 0 auto;
}

.msg-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 12px;
}

.msg-block {
  margin: 24px 0;
}

.msg-time {
  color: #666;
  font-size: 12px;
  text-align: center;
  margin-bottom: 8px;
}

.msg-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 8px;
  word-break: break-word;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.msg-left .msg-content {
  background: #fff;
  border-radius: 8px 8px 8px 0;
}

.msg-right .msg-content {
  background: #dcf8c6;
  border-radius: 8px 8px 0 8px;
}

@media (max-width: 576px) {
  .message-list {
    padding: 10px;
  }

  .input-area {
    padding: 12px;
  }

  .msg-content {
    max-width: 85%;
  }
}
</style>
