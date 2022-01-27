package learning02.common;

import io.netty.buffer.ByteBuf;
import lombok.*;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageBody
{
    private String msgId;
    private String sender;
    private String receiver;
    private String content;
    private Long time;

}
