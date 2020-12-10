package org.csu.os.service.table;

import org.csu.os.domain.pojo.Progress;

import java.util.ArrayList;
import java.util.List;

public class RecordTableData {
    private static List<Progress> progressList;

    public static void addProgress(Progress progress) {
        progressList.add(progress);
    }

    public static List<Progress> getProgressList() {
        return progressList;
    }

    public static double getAveWaitTime() {
        double totTime = 0;
        double havePCBCount = 0;
        for (Progress progress : progressList) {
            if (progress.getWaitTime() != -1) {
                totTime += progress.getWaitTime();
                havePCBCount++;
            }
        }

        if (havePCBCount == 0) return 0;
        return totTime / havePCBCount;
    }

    // 未完成
    public static void init() {
        progressList = new ArrayList<>();
    }
}
