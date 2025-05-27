package com.backend.bilibili.service.netty;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 所有连接到服务端的 Channel 都会加入这个集合
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[系统] - " + incoming.remoteAddress() + " 加入了聊天\n");
        channels.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[系统] - " + incoming.remoteAddress() + " 离开了聊天\n");
        channels.remove(incoming);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[用户 " + incoming.remoteAddress() + "]：" + msg + "\n");
            } else {
                channel.writeAndFlush("[我]：" + msg + "\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close(); // 出现异常关闭连接
    }
}
