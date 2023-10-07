package database;

import java.util.HashMap;
import java.util.Map;

public class DatabaseContext {
    static Map<Class<?>, HashMap<String, ?>> mapTable = new HashMap<>();

    public static Map<Class<?>, HashMap<String, ?>> getMapTable() {
        return mapTable;
    }

    public static <E> void putDatabase(Class<?> clazz, HashMap<String, E> database) {
        mapTable.put(clazz, database);
    }

    public static HashMap<String, ?> getDatabase(Class<?> clazz) {
        return mapTable.get(clazz);
    }
}
