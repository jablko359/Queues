package QueueGraph;

/**
 * Created by Igor on 03.12.2016.
 */
public class FifoSystem extends  QueueSystem {

    private int positionCount;

    public FifoSystem(String id, NodeData node){
        super(id, node);
        positionCount = node.getPositions();
    }
}
