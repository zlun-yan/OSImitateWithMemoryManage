package org.csu.os.domain.semaphore;

public class CPUSemaphore {
    private static int mutex = 1;

    /**
     * 信号量wait操作
     * @return true 进入就绪队列等待 false 进入CPU
     */
    public static boolean waitSemaphore() {
        mutex--;
        return mutex < 0;
    }

    /**
     * 信号量signal操作
     * @return true 允许其他进程进入CPU false 没事
     */
    public static boolean signalSemaphore() {
        mutex++;
        return mutex <= 0;
    }

    public static void init () {
        mutex = 1;
    }
}
