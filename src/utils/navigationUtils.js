import { spaceApi } from "@/api/user/space";


export async function goToUserProfile(router, userId) {
  if (!userId) {
    console.warn("goToUserProfile: userId 为空，无法跳转");
    return;
  }

  try {
    // 可选：预加载数据（不一定非要在这里）
    const res = await spaceApi.getUserPublishedVideos(userId);
    console.log("预加载作者视频成功：", res);

    // 执行跳转
    router.push({
      name: "UserProfile", // 注意：你必须在路由配置中设置 name
      params: { userId }
    });
  } catch (error) {
    console.error("跳转用户主页失败：", error);
  }
}