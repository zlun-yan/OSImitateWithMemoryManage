package org.csu.os.service.table;

import org.csu.os.domain.pojo.MemoryParam;
import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;

import java.util.ArrayList;
import java.util.List;

public class PCBTableData {
    private static List<PCB> PCBList;
    private static int count = 6;  // 默认是6道

    public static PCB addProgress(Progress progress) {
        MemoryParam memoryParam = MemoryTableData.allocationMemory(progress);
        if (memoryParam == null) return null;

        for (int i = 0; i < count; i++) {
            PCB pcb = PCBList.get(i);
            if (!pcb.isBusy()) {
                pcb.setBusy(true);
                pcb.setMyProgress(progress);
                pcb.setMemoryParam(memoryParam);

                return pcb;
            }
        }

        return null;
    }

    /**
     * 这个应该在开始页面 点击开始之后执行
     */
    public static void init(int count) {
        PCBList = new ArrayList<>();
        PCBTableData.count = count;

        for (int i = 0; i < count; i++) {
            PCB pcb = new PCB();
            pcb.setPid(i + 1);
            pcb.setBusy(false);

            PCBList.add(pcb);
        }
    }

    public static List<PCB> getPCBList() {
        return PCBList;
    }
}
