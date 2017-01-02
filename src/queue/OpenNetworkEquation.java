package queue;

import queue.Data;
import org.apache.commons.math3.linear.*;
import queue.OpenNetwork;
import queue.systems.QueueSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 02.01.2017.
 */
public class OpenNetworkEquation {

    private OpenNetwork network;
    private HashMap<String, RealMatrix> matrices;
    private HashMap<String ,RealVector> constants;

    public OpenNetworkEquation(OpenNetwork network) {
        this.network = network;
        initialize();
    }

    private void initialize() {
        Set<String> activeCustomers = network.getActiveCustomerTypes();
        Set<QueueSystem> systems = network.getSystems();
        matrices = new HashMap<>();
        constants = new HashMap<>();
        for (String customerType : activeCustomers) {
            int dim = network.getSystems().size();
            RealVector constant = new ArrayRealVector(dim);
            constant.setEntry(network.getStartSystem().getPosition(),network.getClientArrivalCoeff().getValue(customerType));
            RealMatrix matrix = getIdentityMatrix(dim);
            for (QueueSystem system : systems) {
                int row = system.getPosition();
                if (system != network.getStartSystem()) {
                    Map<QueueSystem, Data> inputs = system.getInputs();
                    for (Map.Entry<QueueSystem, Data> entry : inputs.entrySet()) {
                        double value = -entry.getValue().getValue(customerType);
                        int col = entry.getKey().getPosition();
                        matrix.setEntry(row, col, value);
                    }
                }
            }
            constants.put(customerType,constant);
            matrices.put(customerType, matrix);
        }
    }

    public Map<String,RealVector> compute(){
        Map result = new HashMap<String,RealVector>();
        for(String key : matrices.keySet()){
            RealMatrix coeff = matrices.get(key);
            RealVector constant = constants.get(key);
            DecompositionSolver solver = new LUDecomposition(coeff).getSolver();
            RealVector res = solver.solve(constant);
            result.put(key,res);
        }
        return result;
    }

    private static RealMatrix getIdentityMatrix(int dim) {
        RealMatrix matrix = new Array2DRowRealMatrix(dim, dim);
        for (int i = 0; i < dim; i++) {
            matrix.setEntry(i, i, 1);
        }
        return matrix;
    }

}
