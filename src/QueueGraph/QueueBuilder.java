package QueueGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Igor on 03.12.2016.
 */
public class QueueBuilder {

    private QueueSerialization queueSerialization;

    public QueueBuilder(QueueSerialization deserialziedData) {
        queueSerialization = deserialziedData;
    }


    public QueueNetwork buildQueue() {
        HashMap<String, NodeData> nodes = queueSerialization.getSystems();
        HashMap<String, QueueSystem> systems = new HashMap<>();
        for (Map.Entry<String, NodeData> dataEntry : nodes.entrySet()) {
            SystemType systemType = dataEntry.getValue().getSystemType();
            QueueSystem system = systemType.getSystem(dataEntry.getKey(), dataEntry.getValue());
            systems.put(system.getId(), system);
        }
        for (EdgeData data : queueSerialization.getEdges()) {
            QueueSystem source = systems.get(data.getSourceId());
            QueueSystem target = systems.get(data.getTargetId());
            if(source == null || target == null) {
                throw new RuntimeException("system not found");
            }
            source.addOutput(target,data.getProbabilities());
            target.addInput(source,data.getProbabilities());
        }
        if(queueSerialization.isClosed()){
            //TODO  http://www.memecenter.com/fun/2051433/pink-panther-amp-039-s-to-do-list
            return null;
        }
        QueueNetwork network =  new OpenNetwork(new HashSet<>(systems.values()), queueSerialization.getClientArrivalCoeff());
        if(!network.validate()) {
            throw new RuntimeException("Queue system not valid");
        }
        return network;
    }






}
