package database;

import literal.LiteralRegex;
import util.Input;

import java.util.HashMap;

public class EntityLoader<E> {

    private final String path;
    private final Class<E> type;
    private final Input input;

    public EntityLoader(String path, Class<E> type) {
        this.path = path;
        this.type = type;
        this.input = Input.getInstance(path, LiteralRegex.FILE_DELIMITER);
    }

    /**
     * 데이터 파일로부터 HashMap으로 엔티티들을 불러옵니다. 이는 최초 한 번만 실행합니다.
     */
    public void load(HashMap<String, E> data) {
        while (input.hasNext()) {
            String[] attr = input.readLine().split("\\$");
            data.put(attr[0], EntityFactory.createEntity(type, attr));
        }
    }
}
