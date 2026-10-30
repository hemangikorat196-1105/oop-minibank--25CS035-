class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainers {

    // Generic method to return first element of any array
    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        // Box with Integer
        Box<Integer> intBox = new Box<>();
        intBox.set(100);

        System.out.println("Integer Box: " + intBox.get());

        // Box with String
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello Java");

        System.out.println("String Box: " + stringBox.get());

        // Pair with String and Integer
        Pair<String, Integer> student =new Pair<>("Hemangi", 20);

        System.out.println("Name: " + student.getFirst());
        System.out.println("Age: " + student.getSecond());

        // Pair with Integer and Double
        Pair<Integer, Double> marks =new Pair<>(101, 89.5);

        System.out.println("Roll No: " + marks.getFirst());
        System.out.println("Marks: " + marks.getSecond());

        // Generic method with String array
        String[] names = {"Hemangi", "Rahul", "Priya"};

        System.out.println("First name: "
                + firstElement(names));

        // Generic method with Integer array
        Integer[] numbers = {10, 20, 30};

        System.out.println("First number: "
                + firstElement(numbers));
    }
}