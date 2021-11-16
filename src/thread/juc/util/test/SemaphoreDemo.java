package thread.juc.util.test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    private static final Semaphore semaphore = new Semaphore(3);

    public static void foo() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " get ");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(Thread.currentThread().getName() + " release");
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }

    public static void main(String[] args) {
        foo();
    }
}
