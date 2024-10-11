package org.tyaa.training.current.server.test.system.utils;

/*import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;*/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Составные действия с использованием отражений классов
 * */
public class ReflectionActions {

    public static Set<Class> getAllClassesFromPackage(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    public static <T> Object extractField(T t, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = t.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(t);
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // TODO handle the exception
        }
        return null;
    }
}
