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
        >
          <template #session-item="{ session }">
            <img :src="getSessionAvatar(session)" class="avatar ms-2" />
          </template>
        </MessageList>
        <MessageDetail
          class="detail"
          :session="selectedSession"
          @send-message="sendMessage"
        />
      </div>
    </div>
  </div>
</template>

<script>
import MessageNav from "@/components/message/MessageNav.vue";
import MessageList from "@/components/message/MessageList.vue";
import MessageDetail from "@/components/message/MessageDetail.vue";
import VideoBar from "@/components/navBar/VideoBar.vue";
import axios from "axios";
import websocketClient from "@/utils/websocket";
import { userApi } from "@/api/user";
import { sessionApi } from "@/api/session";

export default {
  name: "MessageView",
  components: { MessageNav, MessageList, MessageDetail, VideoBar },

  data() {
    return {
      sessions: [],
      selectedSession: null,
      currentUser: localStorage.getItem("username") || "",
      hasAutoSelected: false,
    };
  },
  methods: {
    async getAllSessions() {
      const userInfo = JSON.parse(localStorage.getItem("userInfo"));
      const userId = userInfo.id;
      try {
        const response = await sessionApi.getSessionsByUserId(userId);
        this.sessions = response.data;

        // 立即加载所有会话的头像
        await Promise.all(
          this.sessions.map(async (session) => {
            const targetUserId =
              userId === session.user1Id ? session.user2Id : session.user1Id;
            try {
              const userResponse = await userApi.getUserById(targetUserId);
              this.$set(session, "avatar", userResponse.data.avatar);
            } catch (error) {
              this.$set(
                session,
                "avatar",
                "http://113.45.69.13:9000/image/default-avatar.jpg"
              );
            }
          })
        );

        if (this.sessions.length > 0) {
          if (
            this.sessions[0].user1Id === this.currentUser ||
            this.sessions[0].user2Id === this.currentUser
          ) {
            this.selectSession(this.sessions[0]);
            this.hasAutoSelected = true;
          }
        }

        if (!this.hasAutoSelected && this.sessions.length > 0) {
          this.selectSession(this.sessions[0]);
          this.hasAutoSelected = true;
        }
      } catch (error) {
        console.error("Error fetching sessions:", error);
      }
    },

    async getMessages(fromUser, toUser) {
      const url = `http://localhost:8081/api/messages/private?user1=${fromUser}&user2=${toUser}`;
      const token = localStorage.getItem("token");

      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        const messages = response.data;
        const session = this.sessions.find(
          (s) =>
            (s.user1Id === fromUser && s.user2Id === toUser) ||
            (s.user1Id === toUser && s.user2Id === fromUser)
        );

        if (session) {
          session.messages = messages;
          this.getSessionAvatar(session);
          this.selectedSession = { ...session };
        }
      } catch (error) {
        console.error("Error fetching messages:", error);
      }
    },

    selectSession(session) {
      this.selectedSession = session;
      const userInfoStr = JSON.parse(localStorage.getItem("userInfo"));
      const userId = parseInt(userInfoStr.id);

      let targetUserId = null;
      if (userId === session.user1Id) {
        targetUserId = session.user2Id;
      } else {
        targetUserId = session.user1Id;
      }

      if (!userId || !targetUserId) {
        console.error("Invalid user IDs", userId, targetUserId);
        return;
      }

      this.getMessages(userId, targetUserId);
    },

    async sendMessage(newMsg) {
      if (!this.selectedSession) return;

      const session = this.sessions.find((s) => s.id === this.selectedSession.id);
      if (!session) return;

      const userInfo = JSON.parse(localStorage.getItem("userInfo"));
      const senderId = userInfo.id;
      const senderName = userInfo.username;
      const senderAvatar = userInfo.avatar;

      // 判断对方id
      const receiverId = senderId === session.user1Id ? session.user2Id : session.user1Id;

      // 查对方信息
      let receiverName = "";
      let receiverAvatar = "";
      try {
        const res = await userApi.getUserById(receiverId);
        receiverName = res.data.username;
        receiverAvatar = res.data.avatar;
      } catch (e) {
        console.error("获取对方用户信息失败", e);
      }

      const messagePayload = {
        session_id: session.id,
        content: newMsg.content,
        send_time: new Date().toISOString(),
        sender_id: senderId,
        sender_name: senderName,
        sender_avatar: senderAvatar,
        receiver_id: receiverId,
        receiver_name: receiverName,
        receiver_avatar: receiverAvatar,
      };

      // 立即在发送者界面显示消息
      if (!session.messages) {
        session.messages = [];
      }
      session.messages.push({
        ...messagePayload,
        // 兼容前端展示字段
        timestamp: messagePayload.send_time,
        fromUserId: senderId,
        toUserId: receiverId,
        avatar: senderAvatar,
        username: senderName,
      });
      this.selectedSession = { ...session };

      // 发送消息到WebSocket服务器
      websocketClient.send({
        type: "private_message",
        ...messagePayload,
      });

      // 同时保存到后端数据库
      const url = `http://localhost:8081/api/messages`;
      const token = localStorage.getItem("token");
      try {
        const response = await axios.post(url, messagePayload, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("保存消息到数据库成功:", response.data);
      } catch (error) {
        console.error("Error saving message to database:", error);
      }
    },

    handleWebSocketMessage(event) {
      try {
        const rawMessage = event.data;

        // 正确的前缀匹配
        const prefixMatch = rawMessage.match(/^\[.*?]：/);
        let jsonStr = rawMessage;
        if (prefixMatch) {
          jsonStr = rawMessage.substring(prefixMatch[0].length);
        }

        // 只处理以 { 开头的消息
        if (!jsonStr.trim().startsWith("{")) {
          // 不是 JSON 消息，直接忽略
          return;
        }

        // 尝试解析 JSON
        const data = JSON.parse(jsonStr);

        if (data.type === "private_message") {
          const userInfo = JSON.parse(localStorage.getItem("userInfo"));
          const userId = parseInt(userInfo.id);

          // 只处理发送给当前用户的消息
          if (data.toUserId === userId) {
            // 检查消息是否属于当前会话
            if (
              this.selectedSession &&
              ((this.selectedSession.user1Id === data.fromUserId &&
                this.selectedSession.user2Id === userId) ||
                (this.selectedSession.user1Id === userId &&
                  this.selectedSession.user2Id === data.fromUserId))
            ) {
              // 将新消息添加到当前会话
              const newMessage = {
                content: data.content,
                timestamp: data.timestamp,
                fromUserId: data.fromUserId,
                toUserId: data.toUserId,
                avatar: data.avatar,
                username: data.username,
              };

              if (!this.selectedSession.messages) {
                this.selectedSession.messages = [];
              }
              this.selectedSession.messages.push(newMessage);
              this.selectedSession = { ...this.selectedSession };
            }
          }
        }
      } catch (error) {
        console.error("Error handling WebSocket message:", error);
      }
    },

    async getSessionAvatar(session) {
      console.log("getSessionAvatar session:", session);
      if (session.avatar === this.currentUser.avatar) {
        if (session.messages && session.messages.length > 0) {
          const otherUserMessage = session.messages.find(
            (msg) => msg.senderId !== this.currentUser.id
          );
          if (otherUserMessage) {
            return otherUserMessage.avatar || otherUserMessage.sender_avatar;
          }
        }
        // messages 为空时，直接用 session 里的对方头像
        if (session.user1Id === this.currentUser.id) {
          return session.user2Avatar;
        } else {
          return session.user1Avatar;
        }
      }
      return session.avatar;
    },
  },

  mounted() {
    this.getAllSessions();
    // 确保WebSocket连接已建立
    if (!websocketClient.ws || websocketClient.ws.readyState !== WebSocket.OPEN) {
      websocketClient.connect();
      // 等待连接建立后再添加监听器
      const checkConnection = setInterval(() => {
        if (websocketClient.ws && websocketClient.ws.readyState === WebSocket.OPEN) {
          websocketClient.ws.addEventListener("message", this.handleWebSocketMessage);
          clearInterval(checkConnection);
        }
      }, 100);
    } else {
      websocketClient.ws.addEventListener("message", this.handleWebSocketMessage);
    }
  },

  beforeUnmount() {
    // 移除WebSocket消息监听器
    if (websocketClient.ws && websocketClient.ws.readyState === WebSocket.OPEN) {
      websocketClient.ws.removeEventListener("message", this.handleWebSocketMessage);
    }
  },

  watch: {
    selectedSession: {
      handler(newVal) {
        if (newVal && Array.isArray(newVal.messages)) {
          console.log("selectedSession messages:", newVal.messages);
        }
      },
      immediate: true,
      deep: true,
    },
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
  background: #f4f5f7;
  overflow-y: auto;
}
.session {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  transition: background 0.2s;
}
.session.active {
  background: #e0e0e0;
}
</style>
