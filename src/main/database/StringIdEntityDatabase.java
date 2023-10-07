package database;

import exception.EntityInstantiateException;
import literal.IdStrategy;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Id를 String 형태로 가지는 EntityDatabase 클래스입니다. <p>
 * @param <E> 엔티티 클래스가 제너릭으로 작성됩니다
 */
public class StringIdEntityDatabase<E> extends EntityDatabase<E>{

    protected final int UUIDLength;
    public StringIdEntityDatabase(Class<E> entityType, int uuidLength, String path) {
        super(entityType, path);
        this.idStrategy = IdStrategy.UUID;
        this.UUIDLength = uuidLength;
    }

    @Override
    public boolean save(E entity) {
        String id;
        do {
            id = UUID.randomUUID().toString().substring(0, UUIDLength).toUpperCase();
        } while (data.containsKey(id));

        // Set ID of an entity using reflection API
        try {
            Field idField = entity.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new EntityInstantiateException();
        }

        data.put(id, entity);
        return true;
    }
}
