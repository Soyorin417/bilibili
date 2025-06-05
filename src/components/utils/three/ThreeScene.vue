<template>
  <div ref="threeContainer" class="three-container">
    <canvas ref="webglCanvas" class="webgl"></canvas>
  </div>
</template>

<script>
import * as THREE from "three";
import { markRaw } from "vue";
import imageTexture from "@/assets/StandardCubeMap.png";

export default {
  name: "ThreeScene",
  data() {
    return {
      animationFrameId: null,
      sizes: {
        width: 800,
        height: 600,
      },
    };
  },
  mounted() {
    this.initThree();
    this.animate();
  },
  beforeUnmount() {
    // 清理资源
    if (this.animationFrameId) {
      cancelAnimationFrame(this.animationFrameId);
    }
    if (this.renderer) {
      this.renderer.dispose();
    }
  },
  methods: {
    initThree() {
      // 1. 创建场景
      this.scene = markRaw(new THREE.Scene());
      const image = new Image();
      image.src = imageTexture;

      const texture = new THREE.Texture(image);
      image.onload = () => {
        texture.needsUpdate = true;
      };

      // 2. 创建立方体
      const geometry = new THREE.BoxGeometry(1, 1, 1);
      const material = new THREE.MeshBasicMaterial({ map: texture });
      this.mesh = markRaw(new THREE.Mesh(geometry, material));
      this.scene.add(this.mesh);

      // 3. 创建相机
      this.camera = markRaw(
        new THREE.PerspectiveCamera(75, this.sizes.width / this.sizes.height)
      );
      this.camera.position.z = 3; // 调整相机位置
      this.scene.add(this.camera);

      // 4. 创建渲染器
      const canvas = this.$refs.webglCanvas; // 使用 ref 获取 canvas
      this.renderer = markRaw(new THREE.WebGLRenderer({ canvas }));
      this.renderer.setSize(this.sizes.width, this.sizes.height);

      // 5. 初始渲染
      this.renderer.render(this.scene, this.camera);
    },
    animate() {
      // 渲染循环
      this.animationFrameId = requestAnimationFrame(this.animate);

      // 旋转立方体
      this.mesh.rotation.x += 0.01;
      this.mesh.rotation.y += 0.01;

      // 渲染场景
      this.renderer.render(this.scene, this.camera);
    },
  },
};
</script>

<style scoped>
.three-container {
  width: 120vh;
  height: 120vh;
  background-color: #000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.webgl {
  width: 800px;
  height: 600px;
  border: 1px solid #fff;
}
</style>
