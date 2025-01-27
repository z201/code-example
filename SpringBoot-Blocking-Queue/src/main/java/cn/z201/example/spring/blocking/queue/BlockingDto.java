package cn.z201.example.blocking.queue;

import lombok.Getter;
import lombok.Setter;

/**
 * @author z201.coding@gmail.com
 **/

@Getter
@Setter
public class BlockingDto implements Comparable<BlockingDto> {

    private Long id;

    public BlockingDto(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(BlockingDto o) {
        if (this.id > o.getId()) {
            return 1;
        }
        return -1;
    }

}
