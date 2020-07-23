package com.example.chat.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author: liuhuan
 * @Description:
 * @date: 2020/7/22
 */
public class MyHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    // 建立连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channels.writeAndFlush(ctx.channel().remoteAddress()+ "上线了");
        channels.add(ctx.channel());
    }

    // 断开连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channels.writeAndFlush(ctx.channel().remoteAddress()+ "离线了");
        channels.remove(ctx.channel());
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 排除自己转发消息
        channels.forEach(channel1 -> {
            if (!channel.equals(channel1)){
                channel1.writeAndFlush(channel.remoteAddress()+ "说:" + msg);
            }
        });
    }
}
