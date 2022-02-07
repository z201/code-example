package cn.z201.blocking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author z201.coding@gmail.com
 **/
@Lazy(false)
@Component
@Slf4j
public class PriorityBlockingQueueManager {

    private final static BlockingQueue<BlockingDto> blockingQueue = new PriorityBlockingQueue<>();

    @PostConstruct
    public void setup() {
        new Thread(() -> {
            BlockingDto blockingDto = null;
            for (; ; ) {
                try {
                    blockingDto = blockingQueue.take();
                    log.info("PriorityBlockingQueueManager tate {} ", blockingDto.getId());
                } catch (Exception e) {
                    log.error("执行队列_异常:" + e);
                }
            }
        }).start();
    }

    public void addItem(BlockingDto blockingDto) {
        if (!blockingQueue.contains(blockingDto)) {
            blockingQueue.add(blockingDto);
        }
    }
}
