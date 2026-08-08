import java.util.regex.Pattern;

public class PasswordChecker {

    public static boolean hasLength(String pw) {
        return pw.length() >= 8;
    }

    public static boolean hasUppercase(String pw) {
        return Pattern.compile("[A-Z]").matcher(pw).find();
    }

    public static boolean hasDigit(String pw) {
        return Pattern.compile("[0-9]").matcher(pw).find();
    }

    public static boolean hasSpecial(String pw) {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(pw).find();
    }

    public static String strength(String pw) {

        int count = 0;

        if (hasLength(pw))
            count++;

        if (hasUppercase(pw))
            count++;

        if (hasDigit(pw))
            count++;

        if (hasSpecial(pw))
            count++;

        if (count <= 1)
            return "Weak";
        else if (count <= 3)
            return "Medium";
        else
            return "Strong";
    }
}