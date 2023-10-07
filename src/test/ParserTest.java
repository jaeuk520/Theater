import database.EntityLoader;
import database.EntityParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ParserTest {
    @Test
    @DisplayName("Parser Test")
    public void ParserTest(){
        Map<Class<?>, Method> map = EntityParser.map;
        System.out.println(map.keySet().size());
        for(Class<?> clazz : map.keySet()) {
            System.out.println(clazz.getName());
        }

        EntityLoader<TestSimpleEntity> entityLoader = new EntityLoader<>("test1.txt", TestSimpleEntity.class);
        HashMap<String, TestSimpleEntity> entityMap = new HashMap<>();
        entityLoader.load(entityMap);

        Assertions.assertThat(entityMap).hasSize(2);
        Assertions.assertThat(entityMap).containsKey("ABCDEF");
        Assertions.assertThat(entityMap.get("ABCDEF").getNumber()).isEqualTo(100);
    }
}
