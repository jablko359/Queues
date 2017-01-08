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
//            double K = network.getK();
//            System.out.println("K: " + K);
//            double Q = network.getQ();
//            for (String customer : network.getActiveCustomerTypes()) {
//                System.out.println("Q: " + network.getQ("1", customer));
//                System.out.println("Q: " + network.getQ("2", customer));
//                System.out.println("Q: " + network.getQ("3", customer));
//                System.out.println("Q: " + network.getQ("4", customer));
//                System.out.println("W: " + network.getW("1", customer));
//                System.out.println("W: " + network.getW("2", customer));
//                System.out.println("W: " + network.getW("3", customer));
//                System.out.println("W: " + network.getW("4", customer));
//            }

            startBees(network);
        } catch (IncorrectUtilizationException ex){
            ex.printStackTrace();
        }
    }

    //pszczolki
    private static void startBees(QueueNetwork network) {

        //create bee Argorithm object
        int bestSolutionsNumber = 100;
        int exclusiveSolutionsNumber = 50;
        int totalSolutionsNumber = 500;
        int bestSolutionsNeighberhoodNumber = 10;
        int exclusiveSolutionsNeighberhoodNumber = 20;
        BeeAlgorithm beeAlgorithm = new BeeAlgorithm(network, bestSolutionsNumber, exclusiveSolutionsNumber, totalSolutionsNumber, bestSolutionsNeighberhoodNumber, exclusiveSolutionsNeighberhoodNumber);

        //initiate beeAlgorithm
        beeAlgorithm.initialize();

        //calculate
        beeAlgorithm.calculate();
    }
}
