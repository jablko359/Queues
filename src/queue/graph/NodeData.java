package queue.graph;

import queue.systems.SystemType;

/**
 * Created by Igor on 03.12.2016.
 */
public class NodeData {
    private int positions;
    private double serviceRatio;
    private SystemType systemType = SystemType.FIFO;

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }

    public SystemType getSystemType() {
        return systemType;
    }

    public void setSystemType(SystemType systemType) {
        this.systemType = systemType;
    }

    public double getServiceRatio() {
        return serviceRatio;
    }

    public void setServiceRatio(double serviceRatio) {
        this.serviceRatio = serviceRatio;
    }


}
