import java.io.File;

import javax.xml.bind.JAXBException;

import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.exceptions.InvalidNetworkException;
import queue.exceptions.QueueException;
import queue.graph.QueueSerialization;

public class ClosedNetworkExample {
	
	public static void main(String[] args) throws JAXBException {
	    try {
	        QueueSerialization serialization = QueueSerialization.fromFile(new File("examples/ClosedNetwork.xml"));
	        QueueBuilder builder = new QueueBuilder(serialization);
	        QueueNetwork network = builder.buildQueue();

	    } catch (QueueException | InvalidNetworkException ex){
	        ex.printStackTrace();
	    }	
	}
}
