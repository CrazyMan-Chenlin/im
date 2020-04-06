package com.roy.im.connection.server;

import com.roy.im.connection.config.ServerConfig;
import com.roy.im.connection.handler.CloseIdleChannelHandler;
import com.roy.im.connection.handler.WebsocketRouterHandler;
import com.roy.im.connection.pool.EnhancedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author chenlin
 */
@Component
@Slf4j
@Order(value = 1)
public class WebSocketServer implements CommandLineRunner {

    @Autowired
    private ServerConfig serverConfig;

    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    private EventExecutorGroup eventExecutorGroup;

    @Autowired
    private WebsocketRouterHandler websocketRouterHandler;

    @Autowired
    private CloseIdleChannelHandler closeIdleChannelHandler;

    public void start() throws InterruptedException {
        if (serverConfig.port == 0) {
            log.info("WebSocket Server not config.");
            return;
        }

        log.info("WebSocket Server is starting");

        eventExecutorGroup = new DefaultEventExecutorGroup(serverConfig.userThreads, new EnhancedThreadFactory("WebSocketBizThreadPool"));

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //先添加websocket相关的编解码器和协议处理器
                pipeline.addLast(new HttpServerCodec());
                //这个是用来解析http请求中message body
                pipeline.addLast(new HttpObjectAggregator(65536));
                //日志处理器
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                //添加WebSocket协议的处理器
                pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true));
                //再添加服务端业务消息的总处理器
                pipeline.addLast(websocketRouterHandler);
                //服务端添加一个idle处理器，如果一段时间socket中没有消息传输，服务端会强制断开
                pipeline.addLast(new IdleStateHandler(serverConfig.getReadIdleSecond(),
                        serverConfig.getWriteIdleSecond(), serverConfig.getAllIdleSecond()));
                //定义用户触发器，用来清除用户上线信息
                pipeline.addLast(closeIdleChannelHandler);
            }

        };

        bootstrap = newServerBootstrap();
        bootstrap.childHandler(initializer);

        channelFuture = bootstrap.bind(serverConfig.port).sync();

        /*在JVM关闭之前，正常关闭Netty的网络连接
        (Netty用到了一些堆外内存，如果直接关闭虚拟机，可能会造成一些内存无法释放)*/
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        log.info("WebSocket Server start success on:" + serverConfig.port);

            try {
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("WebSocket Server start failed!", e);
            }

    }

    @Override
    @Async
    public void run(String... args) throws Exception {
        start();
    }

    class ShutdownThread extends Thread {
        @Override
        public void run() {
            close();
        }
    }

    public void close() {
        if (bootstrap == null) {
            log.info("WebSocket server is not running!");
            return;
        }

        log.info("WebSocket server is stopping");
        if (channelFuture != null) {
            channelFuture.channel().close().awaitUninterruptibly(10, TimeUnit.SECONDS);
            channelFuture = null;
        }
        if (bootstrap != null && bootstrap.config().group() != null) {
            bootstrap.config().group().shutdownGracefully();
        }
        if (bootstrap != null && bootstrap.config().childGroup() != null) {
            bootstrap.config().childGroup().shutdownGracefully();
        }
        bootstrap = null;

        log.info("WebSocket server stopped");
    }

    /**
     * 如果系统本身支持epoll同时用户自己的配置也允许epoll，会优先使用EpollEventLoopGroup
     *
     * @return
     */
    public ServerBootstrap newServerBootstrap() {
        //Epoll能够只查询发生事件的socket，而不用全部去遍历所有的socket哪个发生了时间，这大大提高了效率
        if (Epoll.isAvailable() && serverConfig.useEpoll) {
            EventLoopGroup bossGroup =
                    new EpollEventLoopGroup(serverConfig.bossThreads, new DefaultThreadFactory("WebSocketBossGroup", true));
            EventLoopGroup workerGroup =
                    new EpollEventLoopGroup(serverConfig.workerThreads, new DefaultThreadFactory("WebSocketWorkerGroup", true));
            return new ServerBootstrap().group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class);
        }
        //直接创建多路复用的Nio模型
        return newNioServerBootstrap(serverConfig.bossThreads, serverConfig.workerThreads);
    }

    private ServerBootstrap newNioServerBootstrap(int bossThreads, int workerThreads) {
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        if (bossThreads >= 0 && workerThreads >= 0) {
            bossGroup = new NioEventLoopGroup(bossThreads);
            workerGroup = new NioEventLoopGroup(workerThreads);
        } else {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
        }

        return new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
    }

}
