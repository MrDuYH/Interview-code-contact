package thread.juc.util.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Thread(() -> System.out.println("success")));

    public static void foo() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, Thread.currentThread().getName()).start();
        }
    }

    public static void main(String[] args) {
        foo();
    }
}
