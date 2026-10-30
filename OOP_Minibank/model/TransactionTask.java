
package model;

public class TransactionTask implements Runnable {
    private final int taskId;

    public TransactionTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println(
                "Task " + taskId + " executed by "
                + Thread.currentThread().getName()
        );

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
