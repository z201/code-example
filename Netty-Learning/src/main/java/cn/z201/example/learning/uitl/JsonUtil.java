package cn.z201.example.learning.uitl;

import com.google.gson.Gson;

/**
 * @author z201.coding@gmail.com
 * @date 11/21/21
 **/
public class JsonUtil {

    private static final Gson GSON = new Gson();

    private JsonUtil() {

    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return GSON.fromJson(jsonString,clazz);
    }

    public static String toJson(Object object){
        return GSON.toJson(object);
    }

}
