package cn.z201.example.learning.server.codec.handler;

import cn.z201.example.learning.protocol.MessageHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class ServerProtocolProcessHandler extends SimpleChannelInboundHandler<MessageHolder> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageHolder msg) throws Exception {
        log.info("running {}", msg);
        ctx.writeAndFlush(msg);
    }
}
