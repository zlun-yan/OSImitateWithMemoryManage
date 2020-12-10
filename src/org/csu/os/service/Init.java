package org.csu.os.service;

import org.csu.os.domain.semaphore.CPUSemaphore;
import org.csu.os.domain.semaphore.PCBSemaphore;
import org.csu.os.service.table.*;

public class Init {
    public static void init(int PCBCount, int defaultTimeSlice) {
        CPUSemaphore.init();
        PCBSemaphore.init(PCBCount);  // 暂时默认设置为6道

        BackUpTableData.init();
        CPUTableData.init(defaultTimeSlice);
        MemoryTableData.init();
        PCBTableData.init(PCBCount);  // 暂时默认设置为6道
        ReadyTableData.init();
        RecordTableData.init();
        SuspendTableData.init();

        AutoMoving.init();
    }
}
