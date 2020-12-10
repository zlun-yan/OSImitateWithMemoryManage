package org.csu.os.domain.pojo;

public class PCB {
    private int pid;
    private boolean busy;
    private String state;

    private Progress myProgress;
    private MemoryParam memoryParam;

    private int next;

    public PCB() {}

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Progress getMyProgress() {
        return myProgress;
    }

    public void setMyProgress(Progress myProgress) {
        this.myProgress = myProgress;
    }

    public MemoryParam getMemoryParam() {
        return memoryParam;
    }

    public void setMemoryParam(MemoryParam memoryParam) {
        this.memoryParam = memoryParam;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
