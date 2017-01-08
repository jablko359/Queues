package queue;

import queue.systems.CalculatorFactory;
import queue.systems.QueueSystem;
import queue.systems.StateProbabilityCalculator;

import java.util.*;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueNetwork {
    protected Map<String,QueueSystem> systems;
    protected Set<String> activeCustomerTypes;
    protected CalculatorFactory calculatorFactory;

    protected QueueNetwork(Map<String,QueueSystem> systems, CalculatorFactory factory) {
        this.systems = systems;
        this.calculatorFactory = factory;
        activeCustomerTypes = getActiveClientTypes();
    }

    public boolean validate() {
        //TODO check if graph is consistent
        for (QueueSystem system : systems.values()) {
            if (!system.validate()) {
                return false;
            }
        }
        return true;
    }

    private Set<String> getActiveClientTypes() {
        HashSet<String> activeTypes = new HashSet<>();
        for (QueueSystem system : systems.values()) {
            for (String type : system.getActiveClients()) {
                activeTypes.add(type);
            }
        }
        return activeTypes;
    }

    public double getK(){
        double measure = 0;
        for (QueueSystem system : systems.values()){
            for (String customerType : activeCustomerTypes){
                measure += system.getPerformanceMeasure(customerType);
            }
        }
        return measure;
    }

    public double getK(String clientType, String systemId){
        QueueSystem system = systems.get(systemId);
        if(system != null){
            return system.getPerformanceMeasure(clientType);
        }
        throw new IllegalArgumentException("Unknown system: " + systemId);
    }

    public double getQ(String systemId ,String clientId){
        QueueSystem system = systems.get(systemId);
        if(system != null){
            return system.calculateQ(clientId);
        }
        throw new IllegalArgumentException("Unknown system: " + systemId);
    }

    public double getW(String systemId ,String clientId){
        QueueSystem system = systems.get(systemId);
        if(system != null){
            return system.calculateW(clientId);
        }
        throw new IllegalArgumentException("Unknown system: " + systemId);
    }

    public double getLambdaT(String systemId ,String clientId){
        QueueSystem system = systems.get(systemId);
        if(system != null){
            return system.getPerformanceMeasure(clientId);
        }
        throw new IllegalArgumentException("Unknown system: " + systemId);
    }

    public double getT(String systemId ,String clientId){
        QueueSystem system = systems.get(systemId);
        if(system != null){
            return system.calculateT(clientId);
        }
        throw new IllegalArgumentException("Unknown system: " + systemId);
    }

    public Set<String> getActiveCustomerTypes() {
        return activeCustomerTypes;
    }

    public Collection<QueueSystem> getSystems() {
        return systems.values();
    }

    public abstract double getStateProbability(HashMap<String, Integer> coditionMap);

    public Map<String, QueueSystem> getSystemsMap() {
        return systems;
    }

    public void setSystems(Map<String, QueueSystem> systems) {
        this.systems = systems;
    }

    public abstract void calculateParameters(boolean calculateLambdas) throws IncorrectUtilizationException;

}
