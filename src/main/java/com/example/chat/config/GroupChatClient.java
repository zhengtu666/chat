package com.example.chat.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author: liuhuan
 * @Description: TODO
 * @date: 2020/7/22
 */
public class GroupChatClient {

    public void connect(){
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder(Charset.forName("utf-8")));
                            pipeline.addLast("ecoder", new StringEncoder(Charset.forName("utf-8")));
                            pipeline.addLast("handler1",new MyHandler1());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 80).sync();

            channelFuture.channel().closeFuture().sync();

            System.out.println(channelFuture.channel().remoteAddress());
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                channelFuture.channel().writeAndFlush(msg);
            }

        } catch (InterruptedException e) {
            group.shutdownGracefully();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GroupChatClient().connect();
    }
}
