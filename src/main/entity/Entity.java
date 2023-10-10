package entity;

/**
 * 이 프로그램에서 저장될 수 있는 자료형 (엔티티)에 대한 기본 구조입니다.
 * @param <ID>
 */
public abstract class Entity<ID> {

    protected final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }
}