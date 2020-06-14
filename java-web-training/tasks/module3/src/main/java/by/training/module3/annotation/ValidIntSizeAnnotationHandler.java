package by.training.module3.annotation;

import java.lang.reflect.Field;

public class ValidIntSizeAnnotationHandler {
    public static boolean isValid(Object o, Field field) throws IllegalAccessException {

        ValidIntSize annotation = field.getAnnotation(ValidIntSize.class);
        int value = (int) field.get(o);

        if (value > annotation.maxSize()) {
            return false;
        }

        if (value < annotation.minSize()) {
            return false;
        }

        return true;
    }
}
