package QueueGraph;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Igor on 03.12.2016.
 */
@XmlRootElement(name = "queueNetwork")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueueSerialization {



    private HashMap<String, NodeData> systems = new HashMap<>();
    private Edges edges = new Edges();
    private boolean isClosed = false;

    @XmlElement(name ="arrivalCoefficient")
    private CustomerData clientArrivalCoeff = null;

    public HashMap<String, NodeData> getSystems() {
        return systems;
    }

    public List<EdgeData> getEdges() {
        return edges.getEdges();
    }

    public boolean isClosed() {
        return isClosed;
    }

    public CustomerData getClientArrivalCoeff() {
        return clientArrivalCoeff;
    }

    protected QueueSerialization(){
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
        CustomerData serviceRatio1 = new CustomerData();
        serviceRatio1.setValue("Student", 0.5);
        node1.setServiceRatio(serviceRatio1);
        node1.setPositions(2);


        NodeData node2 = new NodeData();
        CustomerData serviceRatio2 = new CustomerData();
        serviceRatio2.setValue("Student", 0.4);
        serviceRatio2.setValue("Teacher", 0.4);
        node2.setServiceRatio(serviceRatio2);
        node2.setPositions(1);

        NodeData node3 = new NodeData();
        CustomerData serviceRatio3 = new CustomerData();
        serviceRatio3.setValue("Student", 0.2);
        serviceRatio3.setValue("Teacher", 0.4);
        node3.setServiceRatio(serviceRatio3);
        node3.setPositions(12);

        EdgeData edge1 = new EdgeData();
        CustomerData prob1 = new CustomerData();
        prob1.setValue("Student", 0.2);
        edge1.setSourceId("1");
        edge1.setTargetId("3");
        edge1.setProbabilities(prob1);

        EdgeData edge2 = new EdgeData();
        CustomerData prob2 = new CustomerData();
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

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(serialization ,new File("examples/Test.xml"));
    }
}
