package database;

import exception.EntityInstantiateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;

// 파일에서 읽어온 Entity를 객체로 변환해야 함
// EntityParser는 적절히 파싱할 수 있는데, 얘를 어떻게 constructor에 넣지?
public class EntityFactory {
    public static<E> E createEntity(Class<E> entityClass, HashMap<String, E> data, Object... args) {
        try {
            Constructor<?> constructor = entityClass.getDeclaredConstructors()[0];
            Parameter[] parameters = constructor.getParameters();
            Object[] typedArguments = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                typedArguments[i] = EntityParser.resolve(parameters[i].getType(), (String) args[i]);
            }
            //의미 규칙 추가: 영화 id, 영화 제목 중복 허용 X
            if(entityClass.getName().equals("entity.Movie")) {
                validateMovieDuplication(data, typedArguments[0].toString(), typedArguments[1].toString());
            }
            return (E) constructor.newInstance(typedArguments);
        } catch (Exception e) {
            throw new EntityInstantiateException();
        }
    }

    private static <E> void validateMovieDuplication(HashMap<String,E> data, String movieId, String movieName) {
        data.forEach((key, value) -> {
            if(movieId.equals(key) || movieName.equals((value.toString().split("\\$"))[2].replaceAll("\n", ""))) {
                throw new EntityInstantiateException();
            }
        });
    }
}
