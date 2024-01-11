package thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//生产者消费者
public class ProdConsumeDemo {

    static class WaitNotifyDemo {
        int product = 0;
        Object lock = new Object();

        public void produce() {
            synchronized (lock) {
                while (true) {
                    while (product == 10) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    product ++;
                    System.out.println("Producer: " + product);
                    if (product == 10) {
                        lock.notify();
                    }
                }
            }
        }

        public void consume() {
            synchronized (lock) {
                while (true) {
                    while (product == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    product --;
                    if (product == 0) {
                        lock.notify();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("consumer: " + product);
                }
            }
        }

    }

    static class LockSupportDemo {
        private static int product = 0;

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        public void increment() {
            try {
                //获取锁
                lock.lock();
                //一直生产
                while (true) {
                    //生产到10个后,阻塞当前线程
                    while (product == 10) {
                        condition.await();
                    }
                    //产品+1
                    product++;
                    System.out.println("Producer: " + product);
                    //生产到10个后,通知其他线程
                    if (product == 10) {
                        TimeUnit.SECONDS.sleep(1);
                        condition.signalAll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void decrement() {
            try {
                //获取锁
                lock.lock();
                //一直消费
                while (true) {
                    //消费到0,阻塞当前线程
                    while (product == 0) {
                        condition.await();
                    }
                    //产品-1
                    product--;
                    System.out.println("consume:" + product);
                    //产品为0,通知其他线程
                    if (product == 0) {
                        TimeUnit.SECONDS.sleep(1);
                        condition.signalAll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void foo() {
            new Thread(this::increment).start();

            new Thread(this::decrement).start();
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
        WaitNotifyDemo waitNotifyDemo = new WaitNotifyDemo();
        Thread t1 = new Thread(() -> waitNotifyDemo.produce());
        t1.start();
        Thread t2 = new Thread(() -> waitNotifyDemo.consume());
        t2.start();
    }
}
