package xyz.jdynb.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, Object> pojo2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException ignored) {
        }
        return map;
    }

}
