package database;

import entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EntityParser {

    public static Map<Class<?>, Method> map = new HashMap<>();

    static {
        for(Method m : EntityParser.class.getDeclaredMethods()) {
            if (m.getName().startsWith("parse"))
                map.put(m.getReturnType(), m);
        }
    }

    public static <T> T resolve(Class<?> entityType, String data) {
        Method method = map.get(entityType);

        if (Entity.class.isAssignableFrom(entityType)) {
            // Subclass, need to fetch from hashmap
            // data is an ID of it
            HashMap<String, ?> database = DatabaseContext.getDatabase(entityType);
            return (T) database.get(data);
        }
        try {
            return (T) method.invoke(null, data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("속성 파싱에 실패했습니다.");
        }
    }

    public static int parseInt(String data) {
        return Integer.parseInt(data);
    }

    public static LocalDate parseDate(String data) {
        return LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalTime parseTime(String data) {
        return LocalTime.parse(data, DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String parseString(String data) {
        return data;
    }
}
