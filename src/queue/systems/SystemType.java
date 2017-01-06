package queue.systems;

import queue.graph.NodeData;

import java.util.HashMap;

/**
 * Created by Igor on 03.12.2016.
 */
public enum SystemType {
    FIFO,PS,IS,LIFO;

    public QueueSystem getSystem(String id, NodeData data, int position){
        return builders.get(this).buildSystem(id, data, position);
    }

    private static HashMap<SystemType,SystemBuilder> builders = new HashMap<>();

    private static interface SystemBuilder{
        QueueSystem buildSystem(String id, NodeData data,int position);
    }

    static {
        builders.put(SystemType.FIFO, (id, data, position) -> new FifoSystem(id, data, position));
        builders.put(SystemType.IS, (id, data, position) -> new ISSystem(id,data, position));
        builders.put(SystemType.LIFO, (id, data, position) -> new LifoSystem(id,data, position));
        builders.put(SystemType.PS, (id, data, position) -> new ProcesorSharingSystem(id,data, position));
    }
}
