package thread.juc.util.test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    private static  final CountDownLatch cdl = new CountDownLatch(5);

    public static void foo(){
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
               cdl.countDown();
                System.out.println(Thread.currentThread().getName());
            }).start();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        foo();
        cdl.await();
        System.out.println("print success");
    }
}
