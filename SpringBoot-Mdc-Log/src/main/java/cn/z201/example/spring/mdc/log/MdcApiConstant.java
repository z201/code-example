package cn.z201.example.spring.mdc.log;

/**
 * @author z201.coding@gmail.com
 **/
public interface MdcApiConstant {

    String X_REAL_IP = "x-real-ip";

    /**
     * 请求头跟踪id名。
     */
    String HTTP_HEADER_TRACE_ID = "AppTraceId";

    /**
     * 请求头
     */
    String HTTP_TOKEN_HEADER = "Authorization";

    /**
     * app租户
     */
    String APP_TENANT = "Tenant";

}
