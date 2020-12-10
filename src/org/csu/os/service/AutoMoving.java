package org.csu.os.service;

import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;
import org.csu.os.service.controller.CPUController;
import org.csu.os.service.controller.PCBController;
import org.csu.os.service.table.CPUTableData;
import org.csu.os.service.table.ReadyTableData;
import org.csu.os.view.MainFrame;

import java.util.Timer;
import java.util.TimerTask;

import static org.csu.os.service.Static.mode;

public class AutoMoving {
    private static boolean started = false;
    private static int systemTime = 0;
    private static int step = 1;
    private static MainFrame targetFrame;

    private static Timer SystemTimer;

    public static void start(MainFrame targetFrame) {
        if (started) return; // 如果已经开始了就返回吧
        started = true;
        AutoMoving.targetFrame = targetFrame;
        SystemTimer = new Timer(true);
        SystemTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                doMoving();
            }
        }, 0, 500); // 每500毫秒进行一次
    }

    public static void pause() {
        if (!started) return;
        started = false;
        SystemTimer.cancel();
        SystemTimer.purge();
        PCBController.suspend(-1);
    }

    private static void doMoving() {
        systemTime++;
        ReadyTableData.updateWaitingTime();

        try {
            switch (mode) {
                case FCFS:
                    doFCFSMoving();
                    break;
                case PSA:
                    doPSAMoving();
                    break;
                case RR:
                    doRRMoving();
                    break;
                case SJF:
                    doSJFMoving();
                    break;
                case MQ:
                    doMQMoving();
                    break;
                case MFQ:
                    doMFQMoving();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Hello");
        }
    }

    private static void doFCFSMoving() throws Exception{
        PCB pcb = CPUTableData.getRunningPCB();
        if (pcb == null) return;
        Progress progress = pcb.getMyProgress();
        progress.updateTimeLength();
        if (progress.getTimeLength() <= 0) CPUController.terminal();

        targetFrame.refresh();
    }

    private static void doPSAMoving() throws Exception{
        PCB myPCB = CPUTableData.getRunningPCB();
        if (myPCB == null) return;
        Progress progress = myPCB.getMyProgress();
        progress.updateTimeLength();
        progress.updatePriority();
        CPUController.terminal();

        targetFrame.refresh();
    }

    private static void doRRMoving() throws Exception{
        PCB pcb = CPUTableData.getRunningPCB();
        if (pcb == null) return;
        Progress progress = pcb.getMyProgress();
        progress.updateTimeLength();
        CPUTableData.updateRemainTimeSlice();
        if (CPUTableData.getRemainTimeSlice() <= 0 || progress.getTimeLength() <= 0) CPUController.terminal();

        targetFrame.refresh();
    }

    private static void doSJFMoving() throws Exception{
        doFCFSMoving();
    }

    private static void doMQMoving() throws Exception{
        PCB pcb = CPUTableData.getRunningPCB();
        if (pcb == null) return;
        Progress progress = pcb.getMyProgress();
        progress.updateTimeLength();
        if (progress.getQueueOrder() == 1) {
            // 前台队列 额外判断一下时间片
            CPUTableData.updateRemainTimeSlice();
            if (CPUTableData.getRemainTimeSlice() <= 0) CPUController.terminal();
        }

        if (progress.getTimeLength() <= 0) CPUController.terminal();

        targetFrame.refresh();
    }

    private static void doMFQMoving() throws Exception{
        PCB pcb = CPUTableData.getRunningPCB();
        if (pcb == null) return;
        Progress progress = pcb.getMyProgress();
        progress.updateTimeLength();
        CPUTableData.updateRemainTimeSlice();
        if (CPUTableData.getRemainTimeSlice() <= 0 || progress.getTimeLength() <= 0) {
            progress.setQueueOrder(progress.getQueueOrder() + 1);
            CPUController.terminal();
        }

        targetFrame.refresh();
    }

    public static int getSystemTime(){
        return systemTime;
    }

    public static void init() {
        if (SystemTimer != null) {
            SystemTimer.cancel();
            SystemTimer.purge();
        }
        SystemTimer = null;
        targetFrame = null;
        systemTime = 0;
        started = false;
    }
}
