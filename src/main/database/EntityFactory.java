package database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

// 파일에서 읽어온 Entity를 객체로 변환해야 함
// EntityParser는 적절히 파싱할 수 있는데, 얘를 어떻게 constructor에 넣지?
public class EntityFactory {
    public static<E> E createEntity(Class<E> entityClass, Object... args) {
        try {
            Constructor<?> constructor = entityClass.getDeclaredConstructors()[0];
            Parameter[] parameters = constructor.getParameters();
            Object[] typedArguments = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                typedArguments[i] = EntityParser.resolve(parameters[i].getType(), (String) args[i]);
            }
            return (E) constructor.newInstance(typedArguments);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("객체 생성에 실패했습니다.");
        }
    }
}
