package cn.z201.example.learning.server.codec;

import cn.z201.example.learning.protocol.MessageHolder;
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
        log.info("running {}",msg.toString());
        out.add(byteBuf);
    }
}
