package QueueGraph;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Igor on 03.12.2016.
 */
@XmlRootElement(name = "edge")
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgeData {
    private CustomerData probabilities;
    private String targetId;

    public CustomerData getProbabilities() {
        return probabilities;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setProbabilities(CustomerData probabilities) {
        this.probabilities = probabilities;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    private String sourceId;
}
