<template>
  <div class="login-view">
    <StarfieldBackground />
    <div class="login-container">
      <div class="login-title">登录</div>
      <div class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="username" />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" />
        </div>
        <button class="login-button" @click="login">登录</button>
      </div>
    </div>
  </div>
</template>

<script>
import StarfieldBackground from "@/components/three/StarfieldBackground.vue";
import axios from "axios";
import userData from "@/data/userData";

export default {
  components: { StarfieldBackground },
  data() {
    return {
      username: "",
      password: "",
      isLogin: !!localStorage.getItem("token"),
      galaxyParams: {
        count: 2000,
        size: 0.012,
        radius: 2.5,
      },
    };
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    async login() {
      const url = "http://127.0.0.1:8081/login";
      try {
        const response = await axios.post(url, {
          username: this.username,
          password: this.password,
        });

        console.log("Response data:", response.data);

        if (response.data.error_message === "success") {
          const token = response.data.token;
          const userInfo = userData || {}; // 假设接口返回了用户信息

          // 使用 Vuex 的 login action
          this.$store.dispatch("user/login", {
            token,
            userInfo,
          });

          // 设置 Axios 默认请求头
          axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

          // 跳转页面，不需要强制刷新
          this.$router.push("/");

          // 调试信息
          console.log(this.username, "用户名");
          console.log(this.password, "密码");
          console.log(token, "token");
        }
      } catch (error) {
        if (error.response) {
          console.error("响应错误：", error.response.data);
        } else {
          console.error("请求错误：", error.message);
        }
      }
    },
    handleResize() {
      // 处理窗口大小变化的逻辑
    },
    logout() {
      // 1. 清除本地 token
      localStorage.removeItem("token");
      // 2. 清除 axios 默认请求头（如果有设置）
      if (window.axios) {
        delete window.axios.defaults.headers.common["Authorization"];
      }
      // 3. 跳转到登录页
      this.$router.push("/login");
      // 4. 可选：清除用户信息等其他本地状态
      // this.username = "";
      // this.password = "";

      this.isLogin = false; // 登出
    },
  },
};
</script>

<style scoped>
.login-view {
  position: absolute; /* 绝对定位防止受到其他布局影响 */
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: center; /* 水平居中 */
  pointer-events: none; /* 防止背景干扰点击 */
  z-index: 1; /* 背景在0，登录框在上 */
}

.login-container {
  pointer-events: auto; /* 恢复表单可交互 */
  background: rgba(255, 255, 255, 0.75);
  padding: 40px 32px;
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  min-width: 320px;
  z-index: 2;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-weight: 500;
  margin-bottom: 4px;
}

.form-group input {
  width: 100%;
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.login-button {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  color: white;
  background-color: #4a90e2;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background-color: #357ab8;
}
</style>
