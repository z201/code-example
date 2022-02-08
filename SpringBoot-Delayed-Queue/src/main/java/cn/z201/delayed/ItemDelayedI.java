package cn.z201.delayed;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 **/
@Setter
@Getter
public class ItemDelayedI<T> implements Delayed {

    /**
     * 数据id
     */
    private Long dataId;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 到期时间
     */
    private long expire;
    /**
     * 泛型data
     */
    private T data;

    private ItemDelayedI() {

    }

    public ItemDelayedI(Long dataId, long startTime, long expire) {
        super();
        this.dataId = dataId;
        this.startTime = startTime;
        this.expire = expire;
    }

    @Override
    public int compareTo(Delayed o) {
        // 入队时需要判断任务放到队列的哪个位置，过期时间短的放在前面
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // 和当前时间比较，判断是否过期。
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    // 重写 equals 和 hashcode方法用id作为唯一标志，添加列队的时候做防重复判断。
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemDelayedI<?> that = (ItemDelayedI<?>) o;

        return dataId.equals(that.dataId);
    }

    @Override
    public int hashCode() {
        return dataId.hashCode();
    }
}
