package thread;

public class ProducerConsumerExample {
    private int count = 0;
    private final Object lock = new Object();

    public static void main(String[] args) {
        ProducerConsumerExample example = new ProducerConsumerExample();
        Thread producerThread = new Thread(() -> {
            while (true) {
                example.produce();
            }
        });
        Thread consumerThread = new Thread(() -> {
            while (true) {
                example.consume();
            }
        });
        producerThread.start();
        consumerThread.start();
    }

    public void produce() {
        synchronized (lock) {
            while (count == 10) {
                try {
                    lock.wait();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if (count < 10) {
                count++;
            }
            System.out.println("Producer: " + count);
            lock.notifyAll();
        }
    }

    public void consume() {
        synchronized (lock) {
            while (count <= 0) {
                try {
                    lock.wait();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            count--;
            System.out.println("Consumer: " + count);
            lock.notifyAll();
        }
    }
}
