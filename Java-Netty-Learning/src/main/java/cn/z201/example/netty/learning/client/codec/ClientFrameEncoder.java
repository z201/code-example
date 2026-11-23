package cn.z201.example.netty.learning.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
public class ClientFrameEncoder extends LengthFieldPrepender {

    public ClientFrameEncoder() {
        super(2);
    }

}
