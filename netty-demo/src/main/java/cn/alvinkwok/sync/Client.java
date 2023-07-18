package cn.alvinkwok.sync;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
            .channel(NioServerSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            try {
                                DefaultFuture.receive(ctx.channel(), msg);
                            } finally {
                                ReferenceCountUtil.release(msg);
                            }
                        }
                    });
                }
            });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8000).sync();

        DefaultFuture requestFuture = new DefaultFuture(future.channel(), 10000);
        future.channel().writeAndFlush("123");
        requestFuture.get();
        future.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
