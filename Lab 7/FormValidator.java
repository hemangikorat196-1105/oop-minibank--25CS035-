import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {

    @NotBlank
    @MaxLength(20)
    String name;

    @NotBlank
    @MaxLength(30)
    String email;

    @NotBlank
    @MaxLength(10)
    String password;

    SignupForm(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

public class FormValidator {

    public static List<String> validate(Object obj) {

        List<String> errors = new ArrayList<>();

        Class<?> cls = obj.getClass();

        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            try {

                Object value = field.get(obj);

                // Check @NotBlank
                if (field.isAnnotationPresent(NotBlank.class)) {

                    if (value == null ||
                        value.toString().trim().isEmpty()) {

                        errors.add(
                            field.getName() + " must not be blank"
                        );
                    }
                }

                // Check @MaxLength
                if (field.isAnnotationPresent(MaxLength.class)) {

                    MaxLength annotation =
                        field.getAnnotation(MaxLength.class);

                    int max = annotation.value();

                    if (value != null &&
                        value.toString().length() > max) {

                        errors.add(
                            field.getName()
                            + " must have maximum "
                            + max
                            + " characters"
                        );
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Cannot access " + field.getName());
            }
        }

        return errors;
    }

    public static void main(String[] args) {

        SignupForm form = new SignupForm(
            "",
            "verylongemailaddress@example.com",
            "123456789012345"
        );

        List<String> errors = validate(form);

        if (errors.isEmpty()) {

            System.out.println("Form is valid.");

        } else {

            System.out.println("Validation Errors:");

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}