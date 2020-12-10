package org.csu.os.service.controller;

import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;
import org.csu.os.domain.semaphore.CPUSemaphore;
import org.csu.os.domain.semaphore.PCBSemaphore;
import org.csu.os.service.AutoMoving;
import org.csu.os.service.table.*;

public class CPUController {
    public static void addProgress(Progress progress) {
        PCB pcb = PCBTableData.addProgress(progress);
        if (pcb == null) {
            BackUpTableData.addProgress(progress);
            return;
        }

        if (progress.getStartTime() == -1) {
            progress.setResponseTime(0);
            progress.setTurnAroundTime(0);
            progress.setWaitTime(0);
        }
        progress.setStartTime(AutoMoving.getSystemTime());
        progress.setWaitTime(0);

        if (CPUSemaphore.waitSemaphore()) {
            // 进入就绪队列等待
            ReadyTableData.addProgress(pcb);
        }
        else {
            // 进入CPU
            CPUTableData.setPCB(pcb);
        }
    }

    /**
     * 对正在CPU上运行的进程挂起
     */
    public static PCB suspend() {
        PCB runningPCB = CPUTableData.getRunningPCB();
        if (runningPCB == null) return null;

        CPUTableData.setPCB(null);
        if (CPUSemaphore.signalSemaphore()) {
            wakeUp();
        }

        return runningPCB;
    }

    /**
     * 这个是从CPU上下来
     */
    public static void terminal() {
        PCB runningPCB = CPUTableData.getRunningPCB();

        CPUTableData.setPCB(null);
        //现在只是从CPU上下来 如果预计运行时间不为0的话 就说明还要进入就绪队列
        if (runningPCB.getMyProgress().getTimeLength() <= 0) PCBController.terminal(runningPCB);
        else {
            CPUSemaphore.waitSemaphore();
            ReadyTableData.addProgress(runningPCB);
        }

        if (CPUSemaphore.signalSemaphore()) {
            // 允许其他进程进入CPU
            wakeUp();
        }
    }

    public static void wakeUp() {
        PCB newPCB = ReadyTableData.wakeUp();
        if (newPCB != null) CPUTableData.setPCB(newPCB);
    }
}
