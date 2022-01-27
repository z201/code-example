package learning02.server.codec.handler;

import cn.z201.netty.learning02.common.MessageHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class ServerProtocolProcessHandler extends SimpleChannelInboundHandler<MessageHolder> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageHolder msg) throws Exception {
        log.info("running {}", msg);
        Long time = Clock.systemDefaultZone().millis();
        msg.getBody().setTime(time);
        msg.getBody().setSender("server");
        msg.getBody().setReceiver("client");
        msg.getBody().setContent("ok !");
        ctx.writeAndFlush(msg);
    }
}
