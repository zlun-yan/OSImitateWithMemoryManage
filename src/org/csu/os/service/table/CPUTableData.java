package org.csu.os.service.table;

import org.csu.os.domain.pojo.PCB;
import org.csu.os.service.AutoMoving;

public class CPUTableData {
    private static PCB runningPCB;
    private static int defaultTimeSlice = 4;
    private static int remainTimeSlice;

    public static void setPCB(PCB pcb) {
        runningPCB = pcb;
        if (runningPCB == null) return;
        if (pcb.getMyProgress().getResponseTime() == -1) pcb.getMyProgress().setResponseTime(AutoMoving.getSystemTime());
        remainTimeSlice = defaultTimeSlice;
        pcb.setState("running");
    }

    public static void updateRemainTimeSlice() {
        remainTimeSlice--;
    }

    public static int getRemainTimeSlice() {
        return remainTimeSlice;
    }

    public static PCB getRunningPCB() {
        return runningPCB;
    }

    public static void init(int defaultTimeSlice) {
        CPUTableData.defaultTimeSlice = defaultTimeSlice;
        runningPCB = null;
    }
}
