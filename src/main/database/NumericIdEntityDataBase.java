package database;

/**
 * Id를 Long(수) 형태로 가지는 EntityDatabase 클래스입니다. id는 1부터 시작합니다.<p>
 * @param <E> 엔티티 클래스가 제너릭으로 작성됩니다
 */
public class NumericIdEntityDatabase<E> extends EntityDatabase<E>{

    private long lastId = 0;

    public NumericIdEntityDatabase(Class<E> entityType, int idStrategy, String path) {
        super(entityType, idStrategy, path);
    }

    @Override
    public boolean save(E entity) {
        data.put(String.valueOf(++lastId), entity);
        return true;
    }

}
