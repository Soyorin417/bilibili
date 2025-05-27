package com.backend.bilibili.config;

import com.backend.bilibili.service.netty.WebSocketServer;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WebSocketServerRunner {

    private final WebSocketServer webSocketServer = new WebSocketServer();

    @PostConstruct
    public void startWebSocketServer() {
        new Thread(() -> {
            try {
                int port = 9002;
                webSocketServer.start(port);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("Netty WebSocket 服务已在项目启动时自动运行。");
    }
}
