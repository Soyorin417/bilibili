<template>
  <div ref="container" class="starfield-bg"></div>
</template>

<script>
import * as THREE from "three";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";

export default {
  name: "StarfieldBackground",
  mounted() {
    this.initThree();
    window.addEventListener("resize", this.handleResize);
  },
  beforeUnmount() {
    this.cleanupThree();
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    initThree() {
      this.scene = new THREE.Scene();
      this.scene.background = new THREE.Color(0x000000);
      this.camera = new THREE.PerspectiveCamera(
        75,
        window.innerWidth / window.innerHeight,
        0.1,
        100
      );
      this.camera.position.z = 5;

      this.renderer = new THREE.WebGLRenderer({ alpha: true });
      this.renderer.setSize(window.innerWidth, window.innerHeight);
      this.renderer.domElement.style.position = "fixed";
      this.renderer.domElement.style.left = "0";
      this.renderer.domElement.style.top = "0";
      this.renderer.domElement.style.width = "100vw";
      this.renderer.domElement.style.height = "100vh";
      this.renderer.domElement.style.zIndex = "0";
      this.$refs.container.appendChild(this.renderer.domElement);

      this.controls = new OrbitControls(this.camera, this.renderer.domElement);
      this.controls.enableDamping = true;
      this.controls.dampingFactor = 0.05;
      this.controls.enablePan = false;
      this.controls.minDistance = 2;
      this.controls.maxDistance = 30;

      this.generateGalaxy();

      const animate = () => {
        if (this.points) this.points.rotation.y += 0.0015;
        if (this.controls) this.controls.update();
        this.renderer.render(this.scene, this.camera);
        this.animationId = requestAnimationFrame(animate);
      };
      animate();
    },
    generateGalaxy() {
      if (this.points) {
        this.geometry.dispose();
        this.material.dispose();
        this.scene.remove(this.points);
      }
      const starCount = 2500;
      const starSize = 0.008;
      const starRadius = 4.5;
      this.geometry = new THREE.BufferGeometry();
      const positions = new Float32Array(starCount * 3);
      const colors = new Float32Array(starCount * 3);
      for (let i = 0; i < starCount; i++) {
        const i3 = i * 3;
        const r = Math.random() * starRadius;
        const theta = Math.random() * 2 * Math.PI;
        const phi = Math.acos(2 * Math.random() - 1);
        positions[i3 + 0] = r * Math.sin(phi) * Math.cos(theta);
        positions[i3 + 1] = r * Math.sin(phi) * Math.sin(theta);
        positions[i3 + 2] = r * Math.cos(phi);
        const color = new THREE.Color();
        color.setRGB(Math.random(), Math.random(), Math.random());
        colors[i3 + 0] = color.r;
        colors[i3 + 1] = color.g;
        colors[i3 + 2] = color.b;
      }
      this.geometry.setAttribute("position", new THREE.BufferAttribute(positions, 3));
      this.geometry.setAttribute("color", new THREE.BufferAttribute(colors, 3));
      this.material = new THREE.PointsMaterial({
        size: starSize,
        sizeAttenuation: true,
        vertexColors: true,
        depthWrite: false,
        blending: THREE.AdditiveBlending,
      });
      this.points = new THREE.Points(this.geometry, this.material);
      this.scene.add(this.points);
    },
    handleResize() {
      if (!this.camera || !this.renderer) return;
      const width = window.innerWidth;
      const height = window.innerHeight;
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
      this.renderer.setSize(width, height);
    },
    cleanupThree() {
      if (this.animationId) cancelAnimationFrame(this.animationId);
      if (
        this.renderer &&
        this.renderer.domElement &&
        this.$refs.container.contains(this.renderer.domElement)
      ) {
        this.$refs.container.removeChild(this.renderer.domElement);
        this.renderer.dispose();
      }
      if (this.points) {
        this.geometry.dispose();
        this.material.dispose();
        this.scene.remove(this.points);
      }
      this.scene = null;
      this.camera = null;
      this.renderer = null;
      this.geometry = null;
      this.material = null;
      this.points = null;
      this.animationId = null;
      if (this.controls) {
        this.controls.dispose();
        this.controls = null;
      }
    },
  },
};
</script>

<style scoped>
.starfield-bg {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  z-index: 0;
  pointer-events: auto;
}
</style>
