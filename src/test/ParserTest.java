import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import database.EntityLoader;
import database.EntityParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class ParserTest {
    @Test
    @DisplayName("Parser Test")
    public void ParserTest() throws IOException {
        Map<Class<?>, Method> map = EntityParser.map;
        System.out.println(map.keySet().size());
        for(Class<?> clazz : map.keySet()) {
            System.out.println(clazz.getName());
        }

        EntityLoader<TestSimpleEntity> entityLoader = new EntityLoader<>("test_parser.txt", TestSimpleEntity.class);
        HashMap<String, TestSimpleEntity> entityMap = new HashMap<>();
        entityLoader.load(entityMap);

        assertThat(entityMap).hasSize(2);
        assertThat(entityMap).containsKey("ABCDEF");
        assertThat(entityMap.get("ABCDEF").getNumber()).isEqualTo(100);
    }

    @BeforeEach
    void setUp() {
        try {
            final File file1 = new File("test_parser.txt");
            file1.createNewFile();
            FileWriter fileWriter = new FileWriter(file1, false);
            fileWriter.write("ABCDEF$100\nABCDEE$120\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
