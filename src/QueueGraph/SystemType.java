package QueueGraph;

import java.util.HashMap;

/**
 * Created by Igor on 03.12.2016.
 */
public enum SystemType {
    FIFO,PS,IS,LIFO;

    public QueueSystem getSystem(String id, NodeData data){
        return builders.get(this).buildSystem(id, data);
    }

    private static HashMap<SystemType,SystemBuilder> builders = new HashMap<>();

    private static interface SystemBuilder{
        QueueSystem buildSystem(String id, NodeData data);
    }

    static {
        builders.put(SystemType.FIFO, (id, data) -> new FifoSystem(id, data));
        builders.put(SystemType.IS, (id, data) -> new ISSystem(id,data));
        builders.put(SystemType.LIFO, (id, data) -> new LifoSystem(id,data));
        builders.put(SystemType.PS, (id, data) -> new ProcesorSharingSystem(id,data));
    }
}
