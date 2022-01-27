package learning02.server.codec;

import cn.z201.netty.learning02.common.MessageHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class ProtocolEncoder extends MessageToMessageEncoder<MessageHolder> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageHolder msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        msg.encode(byteBuf);
        log.info("running {}",msg.toString());
        out.add(byteBuf);
    }
}
