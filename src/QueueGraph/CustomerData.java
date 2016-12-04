package QueueGraph;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 03.12.2016.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerData {
    private Map<String,Double> values = new HashMap<>();

    public CustomerData(){
    }

    public void setValue(String type,double value) {
        values.put(type,value);
    }

    public double getValue(String type) {
        return values.get(type);
    }

    public Set<String> getTypes() {
        return values.keySet();
    }
}
