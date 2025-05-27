// WebSocket configuration
const WS_URL = "ws://localhost:9002/ws"; // 添加 /ws 路径

class WebSocketClient {
  constructor() {
    this.ws = null;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 5;
    this.reconnectInterval = 3000; // 3 seconds
    this.isConnecting = false;
    this.messageHandlers = new Set();
  }

  connect() {
    if (this.isConnecting) {
      console.log("WebSocket connection already in progress...");
      return;
    }

    this.isConnecting = true;
    try {
      console.log("Attempting to connect to WebSocket server at:", WS_URL);
      this.ws = new WebSocket(WS_URL);

      this.ws.onopen = () => {
        console.log("WebSocket connected successfully");
        this.reconnectAttempts = 0;
        this.isConnecting = false;
        // 重新注册所有消息处理器
        this.messageHandlers.forEach((handler) => {
          this.ws.addEventListener("message", handler);
        });
      };

      this.ws.onmessage = (event) => {
        try {
          // 尝试解析为JSON
          const data = JSON.parse(event.data);
          console.log("Received WebSocket message (JSON):", data);
        } catch (error) {
          // 如果不是JSON，直接处理原始消息
          console.log("Received WebSocket message (raw):", event.data);
          // 这里可以添加对原始消息的处理逻辑
        }
      };

      this.ws.onclose = (event) => {
        console.log("WebSocket connection closed:", {
          code: event.code,
          reason: event.reason,
          wasClean: event.wasClean,
        });
        this.isConnecting = false;

        // 根据关闭代码处理不同的情况
        switch (event.code) {
          case 1000: // 正常关闭
            console.log("WebSocket closed normally");
            break;
          case 1006: // 异常关闭
            console.error("WebSocket connection closed abnormally. Possible reasons:");
            console.error("1. Server is not running");
            console.error("2. Server is not listening on port 9002");
            console.error("3. Firewall is blocking the connection");
            console.error("4. Server is not configured to accept WebSocket connections");
            break;
          case 1015: // TLS握手失败
            console.error("TLS handshake failed");
            break;
          default:
            console.error("WebSocket closed with code:", event.code);
        }

        this.reconnect();
      };

      this.ws.onerror = (error) => {
        console.error("WebSocket error:", error);
        console.error("Connection details:", {
          url: WS_URL,
          readyState: this.ws ? this.ws.readyState : "null",
          connecting: this.isConnecting,
        });
        this.isConnecting = false;
      };
    } catch (error) {
      console.error("Error creating WebSocket connection:", error);
      this.isConnecting = false;
      this.reconnect();
    }
  }

  reconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      console.log(
        `Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts})...`
      );
      setTimeout(() => this.connect(), this.reconnectInterval);
    } else {
      console.error(
        "Max reconnection attempts reached. Please check if the WebSocket server is running."
      );
    }
  }

  addMessageHandler(handler) {
    this.messageHandlers.add(handler);
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.addEventListener("message", handler);
    }
  }

  removeMessageHandler(handler) {
    this.messageHandlers.delete(handler);
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      this.ws.removeEventListener("message", handler);
    }
  }

  send(message) {
    if (this.ws && this.ws.readyState === WebSocket.OPEN) {
      try {
        // 如果消息是对象，转换为JSON字符串
        const messageToSend = typeof message === 'object' ? JSON.stringify(message) : message;
        this.ws.send(messageToSend);
      } catch (error) {
        console.error("Error sending message:", error);
      }
    } else {
      console.error(
        "WebSocket is not connected. Current state:",
        this.ws ? this.ws.readyState : "null"
      );
      // 尝试重新连接
      this.connect();
    }
  }

  close() {
    if (this.ws) {
      this.ws.close();
      this.ws = null;
    }
  }
}

// Create and export a singleton instance
const websocketClient = new WebSocketClient();
export default websocketClient;
