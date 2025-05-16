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

export default {
  name: "MessageView",
  components: { MessageNav, MessageList, MessageDetail, VideoBar },
  data() {
    return {
      sessions: [],
      selectedSession: null,
      currentUser: localStorage.getItem("username") || "",
    };
  },
  methods: {
    async getAllSessions() {
      const url = "http://localhost:8081/sessions";
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
        if (this.sessions.length > 0) {
          this.selectSession(this.sessions[0]);
        }
      } catch (error) {
        console.error("Error fetching sessions:", error);
      }
    },

    async getMessages(fromUser, toUser) {
      const url = `http://localhost:8081/messages/private?user1=${fromUser}&user2=${toUser}`;
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
        session.messages.push(newMsg);
        this.selectedSession = { ...session };
        console.log(this.selectedSession, "this.selectedSession");
        const url = `http://localhost:8081/messages`;
        const token = localStorage.getItem("token");
        const response = await axios.post(url, newMsg, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log(response.data, "response.data");
      }
    },
  },

  mounted() {
    this.getAllSessions();
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
</style>
