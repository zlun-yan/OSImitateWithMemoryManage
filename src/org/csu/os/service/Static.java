package org.csu.os.service;

public class Static {
    public enum DispatchMode {FCFS, SJF, PSA, RR, MQ, MFQ}
    public enum MemoryAllocateMode {FIRST, FIT, BIGGEST}
    public static DispatchMode mode = DispatchMode.FCFS;
    public static MemoryAllocateMode memoryAllocateMode = MemoryAllocateMode.FIRST;

    public static final String[][] FCFSHeader = {
            {"进程名", "预计运行时间", "所需主存大小"},  // 后备队列
            {"PID", "进程名", "状态", "进程状态", "预计运行时间", "所需主存大小", "主存起始位置"},  // PCB队列
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},  // 就绪队列
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置"},  // CPU
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置"}  //挂起
    };
    public static final String[][] SJFHeader = {
            {"进程名", "预计运行时间", "所需主存大小"},
            {"PID", "进程名", "状态", "进程状态", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置"}
    };
    public static final String[][] PSAHeader = {
            {"进程名", "优先级", "预计运行时间", "所需主存大小"},
            {"PID", "进程名", "状态", "进程状态", "优先级", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "优先级", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},
            {"PID", "进程名", "优先级", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "优先级", "预计运行时间", "所需主存大小", "主存起始位置"}
    };
    public static final String[][] RRHeader = {
            {"进程名", "预计运行时间", "所需主存大小"},
            {"PID", "进程名", "状态", "进程状态", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置", "时间片长度"},
            {"PID", "进程名", "预计运行时间", "所需主存大小", "主存起始位置"}
    };
    public static final String[][] MQHeader = {
            {"进程名", "优先级", "队列", "预计运行时间", "所需主存大小"},
            {"PID", "进程名", "状态", "进程状态", "优先级", "队列", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "优先级", "队列", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},
            {"PID", "进程名", "优先级", "队列", "预计运行时间", "所需主存大小", "主存起始位置", "时间片长度"},
            {"PID", "进程名", "优先级", "队列", "预计运行时间", "所需主存大小", "主存起始位置"}
    };
    public static final String[][] MFQHeader = {
            {"进程名", "队列", "预计运行时间", "所需主存大小"},
            {"PID", "进程名", "状态", "进程状态", "队列", "预计运行时间", "所需主存大小", "主存起始位置"},
            {"PID", "进程名", "队列", "预计运行时间", "所需主存大小", "主存起始位置", "队尾指针"},
            {"PID", "进程名", "队列", "预计运行时间", "所需主存大小", "主存起始位置", "时间片长度"},
            {"PID", "进程名", "队列", "预计运行时间", "所需主存大小", "主存起始位置"}
    };
}
