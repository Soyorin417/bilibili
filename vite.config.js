import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8080,  // 确保使用 8080 端口
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
              headers: proxyReq.getHeaders()
            });
          });
          proxy.on('proxyRes', (proxyRes, req, res) => {
            console.log('Received Response:', {
              statusCode: proxyRes.statusCode,
              headers: proxyRes.headers,
              path: req.url
            });
          });
        }
      }
    }
  }
}) 