package database;

import entity.Entity;
import exception.EntityInstantiateException;
import exception.IllegalPropertyParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class EntityPropertyTypeResolver {
    public static <T> T resolve(Class<?> entityType, String data) {
        Method method = EntityParser.map.get(entityType);

        if (Entity.class.isAssignableFrom(entityType)) {
            // Subclass, need to fetch from hashmap
            // data is an ID of it
            HashMap<String, ?> database = DatabaseContext.getDatabase(entityType);
            if((T) database.get(data) == null) throw new EntityInstantiateException();
            return (T) database.get(data);
        }
        try {
            return (T) method.invoke(null, data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalPropertyParseException();
        }
    }
}