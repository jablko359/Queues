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


    public Set<String> getActiveCustomerTypes() {
        return activeCustomerTypes;
    }

    public Collection<QueueSystem> getSystems() {
        return systems.values();
    }

    public abstract void calculateParameters();

    public abstract double getStateProbability(HashMap<String, Integer> coditionMap);

}
