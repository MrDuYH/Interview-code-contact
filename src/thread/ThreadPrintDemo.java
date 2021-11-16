package thread;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPrintDemo {
    //abc交替打印10次

    static class syncDemo {

        private String flag = "a";
        private int countA = 30;
        private int countB = 0;
        private int countC = 0;

        private final Object LOCK = new Object();
        Thread threadA = new Thread(() -> {
            while (countA < 10) {
                synchronized (LOCK) {
                    if (Objects.equals(flag, "a")) {
                        System.out.println(flag);
                        flag = "b";
                        countA += 1;
                    } else {
                        LOCK.notifyAll();
                    }
                }
            }
        });

        Thread threadB = new Thread(() -> {
            while (countB < 10) {
                synchronized (LOCK) {
                    if (Objects.equals(flag, "b") && countB < 10) {
                        System.out.println(flag);
                        flag = "c";
                        countB += 1;
                    } else {
                        LOCK.notifyAll();
                    }
                }
            }
        });

        Thread threadC = new Thread(() -> {
            while (countC < 10) {
                synchronized (LOCK) {
                    if (Objects.equals(flag, "c") && countC < 10) {
                        System.out.println(flag);
                        flag = "a";
                        countC += 1;
                    } else {
                        LOCK.notifyAll();
                    }
                }
            }
        });

        public void foo() {
            threadA.start();
            threadB.start();
            threadC.start();
        }
    }

    static class LockDemo {
        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        private String flag = "a";
        Thread threadA = new Thread(() -> {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (!flag.equals("a")) {
                        conditionA.await();
                    }
                    System.out.println(flag);
                    flag = "b";
                    conditionB.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread threadB = new Thread(() -> {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (!flag.equals("b")) {
                        conditionB.await();
                    }
                    System.out.println(flag);
                    flag = "c";
                    conditionC.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread threadC = new Thread(() -> {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (!flag.equals("c")) {
                        conditionC.await();
                    }
                    System.out.println(flag);
                    flag = "a";
                    conditionA.signal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        private void foo() {
            threadA.start();
            threadB.start();
            threadC.start();

        }
    }

    static class LockSupportDemo {
        Thread a, b, c;

        public void foo() {
            a = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    System.out.println("a");
                    LockSupport.unpark(b);
                    LockSupport.park();
                }
            });

            b = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    LockSupport.park();
                    System.out.println("b");
                    LockSupport.unpark(c);
                }
            });

            c = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    LockSupport.park();
                    System.out.println("c");
                    LockSupport.unpark(a);
                }
            });
            a.start();
            b.start();
            c.start();
        }
    }

    public static void main(String[] args) {
//        new syncDemo().foo();
//        new LockDemo().foo();

        new LockSupportDemo().foo();
    }
}
