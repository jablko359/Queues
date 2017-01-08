package queue;

import org.apache.commons.math3.linear.RealVector;
import queue.systems.OpenNetworkCalculator;
import queue.systems.QueueSystem;
import queue.systems.StateProbabilityCalculator;

import java.util.*;

/**
 * Created by Igor on 03.12.2016.
 */
public class OpenNetwork extends QueueNetwork {


    private Data clientLambdas;


    public OpenNetwork(Map<String, QueueSystem> systems, Data clientArrivalCoeff) {
        super(systems, new OpenNetworkCalculator());
        this.clientLambdas = clientArrivalCoeff;

    }

    @Override
    public void calculateParameters(boolean calculateLambda) throws IncorrectUtilizationException {
        if (calculateLambda) {
            OpenNetworkEquation equation = new OpenNetworkEquation(this);
            Map<String,RealVector> arrivalCoeffs = equation.compute();
            for (QueueSystem system : systems.values()){
                int pos = system.getPosition();
                Data data = new Data();
                for (Map.Entry<String,RealVector> vectorEntry : arrivalCoeffs.entrySet()){
                    data.setValue(vectorEntry.getKey(),vectorEntry.getValue().getEntry(pos));
                }
                system.setClientLambda(data);
            }
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

    public Data getClientLambdas() {
        return clientLambdas;
    }
}
