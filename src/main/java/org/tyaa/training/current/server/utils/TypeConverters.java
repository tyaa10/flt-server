package org.tyaa.training.current.server.utils;

public class TypeConverters {

    public static Integer booleanToInteger(Boolean booleanObject) {
        return booleanObject.compareTo(false);
    }
}
