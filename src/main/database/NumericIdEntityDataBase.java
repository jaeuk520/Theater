package database;

/**
 * Id를 Long(수) 형태로 가지는 EntityDatabase 클래스입니다. id는 1부터 시작합니다.<p>
 * @param <E> 엔티티 클래스가 제너릭으로 작성됩니다
 */
public class NumericIdEntityDataBase<E> extends EntityDatabase<Long, E>{

    private long lastId = 0;

    public NumericIdEntityDataBase(int idStrategy, String path) {
        super(idStrategy, path);
    }

    @Override
    public boolean save(E entity) {
        data.put(++lastId, entity);
        return true;
    }
}
