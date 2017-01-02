package queue;

import org.apache.commons.math3.linear.RealVector;
import queue.systems.QueueSystem;

import java.util.*;

/**
 * Created by Igor on 03.12.2016.
 */
public class OpenNetwork extends QueueNetwork {

    private String inputSystemId;
    private Data clientArrivalCoeff;
    private QueueSystem startSystem;


    public OpenNetwork(Map<String, QueueSystem> systems, Data clientArrivalCoeff, String inputSystemId) {
        super(new HashSet<>(systems.values()));
        this.clientArrivalCoeff = clientArrivalCoeff;
        this.inputSystemId = inputSystemId;
        startSystem = systems.get(inputSystemId);
        if (startSystem == null) {
            throw new RuntimeException("System " + inputSystemId + " not found");
        }
    }


    @Override
    public void countArrivalRatio() {
        OpenNetworkEquation equation = new OpenNetworkEquation(this);
        Map<String,RealVector> arrivalCoefs = equation.compute();
        for (QueueSystem system : systems){
            int pos = system.getPosition();
            Data data = new Data();
            for (Map.Entry<String,RealVector> vectorEntry : arrivalCoefs.entrySet()){
                data.setValue(vectorEntry.getKey(),vectorEntry.getValue().getEntry(pos));
            }
            system.setArrivalRatio(data);
        }

    }

    public QueueSystem getStartSystem() {
        return startSystem;
    }

    public Data getClientArrivalCoeff() {
        return clientArrivalCoeff;
    }
}
