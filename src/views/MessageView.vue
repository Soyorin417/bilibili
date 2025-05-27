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
      const url = "http://localhost:8081/api/sessions";
      const token = localStorage.getItem("token");
      if (!token) {
        console.error("Token is missing");
        return;
      }
      try {
        const response = await axios.get(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        this.sessions = response.data;
        console.log(this.sessions, "this.sessions");
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
      if (session) {
        if (!session.messages) {
          session.messages = [];
        }

        // 添加用户信息到消息中
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        const messageWithUserInfo = {
          ...newMsg,
          avatar: userInfo.avatar,
          username: userInfo.username,
          fromUserId: userInfo.id,
          toUserId: session.user1Id === userInfo.id ? session.user2Id : session.user1Id,
          timestamp: new Date().toISOString(),
        };

        // 立即在发送者界面显示消息
        session.messages.push(messageWithUserInfo);
        this.selectedSession = { ...session };

        // 发送消息到WebSocket服务器
        const messageData = {
          type: "private_message",
          fromUserId: userInfo.id,
          toUserId: session.user1Id === userInfo.id ? session.user2Id : session.user1Id,
          content: newMsg.content,
          timestamp: new Date().toISOString(),
          avatar: userInfo.avatar,
          username: userInfo.username,
        };

        websocketClient.send(messageData);

        // 同时保存到后端数据库
        const url = `http://localhost:8081/api/messages`;
        const token = localStorage.getItem("token");
        try {
          const response = await axios.post(url, messageWithUserInfo, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          console.log("Message saved to database:", response.data);
        } catch (error) {
          console.error("Error saving message to database:", error);
        }
      }
    },

    handleWebSocketMessage(event) {
      try {
        const rawMessage = event.data;
        console.log("处理WebSocket消息:", rawMessage);

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
        console.log("Raw message:", event.data);
      }
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
