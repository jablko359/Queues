package QueueGraph;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueNetwork {
    protected Set<QueueSystem> systems;
    protected Set<String> activeCutomerTypes;

    protected QueueNetwork(Set<QueueSystem> systems) {
        this.systems = systems;
        activeCutomerTypes = getActiveClientTypes();
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

    protected Set<String> getActiveClientTypes(){
        HashSet<String> activeTypes = new HashSet<>();
        for (QueueSystem system : systems){
            for(String type : system.getActiveClients()){
                activeTypes.add(type);
            }
        }
        return activeTypes;
    }

    public abstract void countArrivalRatio();

}
