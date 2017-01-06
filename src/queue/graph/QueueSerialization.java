package queue.graph;

import queue.Data;
import queue.systems.SystemType;

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
    private String inputSystemId;

    @XmlElement(name ="arrivalCoefficient")
    private Data clientArrivalCoeff = null;

    public HashMap<String, NodeData> getSystems() {
        return systems;
    }

    public List<EdgeData> getEdges() {
        return edges.getEdges();
    }

    public boolean isClosed() {
        return isClosed;
    }

    public Data getClientArrivalCoeff() {
        return clientArrivalCoeff;
    }

    public String getInputSystemId() {
        return inputSystemId;
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
        node1.setServiceRatio(0.5);
        node1.setPositions(2);


        NodeData node2 = new NodeData();
        node2.setServiceRatio(3);
        node2.setPositions(1);

        NodeData node3 = new NodeData();
        node3.setSystemType(SystemType.LIFO);
        node3.setServiceRatio(4);
        node3.setPositions(12);

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
        serialization.inputSystemId = "1";

        Data arrivalCoef = new Data();
        arrivalCoef.setValue("Student",5);
        serialization.clientArrivalCoeff = arrivalCoef;

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(serialization ,new File("Test.xml"));
    }
}
