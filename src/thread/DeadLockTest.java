package thread;

import java.util.concurrent.TimeUnit;

public class DeadLockTest {
    private final Object LOCK1 = new Object();
    private final Object LOCK2 = new Object();

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            synchronized (LOCK1){
                System.out.println("runnable1 get LOCK1");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (LOCK2){
                    System.out.println("runnable1 success");
                }
            }
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            synchronized (LOCK2){
                System.out.println("runnable1 get LOCK2");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (LOCK1){
                    System.out.println("runnable2 success");
                }
            }
        }
    };

    public void foo(){
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        thread1.start();
        thread2.start();
    }

    public static void main(String[] args) {
        new DeadLockTest().foo();
    }
}
