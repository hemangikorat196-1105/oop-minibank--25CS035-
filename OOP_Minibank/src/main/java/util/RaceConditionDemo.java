package util;
import model.*;
public final class RaceConditionDemo {
    private RaceConditionDemo(){}
    public static void run(){
        Account unsafe=new SavingsAccount("Unsafe demo",0,0,0.04);
        Thread[] threads=new Thread[10];
        for(int i=0;i<10;i++){threads[i]=new Thread(new AccountWorker(unsafe,1000,1,true),"unsafe-"+i);threads[i].start();}
        join(threads);
        System.out.println("Balance before synchronization (expected 10000, may vary): "+unsafe.getBalance());
        Account safe=new SavingsAccount("Safe demo",0,0,0.04);
        for(int i=0;i<10;i++){threads[i]=new Thread(new AccountWorker(safe,1000,1),"safe-"+i);threads[i].start();}
        join(threads);
        System.out.println("Balance after synchronization: "+safe.getBalance());
    }
    private static void join(Thread[] threads){for(Thread t:threads)try{t.join();}catch(InterruptedException e){Thread.currentThread().interrupt();}}
}
