package learning02.common;

import cn.z201.netty.learning02.uitl.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 消息载体.
 * <p>
 * 传输模块与服务模块之间双向数据传输载体:
 * <p>
 * MessageHolder
 * Service Module <----------------> Transport Module
 *
 * @author z201.coding@gmail.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageHolder {

    /**
     * 消息标志
     */
    private byte sign;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息状态
     */
    private byte status;
    /**
     * 消息体
     */
    private MessageBody body;

    public void decode(ByteBuf msg) {
        this.sign = msg.readByte();
        this.type = msg.readByte();
        this.status = msg.readByte();
        this.body = JsonUtil.fromJson(msg.toString(StandardCharsets.UTF_8), MessageBody.class);
    }

    public void encode(ByteBuf msg) {
        msg.writeByte(sign);
        msg.writeByte(type);
        msg.writeByte(status);
        msg.writeBytes(JsonUtil.toJson(body).getBytes());
    }
}
