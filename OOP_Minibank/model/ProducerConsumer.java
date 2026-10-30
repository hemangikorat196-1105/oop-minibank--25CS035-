
package model;

public class ProducerConsumer {
    private int item;
    private boolean available = false;

    public synchronized void produce(int value) {
        try {
            while (available) {
                wait();
            }

            item = value;
            available = true;

            System.out.println("Produced: " + item);
            notifyAll();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void consume() {
        try {
            while (!available) {
                wait();
            }

            System.out.println("Consumed: " + item);
            available = false;
            notifyAll();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


