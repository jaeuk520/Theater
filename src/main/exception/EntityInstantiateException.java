package exception;

public class EntityInstantiateException extends RuntimeException {
    public EntityInstantiateException() {
        super("객체 생성 중 오류가 발생했습니다.");
    }
}
