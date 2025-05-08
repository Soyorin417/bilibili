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
        <router-link to="/register">没有账号？去注册</router-link>
        <button class="login-button" @click="login">登录</button>
      </div>
    </div>
  </div>
</template>

<script>
import StarfieldBackground from "@/components/three/StarfieldBackground.vue";
import axios from "axios";
import Swal from "sweetalert2";
export default {
  components: { StarfieldBackground },
  data() {
    return {
      token: "",
      userInfo: "",
      username: "",
      password: "",
    };
  },
  methods: {
    async login() {
      const url = "http://127.0.0.1:8081/login";
      try {
        const response = await axios.post(url, {
          username: this.username,
          password: this.password,
        });

        if (response.data.error_message === "success") {
          const token = response.data.token;
          localStorage.setItem("token", token);

          // 设置默认请求头，main.js 会自动请求用户信息
          axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

          this.username = "";
          this.password = "";

          // 触发刷新或跳转
          this.$router.push("/");
        } else {
          Swal.fire("登录失败", response.data.error_message, "error");
        }
      } catch (error) {
        Swal.fire(
          "登录失败",
          error.response?.data?.error_message ||
            error.response?.data?.message ||
            error.message ||
            "登录失败",
          "error"
        );
      }
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
