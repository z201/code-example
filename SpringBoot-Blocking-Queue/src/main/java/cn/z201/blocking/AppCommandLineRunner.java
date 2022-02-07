package cn.z201.blocking;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Slf4j
@Lazy(false)
public class AppCommandLineRunner implements CommandLineRunner {

    private ArrayBlockingQueueManager arrayBlockingQueueManager;

    private LinkedBlockingDequeManager linkedBlockingDequeManager;

    private PriorityBlockingQueueManager priorityBlockingQueueManager;

    @Autowired
    public AppCommandLineRunner(ArrayBlockingQueueManager arrayBlockingQueueManager,
                                LinkedBlockingDequeManager linkedBlockingDequeManager,
                                PriorityBlockingQueueManager priorityBlockingQueueManager) {
        this.arrayBlockingQueueManager = arrayBlockingQueueManager;
        this.linkedBlockingDequeManager = linkedBlockingDequeManager;
        this.priorityBlockingQueueManager = priorityBlockingQueueManager;
    }

    @Override
    public void run(String... args) throws Exception {
        List<BlockingDto> blockingDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            blockingDtoList.add(new BlockingDto(RandomUtil.randomLong(Long.valueOf(i),Integer.MAX_VALUE)));
        }
        for (BlockingDto dto : blockingDtoList) {
            arrayBlockingQueueManager.addItem(dto);
            linkedBlockingDequeManager.addItem(dto);
            priorityBlockingQueueManager.addItem(dto);
        }
    }
}
