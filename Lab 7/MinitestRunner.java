import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {

    @Run
    public void testAddition() {
        System.out.println("testAddition executed");
    }

    @Run
    public void testSubtraction() {
        System.out.println("testSubtraction executed");
    }

    public void normalMethod() {
        System.out.println("Normal method executed");
    }

    @Run
    public void testMessage() {
        System.out.println("testMessage executed");
    }
}

public class MinitestRunner {

    public static void main(String[] args) {

        MyTests tests = new MyTests();

        Method[] methods = MyTests.class.getDeclaredMethods();

        int count = 0;

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)) {

                try {

                    method.invoke(tests);

                    count++;

                } catch (Exception e) {

                    System.out.println(
                        "Error running " + method.getName()
                    );
                }
            }
        }

        System.out.println();
        System.out.println("Total @Run methods executed: " + count);
    }
}