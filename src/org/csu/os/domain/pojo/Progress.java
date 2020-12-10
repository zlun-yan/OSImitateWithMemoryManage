package org.csu.os.domain.pojo;

import org.csu.os.service.AutoMoving;

public class Progress {
    private String name;

    private int priority;
    private int timeLength;  //预计允许时间
    private int memoryLength;
    private int queueOrder;  // 这里记录多级队列和多级反馈队列的参数

    // 这里之所以赋值为-1 是为了在RecordTable中好显示
    // 只有那些没有分配PCB的进程 等等等等 这些值就都是 -1
    // 分配PCB之后 再将这些值调为 0
    private int startTime;
    private int responseTime;
    private int turnAroundTime;
    private int waitTime;

    public Progress() {
        startTime = -1;
        responseTime = -1;
        turnAroundTime = -1;
        waitTime = -1;
    }

    public Progress(String name, int priority, int timeLength, int memoryLength, int queueOrder) {
        this.name = name;
        this.priority = priority;
        this.timeLength = timeLength;
        this.memoryLength = memoryLength;
        this.queueOrder = queueOrder;

        startTime = -1;
        responseTime = -1;
        turnAroundTime = -1;
        waitTime = -1;
    }
    // 下面加入一些额外的操作

    /**
     * 时间长度自减1
     */
    public void updateTimeLength() {
        timeLength -= 1;
    }

    /**
     * 优先级自减1
     */
    public void updatePriority() {
        priority -= 1;
    }

    /**
     * 进入PCB之后 初始化
     */
    public void accessPCBInit() {
        startTime = AutoMoving.getSystemTime();
        waitTime = 0;
    }

    /**
     * 得到响应之后 设置 responseTime
     */
    public void afterResponse() {
        responseTime = AutoMoving.getSystemTime() - startTime;
    }

    /**
     * 进程完成后 得到周转时间
     */
    public void afterTerminate() {
        turnAroundTime = AutoMoving.getSystemTime() - startTime;
    }

    /**
     * 在就绪队列中 wait时间 +1
     */
    public void waitTimeMoving() {
        waitTime += 1;
    }

    //  下面都是 get set 方法 上面再加一些额外的操作
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public int getMemoryLength() {
        return memoryLength;
    }

    public void setMemoryLength(int memoryLength) {
        this.memoryLength = memoryLength;
    }

    public int getQueueOrder() {
        return queueOrder;
    }

    public void setQueueOrder(int queueOrder) {
        this.queueOrder = queueOrder;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int firstCPUSystemTime) {
        this.responseTime = firstCPUSystemTime - startTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int terminalSystemTime) {
        this.turnAroundTime = terminalSystemTime - startTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
