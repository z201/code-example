package cn.z201.mdc.log;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author z201.coding@gmail.com
 **/
public class MdcTool {

    private static class SingletonHolder {
        private static final MdcTool INSTANCE = new MdcTool();
    }
    private MdcTool (){}

    public static final MdcTool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized String currentTraceId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        UUID uuid = new UUID(random.nextInt(), random.nextInt());
        StringBuilder st = new StringBuilder(uuid.toString().replace("-", "").toLowerCase());
        st.append(Instant.now().toEpochMilli());
        int i = 0;
        while (i < 3) {
            i++;
            st.append(ThreadLocalRandom.current().nextInt(2));
        }
        return st.toString();
    }
}
