package exception;

public class PatternMismatchException extends RuntimeException {
    public PatternMismatchException(String pattern) {
        super(String.format("입력 패턴 %s에 일치하지 않습니다.", pattern));
    }
}
