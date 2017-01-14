package queue.graph;

import queue.Data;
import queue.systems.SystemType;

/**
 * Created by Igor on 03.12.2016.
 */
public class NodeData {
    private int m;
    private double mi;
    private SystemType systemType = SystemType.FIFO;
    //input from outside world for open systems only. For closed ones it should be 0
    private Data outsideInput = new Data();

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public SystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(SystemType systemType) {
        this.systemType = systemType;
    }

    public double getMi() {
        return mi;
    }

    public void setMi(double mi) {
        this.mi = mi;
    }

    public Data getOutsideInput() {
        return outsideInput;
    }

    public void setOutsideInput(Data outsideInput) {
        this.outsideInput = outsideInput;
    }
}
