package org.csu.os.service.table;

import org.csu.os.domain.pojo.PCB;
import org.csu.os.domain.pojo.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.csu.os.service.Static.mode;

public class ReadyTableData {
    private static List<PCB> readyPCBList;

    public static PCB suspend(int selectIndex) {
        PCB returnPCB = readyPCBList.remove(selectIndex);
        sortReadyPCBList();
        return returnPCB;
    }

    public static void updateWaitingTime() {
        // 在AutoMoving里面使用
        // 自增所有在就绪队列中的进程的等待时间
        int count = readyPCBList.size();
        for (int i = 0; i < count; i++) {
            readyPCBList.get(i).getMyProgress().waitTimeMoving();
        }
    }

    public static PCB wakeUp() {
        try {
            PCB returnPCB = readyPCBList.remove(0);
            sortReadyPCBList();
            return returnPCB;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    // 加入一个进程之后 还应该重新排序
    public static void addProgress(PCB pcb) {
        pcb.setState("waiting");
        readyPCBList.add(pcb);
        sortReadyPCBList();
    }

    public static void sortReadyPCBList() {
        switch (mode) {
            case PSA:
                Collections.sort(readyPCBList, new Comparator<PCB>() {
                    @Override
                    public int compare(PCB o1, PCB o2) {
                        Progress progress1 = o1.getMyProgress();
                        Progress progress2 = o2.getMyProgress();
                        if (progress1.getPriority() < progress2.getPriority()) return 1;
                        else return -1;
                    }
                });
                break;
            case SJF:
                Collections.sort(readyPCBList, new Comparator<PCB>() {
                    @Override
                    public int compare(PCB o1, PCB o2) {
                        Progress progress1 = o1.getMyProgress();
                        Progress progress2 = o2.getMyProgress();
                        if (progress1.getTimeLength() > progress2.getTimeLength()) return 1;
                        else return -1;
                    }
                });
                break;
            case MQ:
            case MFQ:
                Collections.sort(readyPCBList, new Comparator<PCB>() {
                    // 只需要按照队列顺序排好就行了
                    @Override
                    public int compare(PCB o1, PCB o2) {
                        Progress progress1 = o1.getMyProgress();
                        Progress progress2 = o2.getMyProgress();
                        if (progress1.getQueueOrder() > progress2.getQueueOrder()) return 1;
                        else return -1;
                    }
                });
                break;
            default:
                break;
        }
        refreshNextPoint();
    }

    private static void refreshNextPoint() {
        int count = readyPCBList.size();
        if (count != 0) {
            for (int i = 0; i < count - 1; i++) {
                readyPCBList.get(i).setNext(readyPCBList.get(i + 1).getPid());
            }

            readyPCBList.get(count - 1).setNext(0);
        }
    }

    public static List<PCB> getReadyPCBList() {
        return readyPCBList;
    }

    /**
     * 这个应该在开始页面 点击开始之后执行
     */
    public static void init() {
        readyPCBList = new ArrayList<>();
    }
}
