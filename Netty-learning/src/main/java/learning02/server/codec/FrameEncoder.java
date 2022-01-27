package learning02.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class FrameEncoder extends LengthFieldPrepender {

    public FrameEncoder() {
        super(2);
        log.info("running");
    }
}
