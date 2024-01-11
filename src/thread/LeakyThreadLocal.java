package thread;

import java.util.ArrayList;
import java.util.List;

public class LeakyThreadLocal {
    private static final List<ThreadLocal<byte[]>> threadLocals = new ArrayList<>();

    public static void main(String[] args) {
        int i = 0;
        while (true) {
            System.out.println("i:" + i++);
            ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();
            threadLocals.add(threadLocal);
            try {
                new Thread(() -> {
                    threadLocal.set(new byte[1024 * 1024 * 1024]); // 设置1MB的字节数组
                    // 线程执行其他任务，但忘记清除ThreadLocal的值
                }).start();
            } catch (Error e) {
                System.out.println("oom,i:" + i);
            }
        }
    }


}
