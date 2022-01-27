package learning02.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
public class ClientFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ClientFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2,0,2);
    }
}
