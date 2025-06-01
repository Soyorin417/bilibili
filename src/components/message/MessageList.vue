<template>
  <div>
    <div
      v-for="session in filteredSessions"
      :key="session.id"
      :class="['session', { active: session.id === (selected && selected.id) }]"
      @click="$emit('select', session)"
      class="ms-4 mt-2"
    >
      <img :src="getSessionAvatar(session)" class="avatar ms-2" />
      <div class="ms-4">
        <div class="name">{{ getSessionName(session) }}</div>
        <div class="summary">{{ session.summary }}</div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";

export default {
  props: {
    selected: {
      type: [String, Number, Object],
      default: null,
    },
    sessions: {
      type: Array,
      default: () => [],
    },
  },
  computed: {
    ...mapGetters("user", ["userInfo"]),
    currentUser() {
      return this.userInfo;
    },
    filteredSessions() {
      if (!Array.isArray(this.sessions)) {
        return [];
      }
      return this.sessions.filter(
        (session) =>
          session.user1Id === this.currentUser.id ||
          session.user2Id === this.currentUser.id
      );
    },
  },
  methods: {
    getSessionAvatar(session) {
      // 如果当前会话的头像是当前用户的头像，则显示对话者的头像
      if (session.avatar === this.currentUser.avatar) {
        // 从消息中获取另一个用户的头像
        if (session.messages && session.messages.length > 0) {
          // 找到第一条不是当前用户发送的消息
          const otherUserMessage = session.messages.find(
            (msg) => msg.senderId !== this.currentUser.id
          );
          if (otherUserMessage) {
            return otherUserMessage.avatar;
          }
        }
      }
      // 如果没有找到其他用户的消息或会话头像不是当前用户的头像，则显示会话本身的头像
      return session.avatar;
    },
    getSessionName(session) {
      // 如果当前会话的名字包含当前用户的名字，则显示对话者的名字
      if (session.name.includes(this.currentUser.username)) {
        // 从消息中获取另一个用户的名字
        if (session.messages && session.messages.length > 0) {
          // 找到第一条不是当前用户发送的消息
          const otherUserMessage = session.messages.find(
            (msg) => msg.senderId !== this.currentUser.id
          );
          if (otherUserMessage) {
            // 从消息中提取发送者名字

            return otherUserMessage.name;
          }
        }
      }
      return session.name;
    },
  },
};
</script>
<style scoped>
.session {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
}
.session.active {
  background: #f0faff;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.name {
  font-weight: bold;
  text-align: left;
}
.summary {
  color: #888;
  text-align: left;
  font-size: 13px;
}
</style>
