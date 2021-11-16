package thread.juc.util.test;

public class CommoditySpikeDemo {
    private final Object LOCK = new Object();
    private int product = 100;

    public void sell() {
        try {
            synchronized (LOCK) {
                if (product > 0) {
                    product -= 1;
                    System.out.println(Thread.currentThread().getName() + " success, product num:" + product);
                } else {
                    System.out.println("sell out");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        CommoditySpikeDemo demo = new CommoditySpikeDemo();
        for (int i = 0; i < 999; i++) {
            new Thread(demo::sell).start();
        }
    }
}
