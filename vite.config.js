import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  define: {
    __VUE_OPTIONS_API__: true,
    __VUE_PROD_DEVTOOLS__: false,
    __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false,
  },
  server: {
    port: 8080, // 保持使用 8080 端口
    proxy: {
      '/danmaku': {
        target: 'http://121.36.211.155:9000',
        changeOrigin: true,
        configure: (proxy, options) => {
          proxy.on('error', (err, req, res) => {
            console.log('proxy error:', err);
          });
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('Sending Request:', {
              method: req.method,
              path: req.url,
              headers: proxyReq.getHeaders(),
            });
          });
          proxy.on('proxyRes', (proxyRes, req, res) => {
            console.log('Received Response:', {
              statusCode: proxyRes.statusCode,
              headers: proxyRes.headers,
              path: req.url,
            });
          });
        },
      },
    },
  },
});
