package queue.systems;

import queue.NodeData;

/**
 * Created by Igor on 03.12.2016.
 */
public class FifoSystem extends QueueSystem {

    private int positionCount;

    public FifoSystem(String id, NodeData node, int position){
        super(id, node,position);
        positionCount = node.getPositions();
    }
}
