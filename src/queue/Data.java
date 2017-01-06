package queue;

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
public class Data {
    private Map<String,Double> values = new HashMap<>();

    public Data(){
    }

    public void setValue(String type,double value) {
        values.put(type,value);
    }

    public double getValue(String type) {
        if(!values.containsKey(type)){
            return 0;
        }
        return values.get(type);
    }

    public Set<String> getTypes() {
        return values.keySet();
    }

    public Map<String, Double> getMapValues() {
        return values;
    }

    public double sum(){
        double sum = 0;
        for (double val : values.values()){
            sum += val;
        }
        return sum;
    }
}
