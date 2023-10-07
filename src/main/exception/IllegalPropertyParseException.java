package exception;

public class IllegalPropertyParseException extends RuntimeException {
    public IllegalPropertyParseException() {
        super("속성 파싱에 실패했습니다.");
    }
}
