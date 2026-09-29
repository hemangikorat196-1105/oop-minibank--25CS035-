import java.util.concurrent.*;

public class ThreadpoolRunner{
    public static void main(String[] args) {
    
        ExecutorService pool1 = Executors.newFixedThreadPool(3);

        for(int i=1 ;i<=10 ;i++){
            final int taskid =i;

            pool1.submit(()-> {
                System.out.println("Task "+taskid+" is running on "+Thread.currentThread().getName());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Task "+taskid+" completed by "+Thread.currentThread().getName());
            });
        }

        pool1.shutdown();

        try {
            if(pool1.awaitTermination(10, TimeUnit.SECONDS)){
                System.out.println("All Tasks Completed.");
            }
            else{
                System.out.println("Timeout.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}