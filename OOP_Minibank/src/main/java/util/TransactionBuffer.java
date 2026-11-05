package util;

/** Fixed-size circular array buffer using wait()/notifyAll() for producer-consumer coordination. */
public class TransactionBuffer {
    private final String[] buffer;
    private int count, head, tail;
    public TransactionBuffer(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Capacity must be positive.");
        buffer = new String[capacity];
    }
    public synchronized void put(String item) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[tail] = item; tail = (tail + 1) % buffer.length; count++; notifyAll();
    }
    public synchronized String take() throws InterruptedException {
        while (count == 0) wait();
        String item = buffer[head]; buffer[head] = null; head = (head + 1) % buffer.length; count--; notifyAll(); return item;
    }
    public static void demo() {
        TransactionBuffer buffer = new TransactionBuffer(3);
        Thread producer = new Thread(() -> {
            try { for (int i = 1; i <= 5; i++) { String tx = "TX-" + i; buffer.put(tx); System.out.println("Produced " + tx); } }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Producer");
        Thread consumer = new Thread(() -> {
            try { for (int i = 1; i <= 5; i++) System.out.println("Consumed " + buffer.take()); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Consumer");
        producer.start(); consumer.start();
        try { producer.join(); consumer.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
