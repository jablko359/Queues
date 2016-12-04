package QueueGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueSystem {
    private String id;

    private CustomerData arrivalRatio = new CustomerData();
    private CustomerData serviceRatio = new CustomerData();

    private Map<QueueSystem,CustomerData> outputs = new HashMap<>();
    private Map<QueueSystem,CustomerData> inputs = new HashMap<>();

    public void addOutput(QueueSystem system,CustomerData data) {
        outputs.put(system,data);
    }

    public void addInput(QueueSystem system,CustomerData data) {
        inputs.put(system,data);
    }

    public String getId() {
        return id;
    }

    public QueueSystem(String id, NodeData data) {
        this.id = id;

        serviceRatio = data.getServiceRatio();
    }

    public boolean validate(){

        if(inputs.size() == 0){
            return true;
        }
        HashMap<String,Double> checkSum = new HashMap();
        for (CustomerData input : inputs.values()){
            for(String type : input.getTypes()){

                double value = input.getValue(type);
                if(checkSum.containsKey(type)){
                    checkSum.put(type,checkSum.get(type) + value);
                } else {
                    checkSum.put(type,value);
                }

                if(checkSum.get(type) > 1){
                    return false;
                }
            }
        }

        for(String type : checkSum.keySet()) {
            if(checkSum.get(type) == 0 || checkSum.get(type) == 1 ) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getActiveClients(){
        HashSet<String> activeType = new HashSet<>();
        for (CustomerData data : inputs.values()){
            for (String type : data.getTypes()){
                if(data.getValue(type) != 0){
                    activeType.add(type);
                }
            }
        }
        return activeType;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
