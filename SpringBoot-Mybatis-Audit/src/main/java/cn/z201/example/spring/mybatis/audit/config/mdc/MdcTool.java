package cn.z201.example.spring.mybatis.audit.config.mdc;

import org.slf4j.MDC;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author z201.coding@gmail.com
 **/
public class MdcTool {

    private static class SingletonHolder {

        private static final MdcTool INSTANCE = new MdcTool();

    }

    private MdcTool() {
    }

    public static MdcTool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static synchronized String currentTraceId() {
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

    public String getAndCreate() {
        Boolean hasMdc = MdcTool.getInstance().has();
        if (!hasMdc) {
            MdcTool.getInstance().put();
        }
        return MdcTool.getInstance().get();
    }

    public void put() {
        put(currentTraceId());
    }

    public void put(String traceId) {
        MDC.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, traceId);
    }

    public String get() {
        return MDC.get(MdcApiConstant.HTTP_HEADER_TRACE_ID);
    }

    public void remote() {
        MDC.clear();
    }

    public Boolean has() {
        if (Objects.isNull(MDC.get(MdcApiConstant.HTTP_HEADER_TRACE_ID))) {
            return false;
        }
        return true;
    }

}
