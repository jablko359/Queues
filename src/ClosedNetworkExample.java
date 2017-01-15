import java.io.File;

import javax.xml.bind.JAXBException;

import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.exceptions.IncorrectUtilizationException;
import queue.exceptions.InvalidNetworkException;
import queue.graph.QueueSerialization;

public class ClosedNetworkExample {
	
	public static void main(String[] args) throws JAXBException {
	    try {
	        QueueSerialization serialization = QueueSerialization.fromFile(new File("examples/ClosedNetwork.xml"));
	        QueueBuilder builder = new QueueBuilder(serialization);
	        QueueNetwork network = builder.buildQueue();

	    } catch (IncorrectUtilizationException | InvalidNetworkException ex){
	        ex.printStackTrace();
	    }	
	}
}
