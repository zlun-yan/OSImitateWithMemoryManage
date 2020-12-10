package org.csu.os.service.controller;

import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;
import org.csu.os.domain.semaphore.CPUSemaphore;
import org.csu.os.domain.semaphore.PCBSemaphore;
import org.csu.os.service.AutoMoving;
import org.csu.os.service.table.*;
import org.csu.os.view.EntryFrame;

public class PCBController {
    public static void addProgress(Progress progress) {
        RecordTableData.addProgress(progress);

        if (PCBSemaphore.waitSemaphore()) {
            // 进入后备队列等待
            BackUpTableData.addProgress(progress);
        }
        else {
            // 分配PCB
            CPUController.addProgress(progress);
        }
        EntryFrame.getMainFrame().refresh();
    }

    public static void resume(int selectRow) {
        PCB newPCB;
        if (selectRow == -1) newPCB = SuspendTableData.resume(0);
        else newPCB = SuspendTableData.resume(selectRow);
        if (newPCB == null) return;

        if (CPUSemaphore.waitSemaphore()) {
            // 进入就绪队列等待
            ReadyTableData.addProgress(newPCB);
        }
        else {
            // 进入CPU
            CPUTableData.setPCB(newPCB);
        }
    }

    public static void suspend(int selectRow) {
        PCB newPCB;
        if (selectRow == -1) {
            newPCB = CPUController.suspend();
        }
        else {
            CPUSemaphore.signalSemaphore();
            newPCB = ReadyTableData.suspend(selectRow);
        }

        if (newPCB == null) return;
        SuspendTableData.addProgress(newPCB);
    }

    /**
     * 这个是进程运行完了 预计运行时间为0
     * @param pcb
     */
    public static void terminal(PCB pcb) {
        // 进程终止 回收内存
        MemoryTableData.recycleMemory(pcb);
        pcb.getMyProgress().setTurnAroundTime(AutoMoving.getSystemTime());

        // 进程终止 回收PCB
        pcb.setBusy(false);
        pcb.setState(null);
        pcb.setMyProgress(null);
        pcb.setMemoryParam(null);

        if (PCBSemaphore.signalSemaphore()) {
            wakeUp();
        }
    }

    /**
     * PCB有空闲 从后备队列唤醒作业
     */
    public static void wakeUp() {
        Progress progress = BackUpTableData.wakeUp();
        if (progress != null) {
            progress.setStartTime(AutoMoving.getSystemTime());
            progress.setWaitTime(0);
            CPUController.addProgress(progress);
        }
        else {
            PCBSemaphore.waitSemaphore();
        }
    }
}
