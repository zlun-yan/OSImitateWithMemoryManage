package org.csu.os.service.table;

import org.csu.os.domain.pojo.MemoryParam;
import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.csu.os.service.Static.MemoryAllocateMode.FIT;
import static org.csu.os.service.Static.memoryAllocateMode;
import static org.csu.os.service.Static.MemoryAllocateMode.FIRST;

public class MemoryTableData {
    private static List<MemoryParam> remainMemoryList;

    private static int totSize;
    private static int remainSize;

    /**
     * 分配PCB 同时分配内存
     * @param progress 正进入PCB的进程
     * @return 分配的内存空间
     */
    public static MemoryParam allocationMemory(Progress progress) {
        // 暂时使用最先适应分配
        int length = progress.getMemoryLength();
        if (length > remainSize) return null;
        remainSize -= length;

        MemoryParam memoryParam = getAllocationMemoryArea(length);
        if (memoryParam == null) {
            floatMemory();
            memoryParam = remainMemoryList.get(0);
            MemoryParam allocationMemory = new MemoryParam(memoryParam.getStart(), progress.getMemoryLength());
            memoryParam.setStart(allocationMemory.getEnd());
            memoryParam.setLength();
            return allocationMemory;
        }
        else {
            MemoryParam allocationMemory = new MemoryParam(memoryParam.getStart(), length);
            memoryParam.setStart(allocationMemory.getEnd());
            memoryParam.setLength();
            return allocationMemory;
        }
    }

    private static MemoryParam getAllocationMemoryArea(int length) {
        MemoryParam answerParam = null;

        if (memoryAllocateMode == FIRST) {
            for (MemoryParam memoryParam : remainMemoryList) {
                if (memoryParam.getLength() >= length) {
                    answerParam = memoryParam;
                    break;
                }
            }
        }
        else {
            List<MemoryParam> tempList = new ArrayList<>();
            tempList.addAll(remainMemoryList);

            Collections.sort(tempList, new Comparator<MemoryParam>() {
                @Override
                public int compare(MemoryParam o1, MemoryParam o2) {
                    if (o1.getLength() > o2.getLength()) return 1;
                    return -1;
                }
            });
            // 先从从小到大排序

            if (memoryAllocateMode == FIT) {
                int left = 0, right = tempList.size() - 1, mid;
                while (left <= right) {
                    mid = (left + right) >> 1;
                    if (tempList.get(mid).getLength() >= length) right = mid - 1;
                    else left = mid + 1;
                }

                // left 就是最合适的那个
                if (tempList.get(left).getLength() >= length) answerParam = tempList.get(left);
            }
            else {
                int right = tempList.size() - 1;
                if (tempList.get(right).getLength() >= length) answerParam = tempList.get(right);
            }
        }

        return answerParam;
    }

    /**
     * 进程终止 回收内存
     * @param pcb
     */
    public static void recycleMemory(PCB pcb) {
        remainMemoryList.add(pcb.getMemoryParam());
        remainSize += pcb.getMemoryParam().getLength();
        mergeMemory();
    }

    public static int getTotSize() {
        return totSize;
    }

    public static void setTotSize(int totSize) {
        MemoryTableData.totSize = totSize;
    }

    public static int getRemainSize() {
        return remainSize;
    }

    public static void setRemainSize(int remainSize) {
        MemoryTableData.remainSize = remainSize;
    }

    public static List<MemoryParam> getRemainMemoryList() {
        return remainMemoryList;
    }

    /**
     * totSize大于进程所需内存 但是未分分区中没有足够大的区间
     */
    private static void floatMemory() {
        Collections.sort(remainMemoryList);

        int begin = 0;
        for (int i = 0; i < remainMemoryList.size(); i++) {
            int length = remainMemoryList.get(i).getLength();
            remainMemoryList.get(i).setStart(begin);
            begin += length;
            remainMemoryList.get(i).setEnd(begin);
            remainMemoryList.get(i).getLength();
        }

        remainMemoryList.clear();
        remainMemoryList.add(new MemoryParam(begin, totSize - begin));
    }

    /**
     * 删除进程后的区间合并操作
     */
    private static void mergeMemory() {
        Collections.sort(remainMemoryList);

        for (int i = 1; i < remainMemoryList.size(); i++) {
            MemoryParam preMemoryParam = remainMemoryList.get(i - 1);
            MemoryParam nowMemoryParam = remainMemoryList.get(i);

            if (nowMemoryParam.getStart() <= preMemoryParam.getEnd()) {
                preMemoryParam.setEnd(Math.max(preMemoryParam.getEnd(), nowMemoryParam.getEnd()));
                preMemoryParam.setLength();
                remainMemoryList.remove(nowMemoryParam);
                i--;
            }
        }
    }

    public static void init() {
        remainMemoryList = new ArrayList<>();

        totSize = 1394;
        remainSize = 1294;  // 因为在这之后加入了操作系统

        // 加入操作系统
        MemoryParam remainMemory = new MemoryParam(100, totSize - 100);
        remainMemoryList.add(remainMemory);
    }
}
