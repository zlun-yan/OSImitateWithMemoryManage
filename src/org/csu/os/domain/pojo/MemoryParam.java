package org.csu.os.domain.pojo;

public class MemoryParam implements Comparable<MemoryParam>{
    private int start;
    private int end;
    private int length;

    public MemoryParam() {}

    /**
     * 赋值 start end length(通过start和end)
     * @param start 起始地址
     * @param length 长度
     */
    public MemoryParam(int start, int length) {
        this.start = start;
        this.end = start + length;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    /**
     * 设置完起始地址和终止地址之后
     * 调用这个方法来计算长度
     */
    public void setLength() {
        length = end - start;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public int compareTo(MemoryParam o) {
        if (start == o.start) return end > o.end ? 1 : -1;
        return start > o.start ? 1 : -1;
    }
}
