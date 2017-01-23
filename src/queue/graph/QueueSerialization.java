package queue.graph;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Setter;
import queue.Data;
import queue.exceptions.InvalidNetworkException;
import queue.systems.SystemType;

/**
 * Created by Igor on 03.12.2016.
 */
@XmlRootElement(name = "queueNetwork")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueueSerialization {

	static Double PROBABILITY_APPROX_E = 0.02;
    public static boolean almostEqual(Double a, Double b, Double epsilon) {
    	return Math.abs(a - b) < epsilon;
    }
	
    private HashMap<String, NodeData> systems = new HashMap<>();
    private Edges edges = new Edges();
    private Data clientLambdas = null;
    
    @Setter
    private HashMap<String, Integer> networkCapacity = new HashMap<>();

    public HashMap<String, NodeData> getSystems() {
        return systems;
    }

    public List<EdgeData> getEdges() {
        return edges.getEdges();
    }

    public Data getClientLambdas() {
        return clientLambdas;
    }

    public void setSystems(HashMap<String, NodeData> systems) {
        this.systems = systems;
    }

    public void setEdges(Edges edges) {
        this.edges = edges;
    }

    public void setClientLambdas(Data clientLambdas) {
        this.clientLambdas = clientLambdas;
    }

    public Map<String, Integer> getNetworkCapacity() {
        return networkCapacity;
    }

    public boolean isNetworkClosed() throws InvalidNetworkException {
    	for(Entry<String, NodeData> systemEntry : systems.entrySet()) {
    		Map<String, Double> outputProbability = new HashMap<>();
    		
    		String systemId = systemEntry.getKey();
    		NodeData system = systemEntry.getValue();
    		// checking if input exists for clientID 
    		if (system.getOutsideInput() != null) {
    			if (system.getOutsideInput().sum() != 0) {
    				return false; // non zero input for system
    			}
    		}
    		
    		// checking output probabilities for clients
    		for(EdgeData edge : edges.getEdges()) {
    			if (systemId.equals(edge.getSourceId())) {
    				for(Entry<String, Double> clientProbabilitiesEntry : edge.getProbabilities().getMapValues().entrySet()) {
    					String clientId = clientProbabilitiesEntry.getKey();
    					Double clientProbability = clientProbabilitiesEntry.getValue();
    					
    					// update output for client
    					//System.out.println(clientId + " @ " + edge.getSourceId() + " -> " + edge.getTargetId() + " with: " + clientProbabilitiesEntry.getValue());
    					outputProbability.merge(clientId, clientProbability, (o, n) -> o + n);
    				}
    			}
    		}
    		for(Entry<String, Double> classEntry : outputProbability.entrySet()) {
    			if (classEntry.getValue() > (1 + PROBABILITY_APPROX_E)) {
    				// probability > 1, such a disaster
    				throw new InvalidNetworkException(systemId, classEntry.getKey(), classEntry.getValue());
    			}
    			// there must be an output
    			if (!almostEqual(1d, classEntry.getValue(), PROBABILITY_APPROX_E)) {
    				//System.out.println("CLOSED NETWORK FOR: " + systemId + " @ " + classEntry.getKey() + " WITH " + classEntry.getValue());
    				return false;
    			}
    		}	
    	}
    	return true;
    }
   
    public QueueSerialization(){
    }

    public static QueueSerialization fromFile(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(QueueSerialization.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object deserialized = unmarshaller.unmarshal(file);
        if(deserialized.getClass().equals(QueueSerialization.class)) {
            return (QueueSerialization)deserialized;
        }
        return null;
    }

    //Serialization Test
    public static void main(String[] args) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(QueueSerialization.class);
        Marshaller marshaller = context.createMarshaller();

        NodeData node1 = new NodeData();
        node1.setMi(0.5);
        node1.setM(2);

        Data outsideInput = new Data();
        outsideInput.setValue("Student", 1);
        node1.setOutsideInput(outsideInput);



        NodeData node2 = new NodeData();
        node2.setMi(3);
        node2.setM(1);

        NodeData node3 = new NodeData();
        node3.setSystemType(SystemType.LIFO);
        node3.setMi(4);
        node3.setM(12);

        EdgeData edge1 = new EdgeData();
        Data prob1 = new Data();
        prob1.setValue("Student", 0.2);
        edge1.setSourceId("1");
        edge1.setTargetId("3");
        edge1.setProbabilities(prob1);

        EdgeData edge2 = new EdgeData();
        Data prob2 = new Data();
        prob2.setValue("Student", 0.8);
        edge2.setSourceId("2");
        edge2.setTargetId("3");
        edge2.setProbabilities(prob2);

        QueueSerialization serialization = new QueueSerialization();
        serialization.edges.getEdges().add(edge1);
        serialization.edges.getEdges().add(edge2);

        serialization.systems.put("1", node1);
        serialization.systems.put("2", node2);
        serialization.systems.put("3", node3);

        Data arrivalCoef = new Data();
        arrivalCoef.setValue("Student",5);
        serialization.clientLambdas = arrivalCoef;

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(serialization ,new File("Test.xml"));
    }
}
