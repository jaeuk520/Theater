package entity;

import database.DatabaseContext;
import exception.EntityInstantiateException;
import java.util.HashMap;

/**
 * 이 프로그램에서 저장될 수 있는 자료형 (엔티티)에 대한 기본 구조입니다.
 *
 * @param <ID>
 */
public abstract class Entity<ID> implements EntityValidator {
    protected final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    @Override
    public void validate() {
        // 기본 Entity 검증: ID가 같은 게 있는 경우에는 예외를 발생한다
        HashMap<String, ?> database = DatabaseContext.getDatabase(this.getClass());
        if (database.containsKey(id)) {
            throw new EntityInstantiateException();
        }
    }
}
