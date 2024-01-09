package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//
public class ProdConsumeDemo {


    static class LockDemo {
        private static int product = 0;

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        public void increment() {
            try {
                lock.lock();
                while (product == 10) {
                    condition.await();
                }
                product++;
                System.out.println("Producer: " + product);
                if (product == 10) {
                    TimeUnit.SECONDS.sleep(1);

                    condition.signalAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void decrement() {
            try {
                lock.lock();
                while (product == 0) {
                    condition.await();
                }
                product--;
                System.out.println("consume:" + product);
                if (product == 0) {
                    TimeUnit.SECONDS.sleep(1);

                    condition.signalAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void foo() {
            new Thread(() -> {
                while (true) {
                    increment();
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    decrement();
                }
            }).start();
        }
    }

    static class BlockQueueDemo {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        public void increment() throws InterruptedException {
            while (true) {
                queue.offer(1);
                System.out.println("increment,queue size:" + queue.size());
                TimeUnit.SECONDS.sleep(1);
            }
        }

        public void decrement() throws InterruptedException {
            while (true) {
                queue.poll();
                System.out.println("decrement,queue size:" + queue.size());
                TimeUnit.SECONDS.sleep(2);
            }
        }

        public void foo() {
            new Thread(() -> {
                try {
                    while (true) {
                        increment();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    while (true) {
                        decrement();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public static void main(String[] args) {
        new LockDemo().foo();
//        new BlockQueueDemo().foo();
    }
}
