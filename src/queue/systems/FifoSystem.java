package queue.systems;

import queue.IncorrectUtilizationException;
import queue.graph.NodeData;
import queue.Utils;

import java.util.Map;

/**
 * Created by Igor on 03.12.2016.
 */
public class FifoSystem extends QueueSystem {

    //m
    private int m;

    private double zeroStateProbability;

    public FifoSystem(String id, NodeData node, int position){
        super(id, node,position);
        m = node.getM();
    }

    @Override
    public void calculateUtilization() throws IncorrectUtilizationException {
        //rho = sum(labda[r]) / mi
        rho = clientLambda.sum() / (m * mi);
        for (Map.Entry<String,Double> arrival : clientLambda.getMapValues().entrySet()){
            double util = arrival.getValue() / (m * mi);
            clientRho.setValue(arrival.getKey(),util);
        }
        if(rho > 1){
            throw new IncorrectUtilizationException(id,rho);
        }
        calculateZeroStateProbability();
    }

    private void calculateZeroStateProbability(){
        double sum = 0;
        //6.26 equation from Wiley Interscience Queueing Networks
        for(int k = 0; k < m; k++){
            sum += (Math.pow(m * rho,k)/ Utils.factorial(k)) + ((Math.pow(m * rho, m)/Utils.factorial(m)) * (1/(1 - rho)));
        }
        zeroStateProbability = 1/sum;
    }

    public int getM() {
        return m;
    }

    public double getZeroStateProbability() {
        return zeroStateProbability;
    }

}
