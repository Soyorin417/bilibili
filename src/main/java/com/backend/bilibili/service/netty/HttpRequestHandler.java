package com.backend.bilibili.service.netty;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (wsUri.equalsIgnoreCase(req.uri())) {
            // 是 WebSocket 请求，转发
            ctx.fireChannelRead(req.retain());
        } else {
            // 非 WebSocket 请求，返回 404
            FullHttpResponse res = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.NOT_FOUND
            );
            ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        }
    }

    // 推荐：添加异常处理器，防止连接异常时未处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
