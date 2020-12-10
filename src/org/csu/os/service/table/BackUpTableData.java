package org.csu.os.service.table;

import org.csu.os.domain.pojo.Progress;

import java.util.ArrayList;
import java.util.List;

public class BackUpTableData {
    private static List<Progress> progressList;

    public static Progress wakeUp() {
        for (int i = 0; i < progressList.size(); i++) {
            Progress progress = progressList.get(i);
            if (progress.getMemoryLength() <= MemoryTableData.getRemainSize()) {
                progressList.remove(i);
                return progress;
            }
        }

        return null;
    }

    public static void addProgress(Progress progress) {
        progressList.add(progress);
    }

    public static List<Progress> getProgressList() {
        return progressList;
    }

    /**
     * 这个应该在开始页面 点击开始之后执行
     */
    public static void init() {
        progressList = new ArrayList<>();
    }
}
