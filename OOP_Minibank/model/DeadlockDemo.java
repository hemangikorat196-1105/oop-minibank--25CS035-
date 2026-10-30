package model;

import java.util.concurrent.CountDownLatch;

public class DeadlockDemo {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch ready = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                ready.countDown();
                try {
                    ready.await();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (lockB) {
                    System.out.println("Thread 1 completed");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                ready.countDown();
                try {
                    ready.await();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (lockA) {
                    System.out.println("Thread 2 completed");
                }
            }
        });

        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();

        Thread.sleep(500);
        System.out.println("Deadlock demonstrated.");

        Object fixedA = new Object();
        Object fixedB = new Object();

        Thread fixed1 = new Thread(() -> transferSafely(fixedA, fixedB, "A to B"));
        Thread fixed2 = new Thread(() -> transferSafely(fixedA, fixedB, "B to A"));

        fixed1.start();
        fixed2.start();

        fixed1.join();
        fixed2.join();

        System.out.println("Fixed version completed successfully.");
    }

    private static void transferSafely(Object a, Object b, String message) {
        synchronized (a) {
            synchronized (b) {
                System.out.println("Transfer completed: " + message);
            }
        }
    }
}