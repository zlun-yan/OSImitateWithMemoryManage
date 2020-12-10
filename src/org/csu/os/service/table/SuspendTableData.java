package org.csu.os.service.table;

import org.csu.os.domain.pojo.PCB;

import java.util.ArrayList;
import java.util.List;

public class SuspendTableData {
    private static List<PCB> suspendPCBList;

    public static PCB resume(int selectRow) {
        try {
            return suspendPCBList.remove(selectRow);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void addProgress(PCB pcb) {
        pcb.setState("suspending");
        suspendPCBList.add(pcb);
    }

    public static List<PCB> getSuspendPCBList() {
        return suspendPCBList;
    }

    // 未完成
    public static void init() {
        suspendPCBList = new ArrayList<>();
    }
}
