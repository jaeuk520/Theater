package database;

import java.util.HashMap;

public class EntityLoader<ID, E> {

    private String path;

    public EntityLoader(String path) {
        this.path = path;
    }

    /**
     * 데이터 파일로부터 HashMap으로 엔티티들을 불러옵니다. 이는 최초 한 번만 실행합니다.
     * @return 데이터 로드에 성공하거나 새로운 파일을 만드는 데 성공하면 true, 실패하면 false를 반환합니다.
     */
    public boolean load(HashMap<ID, E> data) {
        return true;
    }
}
