<template>
  <div class="login-view">
    <StarfieldBackground />
    <div class="register-container">
      <div class="register-title">注册</div>
      <div class="register-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="username" />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" />
        </div>
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input type="password" id="confirmPassword" v-model="confirmPassword" />
        </div>
        <router-link to="/login">已有账号？去登录</router-link>
        <button class="register-button" @click="register">注册</button>
      </div>
    </div>
  </div>
</template>

<script>
import StarfieldBackground from "@/components/three/StarfieldBackground.vue";
import axios from "axios";
import Swal from "sweetalert2";
export default {
  name: "RegisterView",
  components: { StarfieldBackground },
  data() {
    return {
      username: "",
      password: "",
      confirmPassword: "",
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
    switchRegister() {
      this.isRegister = true;
    },
    async register() {
      const url = "http://127.0.0.1:8081/register";
      try {
        const response = await axios.post(url, {
          username: this.username,
          password: this.password,
          confirmPassword: this.confirmPassword,
        });
        if (response.data.error_message === "success") {
          Swal.fire("成功", "注册成功！", "success");
          this.$router.push("/login");
        } else {
          Swal.fire("注册失败", response.data.error_message, "error");
        }
      } catch (error) {
        Swal.fire(
          "注册失败",
          error.response?.data?.message || error.message || "注册失败",
          "error"
        );
      }
    },

    handleResize() {
      // 处理窗口大小变化的逻辑
    },
  },
};
</script>

<style scoped>
.login-view {
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: none;
  z-index: 1;
}
.register-container {
  pointer-events: auto;
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
.register-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 24px;
}
.register-form {
  margin-bottom: 16px;
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
.register-button {
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
.register-button:hover {
  background-color: #357ab8;
}
</style>
