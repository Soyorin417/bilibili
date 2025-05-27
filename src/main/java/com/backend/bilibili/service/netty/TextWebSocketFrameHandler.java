package com.backend.bilibili.service.netty;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        group.writeAndFlush(new TextWebSocketFrame("[系统] - " + incoming.remoteAddress() + " 加入聊天"));
        group.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        group.writeAndFlush(new TextWebSocketFrame("[系统] - " + incoming.remoteAddress() + " 离开聊天"));
        group.remove(incoming);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        Channel incoming = ctx.channel();
        String content = msg.text();

        for (Channel channel : group) {
            if (channel != incoming) {
                channel.writeAndFlush(new TextWebSocketFrame("[用户] " + incoming.remoteAddress() + "：" + content));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("[我]：" + content));
            }
        }
    }
}
