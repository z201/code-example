package cn.z201.example.learning.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {
    public ServerIdleCheckHandler() {
        /**
         *
         * readerIdleTimeSeconds -一个IdleStateEvent其状态是IdleState.READER_IDLE时的指定时间段没有执行读操作将被触发。 指定0以禁用。
         * writerIdleTimeSeconds -一个IdleStateEvent其状态是IdleState.WRITER_IDLE时的指定时间段没有执行写操作将被触发。 指定0以禁用。
         * allIdleTimeSeconds -一个IdleStateEvent其状态是IdleState.ALL_IDLE时的指定时间段进行读取和写入都将被触发。 指定0以禁用。
         */
        super(45, 0, 0, TimeUnit.SECONDS);
    }
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info("idle check happen");
//        if (evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
//            log.info("idle check happen, so close the connection");
//            ctx.close();
//            return;
//        }
        super.channelIdle(ctx, evt);
    }
}
