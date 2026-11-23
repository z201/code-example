package cn.z201.example.netty.learning.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
@Slf4j
public class FrameDecoder extends LengthFieldBasedFrameDecoder {

    public FrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
        log.info("running");
    }

}
