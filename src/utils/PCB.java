package utils;
//进程的状态日志

public class PCB {
    private String name;
    private int runtime;
    private int arrivetime;
    private int priority;
    private String condition;
    private PCB nextPcb;
    private int sizeOfMem;

    public PCB() {
        condition = "run";
        nextPcb = null;
    }

    public PCB(String name, int runtime, int priority, String condition, PCB nextPcb) {
        this.name = name;
        this.runtime = runtime;
        this.priority = priority;
        this.condition = condition;
        this.nextPcb = nextPcb;
    }

    public int getSizeOfMem() {
        return sizeOfMem;
    }

    public void setSizeOfMem(int sizeOfMem) {
        this.sizeOfMem = sizeOfMem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getArrivetime() {
        return arrivetime;
    }

    public void setArrivetime(int arrivetime) {
        this.arrivetime = arrivetime;
    }

    public PCB getNextPcb() {
        return nextPcb;
    }

    public void setNextPcb(PCB nextPcb) {
        this.nextPcb = nextPcb;
    }

}
