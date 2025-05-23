const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://113.45.69.13:8081',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        },
        ws: true,
        secure: false,
        logLevel: 'debug'
      }
    }
  }
})
