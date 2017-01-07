/**
 * Created by Igor on 03.12.2016.
 */
import queue.IncorrectUtilizationException;
import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.graph.QueueSerialization;
import view.SystemCreatorDialog;

import javax.xml.bind.JAXBException;
import java.io.File;

public class Program {
    public static void main(String[] args) throws JAXBException {
        try {
            QueueSerialization serialization = QueueSerialization.fromFile(new File("examples/Test.xml"));
            QueueBuilder builder = new QueueBuilder(serialization);
            QueueNetwork network = builder.buildQueue();
            double K = network.getPerformanceMeasure();
            double Q = network.getSumQ();
            System.out.print("");
        } catch (IncorrectUtilizationException ex){
            ex.printStackTrace();
        }


    }
}
