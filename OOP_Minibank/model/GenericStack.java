
package model;

import java.util.ArrayList;

public class GenericStack<T> {

    private final ArrayList<T> items = new ArrayList<>();

    public void push(T item) {
        items.add(item);
    }

    public T pop() {
        if (items.isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }

        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(items.size() - 1);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }
}
