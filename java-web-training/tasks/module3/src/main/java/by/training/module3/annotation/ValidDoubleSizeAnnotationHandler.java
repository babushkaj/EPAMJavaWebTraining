package by.training.module3.annotation;

import java.lang.reflect.Field;

public class ValidDoubleSizeAnnotationHandler {
    public static boolean isValid(Object o, Field field) throws IllegalAccessException {
        ValidDoubleSize annotation = field.getAnnotation(ValidDoubleSize.class);
        double value = (double) field.get(o);

        if (value > annotation.maxSize()) {
            return false;
        }

        if (value < annotation.minSize()) {
            return false;
        }

        return true;
    }
}
