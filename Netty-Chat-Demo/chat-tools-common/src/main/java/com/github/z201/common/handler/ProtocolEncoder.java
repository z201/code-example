package com.github.z201.common.handler;

import com.github.z201.common.protocol.MessageHolder;
import com.github.z201.common.protocol.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码Handler.
 *
 *                                       Jelly Protocol
 *  __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __
 * |           |           |           |           |              |                          |
 *       2           1           1           1            4               Uncertainty
 * |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __|
 * |           |           |           |           |              |                          |
 *     Magic        Sign        Type       Status     Body Length         Body Content
 * |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __|
 *
 * 协议头9个字节定长
 *     Magic      // 数据包的验证位，short类型
 *     Sign       // 消息标志，请求／响应／通知，byte类型
 *     Type       // 消息类型，登录／发送消息等，byte类型
 *     Status     // 响应状态，成功／失败，byte类型
 *     BodyLength // 协议体长度，int类型
 *
 *
 * @author z201.coding@gmail.com.
 */
public class ProtocolEncoder extends MessageToByteEncoder<MessageHolder> {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageHolder msg, ByteBuf out) throws Exception {
        String body = msg.getBody();
        if (body == null) {
            throw new RuntimeException("body == null");
        }
        // 编码
        byte[] bytes = body.getBytes("utf-8");
        if (logger.isDebugEnabled()) {
            logger.debug("bytes length  {} " , bytes.length);
            logger.debug("body {}" ,new String(bytes, "utf-8"));
        }
        out.writeShort(ProtocolHeader.MAGIC)
                .writeByte(msg.getSign())
                .writeByte(msg.getType())
                .writeByte(msg.getStatus())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
