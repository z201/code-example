package cn.z201.example.learning.client.codec;

import cn.z201.example.learning.protocol.MessageHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class ClientProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf msg, List<Object> out) throws Exception {
        MessageHolder messageHolder = new MessageHolder();
        // 这里需要做转换
        log.info("{}",messageHolder.toString());
        out.add(messageHolder);
    }
}
