
package util;

import model.annotation.Positive;
import model.annotation.MaxLength;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        Class<?> current = obj.getClass();

        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive rule = field.getAnnotation(Positive.class);

                        if (value instanceof Number &&
                            ((Number) value).doubleValue() <= 0) {
                            errors.add(field.getName() + " " + rule.message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength rule = field.getAnnotation(MaxLength.class);

                        if (value instanceof String &&
                            ((String) value).length() > rule.value()) {
                            errors.add(field.getName()
                                + " length must be <= " + rule.value());
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add("Could not validate " + field.getName());
                }
            }

            current = current.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
