package queue.systems;

import queue.graph.NodeData;
import queue.Utils;

/**
 * Created by Igor on 03.12.2016.
 */
public class FifoSystem extends QueueSystem {

    //n
    private int positionCount;

    private double zeroStateProbability;

    public FifoSystem(String id, NodeData node, int position){
        super(id, node,position);
        positionCount = node.getPositions();
    }

    @Override
    public void calculateUtilization() {
        //rho = sum(labda[r]) / mi
        utilization = arrivalRatio.sum() / (positionCount * serviceRatio);
        calculateZeroStateProbability();
    }

    private void calculateZeroStateProbability(){
        double sum = 0;
        //6.26 equation from Wiley Interscience Queueing Networks
        for(int k =0 ; k < positionCount; k++){
            sum += (Math.pow(positionCount * utilization,k)/ Utils.factorial(k)) + ((Math.pow(positionCount * utilization, positionCount)/Utils.factorial(positionCount)) * (1/(1 - utilization)));
        }
        zeroStateProbability = 1/sum;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public double getZeroStateProbability() {
        return zeroStateProbability;
    }
}
