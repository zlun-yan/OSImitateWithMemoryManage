package org.csu.os.domain.semaphore;

public class PCBSemaphore {
    private static int count = 6;

    /**
     * 信号量wait操作
     * @return true 进入后备队列等待 false 分配PCB
     */
    public static boolean waitSemaphore() {
        count--;
        return count < 0;
    }

    /**
     * 信号量signal操作
     * @return true 允许给后备队列中的进程分配PCB false 没事
     */
    public static boolean signalSemaphore() {
        count++;
        return count <= 0;
    }

    public static void init(int count) {
        PCBSemaphore.count = count;
    }
}
