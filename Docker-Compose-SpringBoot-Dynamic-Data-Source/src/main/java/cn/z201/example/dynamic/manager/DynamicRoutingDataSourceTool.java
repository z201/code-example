package cn.z201.example.dynamic.manager;

/**
 * @author z201.coding@gmail.com
 **/
public class DynamicRoutingDataSourceTool {

    private static final String URL = "jdbc:mysql://%s/%s";

    private static final String URL_ADDRESS = "127.0.0.1:3306";

    private static final String URL_PARAMETER = "?useSSL=false&useUnicode=true&autoReconnect=true&failOverReadOnly=false&characterEncoding=utf-8";

    public static String buildDataBase(String dataBase) {
        return String.format(URL, URL_ADDRESS, dataBase) + URL_PARAMETER;
    }

    public static String buildUrl(String address, String dataBase) {
        return String.format(URL, address, dataBase) + URL_PARAMETER;
    }

}
