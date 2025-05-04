<template>
  <div class="login-view">
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
import * as THREE from "three";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";

let scene = null;
let camera = null;
let renderer = null;
let geometry = null;
let material = null;
let points = null;
let animationId = null;
let controls = null;

export default {
  data() {
    return {
      username: "",
      password: "",
      galaxyParams: {
        count: 2000,
        size: 0.012,
        radius: 2.5,
      },
    };
  },
  mounted() {
    this.initThree();
    window.addEventListener("resize", this.handleResize);
  },
  beforeUnmount() {
    this.cleanupThree();
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    login() {
      this.$router.push("/");
      console.log(this.$el);
    },
    initThree() {
      if (animationId) cancelAnimationFrame(animationId);
      // 移除已有canvas
      const oldCanvas = document.querySelector("canvas[data-threejs-bg]");
      if (oldCanvas) oldCanvas.parentNode.removeChild(oldCanvas);

      scene = new THREE.Scene();
      scene.background = new THREE.Color(0x000000); // 黑色背景
      camera = new THREE.PerspectiveCamera(
        75,
        window.innerWidth / window.innerHeight,
        0.1,
        100
      );
      camera.position.z = 5;

      renderer = new THREE.WebGLRenderer({ alpha: true });
      renderer.setSize(window.innerWidth, window.innerHeight);
      renderer.domElement.style.position = "fixed";
      renderer.domElement.style.left = "0";
      renderer.domElement.style.top = "0";
      renderer.domElement.style.width = "100vw";
      renderer.domElement.style.height = "100vh";
      renderer.domElement.style.zIndex = "0";
      renderer.domElement.setAttribute("data-threejs-bg", "1");
      document.body.appendChild(renderer.domElement);

      controls = new OrbitControls(camera, renderer.domElement);
      controls.enableDamping = true;
      controls.dampingFactor = 0.05;
      controls.enablePan = false;
      controls.minDistance = 2;
      controls.maxDistance = 30;

      this.generateGalaxy();

      const animate = () => {
        if (points) points.rotation.y += 0.0015;
        if (controls) controls.update();
        renderer.render(scene, camera);
        animationId = requestAnimationFrame(animate);
      };
      animate();
    },
    generateGalaxy() {
      if (points) {
        geometry.dispose();
        material.dispose();
        scene.remove(points);
      }
      // 星空参数
      const starCount = 2500;
      const starSize = 0.008;
      const starRadius = 4.5;
      geometry = new THREE.BufferGeometry();
      const positions = new Float32Array(starCount * 3);
      const colors = new Float32Array(starCount * 3);
      for (let i = 0; i < starCount; i++) {
        const i3 = i * 3;
        // 均匀分布在球体表面
        const r = Math.random() * starRadius;
        const theta = Math.random() * 2 * Math.PI;
        const phi = Math.acos(2 * Math.random() - 1);
        positions[i3 + 0] = r * Math.sin(phi) * Math.cos(theta);
        positions[i3 + 1] = r * Math.sin(phi) * Math.sin(theta);
        positions[i3 + 2] = r * Math.cos(phi);
        // 随机颜色（完全随机RGB）
        const color = new THREE.Color();
        color.setRGB(Math.random(), Math.random(), Math.random());
        colors[i3 + 0] = color.r;
        colors[i3 + 1] = color.g;
        colors[i3 + 2] = color.b;
      }
      geometry.setAttribute("position", new THREE.BufferAttribute(positions, 3));
      geometry.setAttribute("color", new THREE.BufferAttribute(colors, 3));
      material = new THREE.PointsMaterial({
        size: starSize,
        sizeAttenuation: true,
        vertexColors: true, // 启用每个点的颜色
        depthWrite: false,
        blending: THREE.AdditiveBlending,
      });
      points = new THREE.Points(geometry, material);
      scene.add(points);
    },
    handleResize() {
      if (!camera || !renderer) return;
      const container = this.$el.querySelector(".login-view");
      const width = container.clientWidth;
      const height = container.clientHeight;
      camera.aspect = width / height;
      camera.updateProjectionMatrix();
      renderer.setSize(width, height);
    },
    cleanupThree() {
      if (animationId) cancelAnimationFrame(animationId);
      if (
        renderer &&
        renderer.domElement &&
        document.body.contains(renderer.domElement)
      ) {
        document.body.removeChild(renderer.domElement);
        renderer.dispose();
      }
      if (points) {
        geometry.dispose();
        material.dispose();
        scene.remove(points);
      }
      scene = null;
      camera = null;
      renderer = null;
      geometry = null;
      material = null;
      points = null;
      animationId = null;
      if (controls) {
        controls.dispose();
        controls = null;
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
