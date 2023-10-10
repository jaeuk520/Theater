package util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import exception.IllegalPropertyParseException;
import exception.PatternMismatchException;
import literal.LiteralRegex;

import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {

    private final Scanner scanner;

    /**
     * 커스텀 입력 클래스입니다. 주어진 입력 스트림에서 입력을 받아옵니다.
     * @param in 읽어올 입력 스트림입니다. 이는 파일 스트림이 될 수도 있고, 콘솔이라면 System.in을 사용할 수 있습니다.
     * @param delimiter 해당 입력 객체가 사용할 구분자입니다. 이는 LiteralRegex에 정의된 것을 사용하면 됩니다.
     */
    public Input(InputStream in, String delimiter) {
        this.scanner = new Scanner(in, "UTF-8");
        this.scanner.useDelimiter(delimiter);
    }

    public static Input getInstance(String path, String delimiter) {
        File file = new File(path);
        try {
            file.createNewFile();
            return new Input(new FileInputStream(path), delimiter);
        } catch (IOException e) {
            throw new RuntimeException("파일 검증에 실패했습니다.");
        }
    }

    /**
     * Scanner을 통해 pattern에 해당하는 정규표현식을 입력받습니다.<p>
     * 올바르게 입력되지 않은 경우 에러 메시지를 출력하며, 다시 입력받습니다.<p>
     * 입력받은 뒤, 입력 버퍼를 비웁니다.
     * @param pattern 입력받을 정규표현식
     * @return 정규표현식을 통해 입력받은 문자열
     */
    public String getByPattern(String pattern) {
        while (true){
            try {
                String ret = scanner.next(pattern);
                scanner.skip(LiteralRegex.FILE_DELIMITER);
                return ret.trim();
            } catch (InputMismatchException e) {
                scanner.next();
                return null;
            } catch (NoSuchElementException e) {
                throw new PatternMismatchException(pattern);
            }
        }
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public boolean hasNext(){
        return scanner.hasNextLine();
    }

    /**
     * 인풋 객체를 닫습니다. 대부분의 경우 종료할 때에 실행됩니다.
     * @return 정상 종료하는 경우 0, Scanner가 정상적으로 닫히지 않는 경우 1
     */
    public int close(){
        try{
            scanner.close();
            return 0;
        } catch (IllegalStateException e) {
            return 1;
        }
    }
}