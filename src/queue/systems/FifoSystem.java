package queue.systems;

import queue.graph.NodeData;
import queue.Utils;
import queue.exceptions.IncorrectUtilizationException;

import java.util.Map;

/**
 * Created by Igor on 03.12.2016.
 */
public class FifoSystem extends QueueSystem {

    //m
    private int m;

    private double zeroStateProbability;

    private double Pm;

    public FifoSystem(String id, NodeData node, int position) {
        super(id, node, position);
        m = node.getM();
    }

    @Override
    public void calculateUtilization() throws IncorrectUtilizationException {
        //rho = sum(labda[r]) / mi
        rho = clientLambda.sum() / (m * mi);
        for (Map.Entry<String, Double> arrival : clientLambda.getMapValues().entrySet()) {
            double util = arrival.getValue() / (m * mi);
            clientRho.setValue(arrival.getKey(), util);
        }
        if (rho >= 1) {
            throw new IncorrectUtilizationException(id, rho);
        }
        calculateZeroStateProbability();
        calculatePm();
    }

    private void calculateZeroStateProbability() {
        double sum = 0;
        //6.26 equation from Wiley Interscience Queueing Networks
        for (int k = 0; k < m; k++) {
            sum += (Math.pow(m * rho, k) / Utils.factorial(k)) + ((Math.pow(m * rho, m) / Utils.factorial(m)) * (1 / (1 - rho)));
        }
        zeroStateProbability = 1 / sum;
    }

    private void calculatePm() {
        Pm = (Math.pow(m * rho, m) / (Utils.factorial(m) * (1 - rho))) * zeroStateProbability;
    }

    @Override
    public double getPerformanceMeasure(String clientId) {
        if(m == 1){
            return super.getPerformanceMeasure(clientId);
        }
        double clientTypeRho = clientRho.getValue(clientId);
        double Kir = m * clientTypeRho + (clientTypeRho / (1 - rho)) * Pm;
        return Kir;
    }

    public int getM() {
        return m;
    }

    public double getZeroStateProbability() {
        return zeroStateProbability;
    }

    public void setM(int m) {
        this.m = m;
    }
}
