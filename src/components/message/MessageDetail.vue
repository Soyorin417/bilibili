<template>
  <div class="chat-container">
    <!-- 消息列表区域 -->
    <div class="message-list" ref="messageList">
      <div class="messages-wrapper">
        <div
          v-for="(msg, index) in sessionMessages"
          :key="msg.id || index"
          :class="['msg-block', msg.fromUserId === user.id ? 'msg-right' : 'msg-left']"
        >
          <div class="msg-time" v-if="shouldShowTime(index)">
            {{ formatTime(msg.timestamp) }}
          </div>
          <div
            class="d-flex"
            :class="msg.fromUserId === user.id ? 'flex-row-reverse' : ''"
          >
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

    <!-- 消息通知 -->
    <div
      v-if="showNotification"
      class="message-notification"
      @click="handleNotificationClick"
    >
      <div class="notification-content">
        <img
          :src="notificationMessage.avatar"
          class="notification-avatar"
          alt="用户头像"
        />
        <div class="notification-text">
          <div class="notification-name">{{ notificationMessage.username }}</div>
          <div class="notification-message">{{ notificationMessage.content }}</div>
        </div>
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
      localMessages: [],
      showNotification: false,
      notificationMessage: null,
      notificationTimeout: null,
    };
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
    user() {
      return this.userInfo;
    },
    sessionMessages() {
      const sessionMsgs = this.session?.messages || [];
      return [...this.localMessages, ...sessionMsgs].map((msg) => ({
        ...msg,
        fromUserId: msg.fromUserId || msg.sender_id,
        toUserId: msg.toUserId || msg.receiver_id,
        avatar: msg.avatar || msg.sender_avatar,
        username: msg.username || msg.sender_name,
        timestamp: msg.timestamp || msg.send_time,
      }));
    },
  },
  watch: {
    session: {
      immediate: true,
      handler(newSession) {
        if (newSession && newSession.messages) {
          this.localMessages = [];
        }
      },
    },
    sessionMessages: {
      handler(newMessages) {
        this.$nextTick(() => {
          this.scrollToBottom();
        });

        // 检查是否有新消息
        if (newMessages.length > 0) {
          const lastMessage = newMessages[newMessages.length - 1];
          if (lastMessage.fromUserId !== this.user.id) {
            this.showNewMessageNotification(lastMessage);
          }
        }
      },
      deep: true,
    },
  },
  methods: {
    shouldShowTime(index) {
      if (!this.sessionMessages.length) return true;

      const curr = this.sessionMessages[index];
      const prev = this.sessionMessages[index - 1];

      if (!curr || !prev || !curr.timestamp || !prev.timestamp) return true;

      const currTime = dayjs(curr.timestamp);
      const prevTime = dayjs(prev.timestamp);

      if (!currTime.isValid() || !prevTime.isValid()) return true;

      return currTime.diff(prevTime, "minute") > 5;
    },

    formatTime(t) {
      return dayjs(t).format("HH:mm");
    },

    sendMessage() {
      if (!this.inputMessage.trim()) return;

      const message = {
        content: this.inputMessage,
        timestamp: new Date().toISOString(),
        fromUserId: this.user.id,
        toUserId:
          this.session.user1Id === this.user.id
            ? this.session.user2Id
            : this.session.user1Id,
        avatar: this.user.avatar,
        username: this.user.username,
      };

      this.localMessages.push(message);

      this.$emit("sendMessage", message);

      this.inputMessage = "";
    },

    showNewMessageNotification(message) {
      // 清除之前的通知
      if (this.notificationTimeout) {
        clearTimeout(this.notificationTimeout);
      }

      // 显示新通知
      this.notificationMessage = message;
      this.showNotification = true;

      // 5秒后自动隐藏
      this.notificationTimeout = setTimeout(() => {
        this.showNotification = false;
      }, 5000);
    },

    handleNotificationClick() {
      this.showNotification = false;
      this.scrollToBottom();
    },

    scrollToBottom() {
      const messageList = this.$refs.messageList;
      if (messageList) {
        messageList.scrollTop = messageList.scrollHeight;
      }
    },
  },
  mounted() {
    this.scrollToBottom();
  },
  beforeUnmount() {
    if (this.notificationTimeout) {
      clearTimeout(this.notificationTimeout);
    }
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
  margin-top: auto;
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

.message-notification {
  position: fixed;
  top: 20px;
  right: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  padding: 12px;
  cursor: pointer;
  z-index: 1000;
  animation: slideIn 0.3s ease-out;
}

.notification-content {
  display: flex;
  align-items: center;
}

.notification-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}

.notification-text {
  flex: 1;
}

.notification-name {
  font-weight: bold;
  margin-bottom: 4px;
}

.notification-message {
  color: #666;
  font-size: 14px;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}
</style>
