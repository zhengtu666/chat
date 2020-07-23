package com.example.chat.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * @author: liuhuan
 * @Description: 群聊服务器
 * @date: 2020/7/14
 */
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        // 创建两个事件循环组 bossGroup 只需要一个线程处理，主要用来就收处理连接事件 workGroup 使用默认线程 cpu核数 * 2 处理读事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建启动程序
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 启动程序参数配置
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // 输入连接指示（对连接的请求）的最大队列长度被设置为 backlog 参数。如果队列满时收到连接指示，则拒绝该连接。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // websocket 基于http协议 所以需要http解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 大数据块支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 对httpmessage进行聚合
                            pipeline.addLast(new HttpObjectAggregator(1024*64));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/talkAll"));
                            pipeline.addLast("myHandler",new MyWebSocketTextHandler());
                        }
                    });

            System.out.println("群聊系统已就绪");
            // 启动
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer(80);
        try {
            groupChatServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
