package queue.graph;

import queue.graph.EdgeData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Igor on 03.12.2016.
 */
@XmlRootElement(name = "edges")
@XmlAccessorType(XmlAccessType.FIELD)
public class Edges {
    @XmlElement(name = "edge")
    public List<EdgeData> edges = new LinkedList<>();

    public List<EdgeData> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeData> edges) {
        this.edges = edges;
    }
}
