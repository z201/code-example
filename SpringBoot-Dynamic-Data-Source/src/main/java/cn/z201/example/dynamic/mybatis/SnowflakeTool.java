package cn.z201.example.dynamic.mybatis;

import java.time.Clock;

/**
 * @author z201.coding@gmail.com
 * @date 2020/11/03
 **/
public class SnowflakeTool {

    /**
     * 开始时间截，这里用系统的时间戳
     */
    private final long startTime = 1420041600000L;

    /**
     * 机器id所占的位数 5 位
     */
    private final static long WORKER_ID_BITS = 5L;
    /**
     * 序列在id中占的位数
     */
    private final static long SEQUENCE_BITS = 12L;
    /**
     * 数据标识id所占的位数
     */
    private final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 数据中心最大数量。支持的最大机器id，结果是31 (这个移位算法可以很快计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << WORKER_ID_BITS); //

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = SEQUENCE_BITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 禁止参的构造，规避创建对象时候使用默认的机器码
     */
    private SnowflakeTool() {
    }

    private static class SingletonHolder {
        private static final SnowflakeTool INSTANCE = new SnowflakeTool(0, 0);
    }

    public static final SnowflakeTool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    private SnowflakeTool(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = Clock.systemDefaultZone().millis();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出,序列数已经达到最大
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTime) << timestampLeftShift) // 时间戳部分
                | (datacenterId << datacenterIdShift) // 数据中心部分
                | (workerId << workerIdShift) // 机器标识符部分
                | sequence; // 序列号部分
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = Clock.systemDefaultZone().millis();
        while (timestamp <= lastTimestamp) {
            timestamp = Clock.systemDefaultZone().millis();
        }
        return timestamp;
    }


}
