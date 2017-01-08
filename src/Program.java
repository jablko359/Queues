/**
 * Created by Igor on 03.12.2016.
 */
import queue.IncorrectUtilizationException;
import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.bees.BeeAlgorithm;
import queue.bees.BeeCoordinates;
import queue.bees.DziekanatNodeType;
import queue.graph.QueueSerialization;
import view.SystemCreatorDialog;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Program {

    public static void main(String[] args) throws JAXBException {
        try {
            QueueSerialization serialization = QueueSerialization.fromFile(new File("examples/dziekanat.xml"));
            QueueBuilder builder = new QueueBuilder(serialization);
            QueueNetwork network = builder.buildQueue();

            startBees(network);
        } catch (IncorrectUtilizationException ex){
            ex.printStackTrace();
        }
    }

    //pszczolki
    private static void startBees(QueueNetwork network) {

        //create bee Argorithm object
        int bestSolutionsNumber = 2;
        int exclusiveSolutionsNumber = 1;
        int totalSolutionsNumber = 5;
        int bestSolutionsNeighberhoodNumber = 1;
        int exclusiveSolutionsNeighberhoodNumber = 2;
        BeeAlgorithm beeAlgorithm = new BeeAlgorithm(network, bestSolutionsNumber, exclusiveSolutionsNumber, totalSolutionsNumber, bestSolutionsNeighberhoodNumber, exclusiveSolutionsNeighberhoodNumber);

        //initiate beeAlgorithm
        beeAlgorithm.initialize();

        //calculate
        beeAlgorithm.calculate();
    }
}
