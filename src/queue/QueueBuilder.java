package queue;

import queue.graph.EdgeData;
import queue.graph.NodeData;
import queue.graph.QueueSerialization;
import queue.systems.QueueSystem;
import queue.systems.SystemType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 03.12.2016.
 */
public class QueueBuilder {

    private QueueSerialization queueSerialization;

    public QueueBuilder(QueueSerialization deserializedData) {
        queueSerialization = deserializedData;
    }


    public QueueNetwork buildQueue() throws IncorrectUtilizationException {
        int counter = 0;
        HashMap<String, NodeData> nodes = queueSerialization.getSystems();
        HashMap<String, QueueSystem> systems = new HashMap<>();
        for (Map.Entry<String, NodeData> dataEntry : nodes.entrySet()) {
            SystemType systemType = dataEntry.getValue().getSystemType();
            QueueSystem system = systemType.getSystem(dataEntry.getKey(), dataEntry.getValue(), counter);
            systems.put(system.getId(), system);
            counter++;
        }
        for (EdgeData data : queueSerialization.getEdges()) {
            QueueSystem source = systems.get(data.getSourceId());
            QueueSystem target = systems.get(data.getTargetId());
            if (source == null || target == null) {
                throw new RuntimeException("system not found");
            }
            source.addOutput(target, data.getProbabilities());
            target.addInput(source, data.getProbabilities());
        }
        QueueNetwork network;
        if (queueSerialization.isClosed()) {
            return null;
        } else {
            network = new OpenNetwork(systems, queueSerialization.getClientLambdas());
        }
//        if (!network.validate()) {
//            throw new RuntimeException("queue system not valid");
//        }

        network.calculateParameters(true);

        return network;
    }


}
