package com.zhukai.print.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * netty��ļ���http����
 *
 * @author zhukai
 * @date 2019/1/25
 */
@Slf4j
public class HttpServer {

    public void bind(int port) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();     // bossGroup����parentGroup���Ǹ�����TCP/IP���ӵ�
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // workerGroup����childGroup,�Ǹ�����Channel(ͨ��)��I/O�¼�

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 16)  // ��ʼ������˿����Ӷ���,ָ���˶��еĴ�С16
                    .childOption(ChannelOption.SO_KEEPALIVE, true)      // ���ֳ�����
                    .childHandler(new ChannelInitializer<SocketChannel>() {   // �󶨿ͻ�������ʱ�򴥷�����
                        @Override
                        protected void initChannel(SocketChannel sh) throws Exception {
                            ChannelPipeline pipeline = sh.pipeline();
                            pipeline.addLast(new HttpServerCodec());    // http �����
                            pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024)); // http ��Ϣ�ۺ���                                                                     512*1024Ϊ���յ����contentlength
                            pipeline.addLast(new ServerHandler());      // ��������
                        }
                    });
            // �󶨼����˿ڣ�����syncͬ�����������ȴ��󶨲�����
            ChannelFuture future = sb.bind(port).sync();

            if (future.isSuccess()) {
                // �ɹ��󶨵��˿�֮��,��channel����һ���ܵ��رյļ�������ͬ������,ֱ��channel�ر�,�̲߳Ż�����ִ��,�������̡�
                log.info("����������ɹ�! {}", "http://localhost:" + port);
                future.channel().closeFuture().sync();
            } else {
                log.error("���������ʧ��", future.cause());
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            // �ر��߳���
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
