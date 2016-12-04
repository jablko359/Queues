package QueueGraph;

/**
 * Created by Igor on 03.12.2016.
 */
public class NodeData {
    private int positions;
    private CustomerData serviceRatio;
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

    public CustomerData getServiceRatio() {
        return serviceRatio;
    }

    public void setServiceRatio(CustomerData serviceRatio) {
        this.serviceRatio = serviceRatio;
    }


}
