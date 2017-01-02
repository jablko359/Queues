package queue;

import queue.systems.QueueSystem;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueNetwork {
    protected Set<QueueSystem> systems;
    protected Set<String> activeCustomerTypes;

    protected QueueNetwork(Set<QueueSystem> systems) {
        this.systems = systems;
        activeCustomerTypes = getActiveClientTypes();
    }

    public boolean validate(){
        //TODO check if graph is consistent
        for(QueueSystem system: systems){
            if(!system.validate()){
                return false;
            }
        }
        return true;
    }

    private Set<String> getActiveClientTypes(){
        HashSet<String> activeTypes = new HashSet<>();
        for (QueueSystem system : systems){
            for(String type : system.getActiveClients()){
                activeTypes.add(type);
            }
        }
        return activeTypes;
    }

    public Set<String> getActiveCustomerTypes() {
        return activeCustomerTypes;
    }

    public Set<QueueSystem> getSystems() {
        return systems;
    }

    public abstract void countArrivalRatio();

}
