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
            <img :src="session.avatar" class="avatar ms-2" />
          </template>
        </MessageList>
        <MessageDetail
          v-if="selectedSession"
          :key="
            selectedSession.id +
            '_' +
            ((selectedSession.messages && selectedSession.messages.length) || 0)
          "
          class="detail"
          :session="selectedSession"
          @sendMessage="sendMessage"
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
import websocketClient from "@/utils/websocket";
import { userApi } from "@/api/user/user";
import { sessionApi } from "@/api/message/session";
import { messageApi } from "@/api/message/message";

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
        const newSessions = response.data;

        // 更新 sessions 列表
        this.sessions = newSessions;

        const currentSessionId = this.selectedSession?.id;
        if (currentSessionId) {
          const existing = newSessions.find((s) => s.id === currentSessionId);
          if (existing) {
            this.selectedSession = existing;
            return;
          }
        }

        // 第一次进入才自动选择第一个会话
        if (!this.selectedSession && newSessions.length > 0) {
          this.selectSession(newSessions[0]);
        }
      } catch (error) {
        console.error("Error fetching sessions:", error);
      }
    },
    async getMessages(fromUser, toUser) {
      try {
        const response = await messageApi.getPrivateMessages(fromUser, toUser);

        // 检查响应是否包含错误消息
        if (response.data && response.data.error_message) {
          console.error("获取消息失败:", response.data.error_message);
          // 如果出错，初始化一个空的消息数组
          const session = this.sessions.find(
            (s) =>
              (s.user1Id === fromUser && s.user2Id === toUser) ||
              (s.user1Id === toUser && s.user2Id === fromUser)
          );
          if (session) {
            session.messages = [];
            this.selectedSession = { ...session };
          }
          return;
        }

        // 确保 messages 是一个数组
        const messages = Array.isArray(response.data) ? response.data : [response.data];

        // 找到对应的会话
        const session = this.sessions.find(
          (s) =>
            (s.user1Id === fromUser && s.user2Id === toUser) ||
            (s.user1Id === toUser && s.user2Id === fromUser)
        );

        if (session) {
          // 确保消息按时间排序
          const sortedMessages = messages.sort((a, b) => {
            const timeA = new Date(a.send_time || a.timestamp).getTime();
            const timeB = new Date(b.send_time || b.timestamp).getTime();
            return timeA - timeB;
          });

          // 更新会话的消息
          session.messages = sortedMessages;

          // 更新选中的会话
          this.selectedSession = { ...session };
        }
      } catch (error) {
        console.error("获取消息失败:", error);
        // 如果出错，至少确保 messages 是一个空数组
        const session = this.sessions.find(
          (s) =>
            (s.user1Id === fromUser && s.user2Id === toUser) ||
            (s.user1Id === toUser && s.user2Id === fromUser)
        );
        if (session) {
          session.messages = [];
          this.selectedSession = { ...session };
        }
      }
    },

    async selectSession(session) {
      this.selectedSession = session;

      if (!session.messages || session.messages.length === 0) {
        const userInfoStr = JSON.parse(localStorage.getItem("userInfo"));
        const userId = parseInt(userInfoStr.id);

        try {
          await messageApi.markMessagesAsRead(session.id);
          console.log("标记已读成功");
        } catch (error) {
          console.error("标记消息已读失败：", error);
        }

        let targetUserId = userId === session.user1Id ? session.user2Id : session.user1Id;

        if (!userId || !targetUserId) {
          console.error("Invalid user IDs", userId, targetUserId);
          return;
        }
        this.getMessages(userId, targetUserId);
      }
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
        const res = await userApi.getUserByUid(receiverId);
        receiverName = res.data.username;
        receiverAvatar = res.data.avatar;
      } catch (e) {
        console.error("获取对方用户信息失败", e);
      }

      // 创建新消息对象
      const newMessage = {
        content: newMsg.content,
        timestamp: new Date().toISOString(),
        fromUserId: senderId,
        toUserId: receiverId,
        avatar: senderAvatar,
        username: senderName,
      };

      // 发送WebSocket消息 - 使用JSON格式
      const wsMessage = {
        type: "private",
        fromUserId: senderId,
        toUserId: receiverId,
        content: newMsg.content,
        timestamp: new Date().toISOString(),
        avatar: senderAvatar,
        username: senderName,
      };

      console.log("发送消息:", wsMessage);
      websocketClient.send(JSON.stringify(wsMessage));

      // 直接更新当前会话的消息列表
      if (!this.selectedSession.messages) {
        this.selectedSession.messages = [];
      }
      this.selectedSession.messages.push(newMessage);
      this.selectedSession = { ...this.selectedSession };

      // 更新会话列表中的会话
      const index = this.sessions.findIndex((s) => s.id === session.id);
      if (index !== -1) {
        this.sessions[index].messages = [...this.selectedSession.messages];
        this.sessions[index].summary = newMsg.content;
        this.sessions[index].lastMessageTime = new Date().toISOString();
      }

      // 保存消息到数据库（异步，不影响即时通讯）
      try {
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
        await messageApi.sendMessage(messagePayload);
        console.log("保存消息到数据库成功");
      } catch (error) {
        console.error("保存消息失败:", error);
      }
    },

    handleWebSocketMessage(event) {
      console.log("收到WebSocket消息:", event.data);

      try {
        const rawMessage = event.data;

        // 检查是否是系统消息
        if (rawMessage.startsWith("[系统]")) {
          console.log("收到系统消息:", rawMessage);
          return;
        }

        // 处理私聊消息（包括[用户]和[我]）
        if (rawMessage.startsWith("[用户]") || rawMessage.startsWith("[我]")) {
          // 解析消息内容
          const match = rawMessage.match(/^\[(用户|我)\] ([^：]+)：(.+)/);
          if (match) {
            const [, type] = match; // 只解构 type，移除未使用的 from
            try {
              // 解析JSON内容
              const messageData = JSON.parse(match[3]); // 直接使用 match[3] 获取 content
              console.log("解析后的消息数据:", messageData);

              // 创建新消息对象
              const newMessage = {
                content: messageData.content,
                timestamp: messageData.timestamp,
                fromUserId: messageData.fromUserId,
                toUserId: messageData.toUserId,
                avatar: messageData.avatar,
                username: messageData.username,
              };

              // 查找或创建会话
              let session = this.sessions.find(
                (s) =>
                  (s.user1Id === messageData.fromUserId &&
                    s.user2Id === messageData.toUserId) ||
                  (s.user1Id === messageData.toUserId &&
                    s.user2Id === messageData.fromUserId)
              );

              console.log("找到的会话:", session);

              // 如果会话不存在，创建一个新的
              if (!session) {
                session = {
                  id: Date.now(),
                  user1Id: messageData.fromUserId,
                  user2Id: messageData.toUserId,
                  messages: [],
                  username: messageData.username,
                  avatar: messageData.avatar,
                  summary: messageData.content,
                  lastMessageTime: messageData.timestamp,
                };
                this.sessions.unshift(session);
                console.log("创建新会话:", session);
              }

              // 直接更新会话消息
              if (!session.messages) {
                session.messages = [];
              }
              session.messages.push(newMessage);
              session.summary = messageData.content;
              session.lastMessageTime = messageData.timestamp;

              console.log("更新后的会话消息:", session.messages);

              // 如果是收到的消息（[用户]），自动选择会话
              if (type === "用户") {
                console.log("自动选择会话:", session);
                this.$nextTick(() => {
                  this.selectSession(session);
                });
              }

              // 如果当前选中的会话就是这条消息的会话，直接更新消息列表
              if (this.selectedSession && this.selectedSession.id === session.id) {
                this.selectedSession.messages = [...session.messages];
                this.selectedSession = { ...this.selectedSession };
                console.log("更新当前会话消息:", this.selectedSession.messages);
              }
            } catch (error) {
              console.error("解析消息JSON失败:", error);
            }
          }
          return;
        }

        // 处理群聊消息
        if (rawMessage.startsWith("[群聊]")) {
          console.log("收到群聊消息:", rawMessage);
          return;
        }

        // 处理其他类型的消息
        console.log("收到其他类型消息:", rawMessage);
      } catch (error) {
        console.error("处理WebSocket消息时出错:", error);
      }
    },
  },

  mounted() {
    console.log("组件已挂载");
    this.getAllSessions();

    // 检查用户是否已登录
    const userInfo = localStorage.getItem("userInfo");
    if (!userInfo) {
      console.log("用户未登录，不建立WebSocket连接");
      return;
    }

    // 确保WebSocket连接已建立并注册用户
    if (!websocketClient.ws || websocketClient.ws.readyState !== WebSocket.OPEN) {
      console.log("WebSocket未连接，开始连接");
      websocketClient.connect();

      // 等待连接建立后再注册用户
      const checkConnection = setInterval(() => {
        if (websocketClient.ws && websocketClient.ws.readyState === WebSocket.OPEN) {
          console.log("WebSocket已连接，注册用户");
          try {
            const userInfo = JSON.parse(localStorage.getItem("userInfo"));
            if (userInfo && userInfo.id) {
              websocketClient.send(`login:${userInfo.id}`);
              // 添加消息监听器
              websocketClient.ws.addEventListener("message", this.handleWebSocketMessage);
              console.log("已添加消息监听器");
            } else {
              console.log("用户信息无效，不注册WebSocket");
            }
          } catch (error) {
            console.error("解析用户信息失败:", error);
          }
          clearInterval(checkConnection);
        }
      }, 100);
    } else {
      console.log("WebSocket已连接，直接注册用户");
      try {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        if (userInfo && userInfo.id) {
          websocketClient.send(`login:${userInfo.id}`);
          websocketClient.ws.addEventListener("message", this.handleWebSocketMessage);
          console.log("已添加消息监听器");
        } else {
          console.log("用户信息无效，不注册WebSocket");
        }
      } catch (error) {
        console.error("解析用户信息失败:", error);
      }
    }
  },

  beforeUnmount() {
    // 移除WebSocket消息监听器
    if (websocketClient.ws && websocketClient.ws.readyState === WebSocket.OPEN) {
      websocketClient.ws.removeEventListener("message", this.handleWebSocketMessage);
      // 关闭WebSocket连接
      websocketClient.ws.close();
    }
  },

  watch: {
    selectedSession: {
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
