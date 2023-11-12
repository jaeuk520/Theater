package database;

import exception.EntityInstantiateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class EntityFactory {
    public static <E> E createEntity(Class<E> entityClass, Object... args) {
        try {
            Constructor<?> constructor = entityClass.getDeclaredConstructors()[0];
            Parameter[] parameters = constructor.getParameters();
            Object[] typedArguments = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                typedArguments[i] = EntityPropertyTypeResolver.resolve(parameters[i].getType(), (String) args[i]);
            }
            return (E) constructor.newInstance(typedArguments);
        } catch (Exception e) {
            throw new EntityInstantiateException();
        }
    }
}
