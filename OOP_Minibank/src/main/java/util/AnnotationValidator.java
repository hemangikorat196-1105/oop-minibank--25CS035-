package util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import model.annotation.MaxLength;
import model.annotation.Positive;

/** Reads validation annotations at runtime and returns every validation message. */
public final class AnnotationValidator {
    private AnnotationValidator() { }
    public static String[] validate(Object obj) {
        if (obj == null) return new String[]{"Object must not be null."};
        ArrayList<String> errors = new ArrayList<>();
        try {
            for (Class<?> type = obj.getClass(); type != null; type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (field.isAnnotationPresent(Positive.class) && value instanceof Number number && number.longValue() <= 0) {
                        errors.add(field.getName() + " " + field.getAnnotation(Positive.class).message());
                    }
                    if (field.isAnnotationPresent(MaxLength.class) && value instanceof String string
                            && string.length() > field.getAnnotation(MaxLength.class).value()) {
                        errors.add(field.getName() + " must be at most " + field.getAnnotation(MaxLength.class).value() + " characters");
                    }
                }
            }
        } catch (ReflectiveOperationException e) { throw new IllegalStateException("Could not inspect annotations", e); }
        return errors.toArray(String[]::new);
    }
    public static boolean isValid(Object obj) { return validate(obj).length == 0; }
}
