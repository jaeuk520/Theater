package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 메모리 Database를 구현할 때 사용하는 추상 클래스입니다. <p>
 * HashMap을 사용하며, idStrategy는 IdStrategy.UUID 또는 IdStrategy.NUMBER 중 하나입니다. <p>
 * UUID의 경우 길이 UUIDLength의 랜덤한 문자열이 ID로 들어가고, NUMBER의 경우 lastId를 만들고, lastId를 1 증가시킵니다.
 */
public abstract class EntityDatabase<ID, E> {

    protected final HashMap<ID, E> data;
    protected final int idStrategy;
    private final EntityLoader<ID, E> entityLoader;
    private final EntityWriter<ID, E> entityWriter;

    public EntityDatabase(int idStrategy, String path) {
        this.data = new HashMap<>();
        this.entityLoader = new EntityLoader<>(path);
        this.entityWriter = new EntityWriter<>(path);
        this.idStrategy = idStrategy;
        this.entityLoader.load(this.data);
    }

    /**
     * 메모리 데이터베이스에 entity를 저장합니다.
     * @param entity 저장할 엔티티입니다.
     * @return 저장에 성공했다면 true를, 실패했다면 false를 리턴합니다.
     */
    public abstract boolean save(E entity);

    public boolean save(E entity, ID id) {
        if (data.containsKey(id)) return false;
        data.put(id, entity);
        return true;
    }

    public boolean delete(ID id) {
        if (!data.containsKey(id)) return false;
        data.remove(id);
        return true;
    }

    public Optional<E> findById(ID id) {
        if (!data.containsKey(id)) return Optional.empty();
        return Optional.of(data.get(id));
    }

    /**
     * HashMap에 저장된 엔티티를 파일로 내보냅니다. 이 과정은 성능을 생각하지 않았습니다.
     */
    public void flush() {
        entityWriter.dump(data);
    }

    public int size() {
        return data.size();
    }

    public List<E> findAll() {
        return new ArrayList<>(data.values());
    }

}
