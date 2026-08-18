public class Driver3 
{
    public static void main(String[] args) 
    {

        String[] passwords = 
        {
            "abc",
            "Password",
            "Pass123",
            "Abcd1234!",
            "Hello@123"
        };

        for (String pw : passwords) 
        {

            System.out.println("Password: " + pw);

            System.out.println("Length >= 8 : " + PasswordChecker.hasLength(pw));

            System.out.println("Contains Uppercase : " +PasswordChecker.hasUppercase(pw));

            System.out.println("Contains Digit : " +PasswordChecker.hasDigit(pw));

            System.out.println("Contains Special Character : " +PasswordChecker.hasSpecial(pw));

            System.out.println("Strength : " +PasswordChecker.strength(pw));

            System.out.println("----------------------------");
        }
    }
}