package com.backend.bilibili.service.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        incoming.writeAndFlush(new TextWebSocketFrame("[系统] 请输入你的用户名"));
        group.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        String username = getUsernameByChannel(incoming);
        if (username != null) {
            userChannelMap.remove(username);
            group.writeAndFlush(new TextWebSocketFrame("[系统] - 用户 " + username + " 离开聊天"));
        }
        group.remove(incoming);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Channel incoming = ctx.channel();
        String content = msg.text().trim();

        // 登录逻辑
        if (content.startsWith("login:")) {
            String username = content.substring("login:".length()).trim();
            if (username.isEmpty()) {
                incoming.writeAndFlush(new TextWebSocketFrame("[系统] 用户名不能为空"));
                return;
            }

            if (userChannelMap.containsKey(username)) {
                incoming.writeAndFlush(new TextWebSocketFrame("[系统] 用户名已被占用，请换一个"));
                return;
            }

            userChannelMap.put(username, incoming);
            incoming.writeAndFlush(new TextWebSocketFrame("[系统] 欢迎你，" + username));
            group.writeAndFlush(new TextWebSocketFrame("[系统] - 用户 " + username + " 加入聊天"));
            return;
        }

        try {
            // 尝试解析JSON消息
            Map<String, Object> messageData = objectMapper.readValue(content, Map.class);
            String type = (String) messageData.get("type");

            if ("private".equals(type)) {
                // 处理私聊消息
                Integer fromUserId = (Integer) messageData.get("fromUserId");
                Integer toUserId = (Integer) messageData.get("toUserId");
                String messageContent = (String) messageData.get("content");
                String timestamp = (String) messageData.get("timestamp");
                String avatar = (String) messageData.get("avatar");
                String username = (String) messageData.get("username");

                // 获取接收者的Channel
                Channel toChannel = userChannelMap.get(toUserId.toString());
                if (toChannel != null && toChannel != incoming) {
                    // 发送给接收者
                    toChannel.writeAndFlush(new TextWebSocketFrame(
                            "[用户] " + incoming.remoteAddress() + "：" + content
                    ));
                    // 发送给发送者
                    incoming.writeAndFlush(new TextWebSocketFrame(
                            "[我]：" + content
                    ));
                } else {
                    incoming.writeAndFlush(new TextWebSocketFrame(
                            "[系统] 用户 " + toUserId + " 不在线或不存在"
                    ));
                }
                return;
            }
        } catch (Exception e) {
            // 如果不是JSON格式，按原来的逻辑处理
            System.out.println("非JSON消息，按原逻辑处理: " + content);
        }

        // 群聊逻辑
        String sender = getUsernameByChannel(incoming);
        for (Channel channel : group) {
            if (channel != incoming) {
                channel.writeAndFlush(new TextWebSocketFrame("[群聊] " + sender + "：" + content));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("[我]：" + content));
            }
        }
    }

    private String getUsernameByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : userChannelMap.entrySet()) {
            if (entry.getValue().equals(channel)) {
                return entry.getKey();
            }
        }
        return null;
    }
}