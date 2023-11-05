package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class EntityWriter<E> {
    private final String path;

    public EntityWriter(String path) {
        this.path = path;
    }

    /**
     * 파일에 엔티티 하나를 씁니다.
     *
     * @param entity
     * @return 쓰기에 성공하면 true, 실패하면 false를 반환합니다.
     */
    public boolean write(E entity) {
        try {
            File f = new File(path);
            f.createNewFile();
            FileWriter fileWriter = new FileWriter(f, true);
            fileWriter.append(entity.toString());
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 주어진 data로 파일을 모두 덮어씁니다. 주로 프로그램을 종료하거나, 특수하게 모든 텍스트 파일을 덮어써야 하는 경우 사용합니다.
     *
     * @return 쓰기에 성공하면 true, 실패하면 false를 반환합니다.
     */
    public boolean dump(HashMap<String, E> data) {
        try {
            File f = new File(path);
            f.createNewFile();
            OutputStreamWriter fileWriter = new OutputStreamWriter(Files.newOutputStream(f.toPath()), StandardCharsets.UTF_8);
            for (Object obj : data.values()) {
                fileWriter.append(obj.toString());
            }
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
