
package model;

public class GenericsDemo {

    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0;
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {

        Box<Integer> numberBox = new Box<>();
        numberBox.set(100);
        System.out.println("Box value: " + numberBox.get());

        Box<String> textBox = new Box<>();
        textBox.set("MiniBank");
        System.out.println("Text box: " + textBox.get());

        Pair<String, Integer> pair = new Pair<>("Rahul", 101);
        System.out.println("First: " + pair.getFirst());
        System.out.println("Second: " + pair.getSecond());

        Integer[] numbers = {10, 20, 30};
        String[] names = {"Amit", "Priya", "Rahul"};
        Double[] decimalNumbers = {10.5, 20.5, 30.5};
        Long[] longNumbers = {100L, 200L, 300L};

        System.out.println("First number: " + firstElement(numbers));
        System.out.println("First name: " + firstElement(names));

        System.out.println("Integer average: " + average(numbers));
        System.out.println("Double average: " + average(decimalNumbers));
        System.out.println("Long average: " + average(longNumbers));

        GenericStack<Integer> numberStack = new GenericStack<>();
        numberStack.push(10);
        numberStack.push(20);
        System.out.println("Integer stack pop: " + numberStack.pop());

        GenericStack<String> textStack = new GenericStack<>();
        textStack.push("Java");
        textStack.push("Generics");
        System.out.println("String stack pop: " + textStack.pop());

        System.out.println("Number stack size: " + numberStack.size());
        System.out.println("Text stack empty: " + textStack.isEmpty());
    }
}
