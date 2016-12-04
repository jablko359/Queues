package QueueGraph;

import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
public class OpenNetwork extends QueueNetwork {

    public OpenNetwork(Set<QueueSystem> systems, CustomerData clientArrivalCoeff){
        super(systems);
    }


    @Override
    public void countArrivalRatio() {

    }
}
