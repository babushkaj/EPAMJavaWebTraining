package by.training.module3.annotation;

import java.lang.reflect.Field;

public class ValidAnnotationHandler {

    public boolean isValid(Object o) {

        Class<?> clazz = o.getClass();
        boolean skipValidation = skipValidation(clazz);

        if (skipValidation) {
            return false;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldObject;
            try {
                fieldObject = field.get(o);
            } catch (IllegalAccessException e) {
                throw new ValidAnnotationException(e);
            }
            boolean validAnnotationPresent = false;
            try {
                validAnnotationPresent = field.get(o).getClass().isAnnotationPresent(Valid.class);
            } catch (IllegalAccessException e) {
                throw new ValidAnnotationException(e);
            }
            if (validAnnotationPresent) {
                boolean valid = false;
                try {
                    ValidAnnotationHandler vah = new ValidAnnotationHandler();
                    valid = vah.isValid(field.get(o));
                } catch (IllegalAccessException e) {
                    throw new ValidAnnotationException(e);
                }
                if (!valid) {
                    return false;
                }
            }
            if (fieldObject instanceof Number) {
                if (field.isAnnotationPresent(ValidIntSize.class)) {
                    boolean valid = false;
                    try {
                        valid = ValidIntSizeAnnotationHandler.isValid(o, field);
                    } catch (IllegalAccessException e) {
                        throw new ValidAnnotationException(e);
                    }
                    if (!valid) {
                        return false;
                    }
                }
                if (field.isAnnotationPresent(ValidDoubleSize.class)) {
                    boolean valid = false;
                    try {
                        valid = ValidDoubleSizeAnnotationHandler.isValid(o, field);
                    } catch (IllegalAccessException e) {
                        throw new ValidAnnotationException(e);
                    }
                    if (!valid) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    private boolean skipValidation(Class clazz) throws ValidAnnotationException {
        return !clazz.isAnnotationPresent(Valid.class);
    }
}
