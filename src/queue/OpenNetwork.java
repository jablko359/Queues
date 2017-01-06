package queue;

import org.apache.commons.math3.linear.RealVector;
import queue.systems.CalculatorFactory;
import queue.systems.OpenNetworkCalculator;
import queue.systems.QueueSystem;
import queue.systems.StateProbabilityCalculator;

import java.util.*;

/**
 * Created by Igor on 03.12.2016.
 */
public class OpenNetwork extends QueueNetwork {

    private String inputSystemId;
    private Data clientArrivalCoeff;
    private QueueSystem startSystem;


    public OpenNetwork(Map<String, QueueSystem> systems, Data clientArrivalCoeff, String inputSystemId) {
        super(systems, new OpenNetworkCalculator());
        this.clientArrivalCoeff = clientArrivalCoeff;
        this.inputSystemId = inputSystemId;
        startSystem = systems.get(inputSystemId);
        if (startSystem == null) {
            throw new RuntimeException("System " + inputSystemId + " not found");
        }
    }

    @Override
    public void calculateParameters() {
        OpenNetworkEquation equation = new OpenNetworkEquation(this);
        Map<String,RealVector> arrivalCoeffs = equation.compute();
        for (QueueSystem system : systems.values()){
            int pos = system.getPosition();
            Data data = new Data();
            for (Map.Entry<String,RealVector> vectorEntry : arrivalCoeffs.entrySet()){
                data.setValue(vectorEntry.getKey(),vectorEntry.getValue().getEntry(pos));
            }
            system.setArrivalRatio(data);
        }

        for(QueueSystem system : systems.values()){
            system.calculateUtilization();
        }
    }

    @Override
    public double getStateProbability(HashMap<String, Integer> conditionMap) {
        if(conditionMap.isEmpty()){
            return 0;
        }
        double result = 1;
        for (Map.Entry<String,Integer> condition : conditionMap.entrySet()){
            String systemId = condition.getKey();
            QueueSystem system = systems.get(systemId);
            if(system == null){
                throw new IllegalArgumentException("System with id: " + systemId + " not found");
            }
            StateProbabilityCalculator calculator = calculatorFactory.getCalculator(system);
            double stateProbability = calculator.getProbability(condition.getValue());
            result *= stateProbability;
        }
        return result;
    }

    public QueueSystem getStartSystem() {
        return startSystem;
    }

    public Data getClientArrivalCoeff() {
        return clientArrivalCoeff;
    }
}
