import { sessionApi } from "@/api/message/session";
import { ElMessage } from "element-plus";

export async function goToChat(userInfo, targetUserId, targetUserAvatar, targetUsername, router) {
  if (!userInfo || !userInfo.id) {
    ElMessage.warning("请先登录后再聊天");
    return;
  }
  if (userInfo.id === targetUserId) {
    ElMessage.warning("不能和自己聊天");
    return;
  }
  try {
    await sessionApi.createSession({
      user1Id: userInfo.id,
      user2Id: targetUserId,
      avatar: targetUserAvatar,
      name: `${targetUsername}`,
      summary: "",
    });
    router.push("/message");
  } catch (error) {
    console.error("创建会话失败:", error);
    ElMessage.error("创建会话失败，请重试");
  }
} 