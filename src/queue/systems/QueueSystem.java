package queue.systems;

import queue.graph.NodeData;
import queue.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueSystem {

    protected String id;
    //indexed position in network
    protected int position;

    //lambda
    protected Data arrivalRatio = new Data();
    //mi
    protected double serviceRatio;

    //Rho
    protected double utilization;


    protected Map<QueueSystem, Data> outputs = new HashMap<>();
    protected Map<QueueSystem, Data> inputs = new HashMap<>();

    protected Data clientUtilization = new Data();

    public void addOutput(QueueSystem system, Data data) {
        outputs.put(system,data);
    }

    public void addInput(QueueSystem system, Data data) {
        inputs.put(system,data);
    }

    public String getId() {
        return id;
    }

    public Map<QueueSystem, Data> getInputs() {
        return inputs;
    }

    public int getPosition() {
        return position;
    }

    public double getUtilization() {
        return utilization;
    }

    public Data getArrivalRatio() {
        return arrivalRatio;
    }

    public Map<QueueSystem, Data> getOutputs() {
        return outputs;
    }

    public void setArrivalRatio(Data arrivalRatio) {
        this.arrivalRatio = arrivalRatio;
    }

    public QueueSystem(String id, NodeData data, int position) {
        this.id = id;
        this.position = position;
        serviceRatio = data.getServiceRatio();
    }

    public boolean validate(){

        if(outputs.size() == 0){
            return true;
        }
        HashMap<String,Double> checkSum = new HashMap();
        for (Data input : outputs.values()){
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
        for (Data data : inputs.values()){
            for (String type : data.getTypes()){
                if(data.getValue(type) != 0){
                    activeType.add(type);
                }
            }
        }
        return activeType;
    }


    public void calculateUtilization() {
        utilization = arrivalRatio.sum() / serviceRatio;
        for (Map.Entry<String,Double> arrival : arrivalRatio.getMapValues().entrySet()){
            double util = arrival.getValue() / serviceRatio;
            clientUtilization.setValue(arrival.getKey(),util);
        }
        if(utilization > 1){
            throw new RuntimeException("Utilization rate is more than 1 (" + utilization +") for system: " + id);
        }
    }

    public double getPerformanceMeasure(String clientId){
        double clientUtil = clientUtilization.getValue(clientId);
        return clientUtil / (1 - utilization);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
