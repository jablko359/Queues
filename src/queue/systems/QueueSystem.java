package queue.systems;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import queue.Data;
import queue.exceptions.IncorrectUtilizationException;
import queue.graph.NodeData;

/**
 * Created by Igor on 03.12.2016.
 */
public abstract class QueueSystem {

	public static final Comparator<QueueSystem> ORDER_COMPARATOR = new Comparator<QueueSystem>() {

		@Override
		public int compare(QueueSystem o1, QueueSystem o2) {
			return o1.getId().compareTo(o2.getId());
		}
	};

    protected String id;
    //indexed position in network
    protected int position;

    // systemType
    @Getter
    protected SystemType type;
    
    //lambda
    protected Data clientLambda = new Data();
    //mi
    @Getter
    protected double mi;
    
    // systemChannels
    @Getter
    protected int m;

    //Rho
    protected double rho;

    public static Comparator<QueueSystem> getOrderComparator() {
        return ORDER_COMPARATOR;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SystemType getType() {
        return type;
    }

    public void setType(SystemType type) {
        this.type = type;
    }

    public double getMi() {
        return mi;
    }

    public void setMi(double mi) {
        this.mi = mi;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public void setOutsideInput(Data outsideInput) {
        this.outsideInput = outsideInput;
    }

    public void setOutputs(Map<QueueSystem, Data> outputs) {
        this.outputs = outputs;
    }

    public void setInputs(Map<QueueSystem, Data> inputs) {
        this.inputs = inputs;
    }

    public Data getClientRho() {
        return clientRho;
    }

    public void setClientRho(Data clientRho) {
        this.clientRho = clientRho;
    }

    //Input from outside world
    protected Data outsideInput;

    protected Map<QueueSystem, Data> outputs = new HashMap<>();
    protected Map<QueueSystem, Data> inputs = new HashMap<>();

    protected Data clientRho = new Data();

    public void addOutput(QueueSystem system, Data data) {
        outputs.put(system, data);
    }

    public void addInput(QueueSystem system, Data data) {
        inputs.put(system, data);
    }

    public String getId() {
        return id;
    }

    public Map<QueueSystem, Data> getInputs() {
        return inputs;
    }

    public int getPosition() {
        return position;
    }

    public double getRho() {
        return rho;
    }

    public Data getClientLambda() {
        return clientLambda;
    }

    public Map<QueueSystem, Data> getOutputs() {
        return outputs;
    }

    public void setClientLambda(Data clientLambda) {
        this.clientLambda = clientLambda;
    }

    public QueueSystem(String id, NodeData data, int position) {
        this.id = id;
        this.position = position;
        this.outsideInput = data.getOutsideInput();
        this.type = data.getSystemType();
        this.mi = data.getMi();
        this.m = data.getM();
    }

    public boolean validate() {

        if (outputs.size() == 0) {
            return true;
        }
        HashMap<String, Double> checkSum = new HashMap();
        for (Data input : outputs.values()) {
            for (String type : input.getTypes()) {

                double value = input.getValue(type);
                if (checkSum.containsKey(type)) {
                    checkSum.put(type, checkSum.get(type) + value);
                } else {
                    checkSum.put(type, value);
                }

                if (checkSum.get(type) > 1) {
                    return false;
                }
            }
        }

        for (String type : checkSum.keySet()) {
            if (checkSum.get(type) == 0 || checkSum.get(type) == 1) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getActiveClients() {
        HashSet<String> activeType = new HashSet<>();
        for (Data data : inputs.values()) {
            for (String type : data.getTypes()) {
                if (data.getValue(type) != 0) {
                    activeType.add(type);
                }
            }
        }
        return activeType;
    }


    public void calculateUtilization() throws IncorrectUtilizationException {
        rho = clientLambda.sum() / mi;
        for (Map.Entry<String, Double> arrival : clientLambda.getMapValues().entrySet()) {
            double util = arrival.getValue() / mi;
            clientRho.setValue(arrival.getKey(), util);
        }
        if (rho >= 1) {
            throw new IncorrectUtilizationException(id, rho);
        }
    }

    public double getPerformanceMeasure(String clientId) {
        double clientUtil = clientRho.getValue(clientId);
        return clientUtil / (1 - rho);
    }

    public Data getOutsideInput() {
        return outsideInput;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public double calculateQ(String clientType) {
        double W = calculateW(clientType);
        double lambda = this.clientLambda.getValue(clientType);
        return lambda * W;
    }

    public double calculateT(String clientType) {
        //7.43 EQ
        double K = this.getPerformanceMeasure(clientType);
        if (this.clientLambda.getValue(clientType) == 0) {
            return 0;
        }
        return K / this.clientLambda.getValue(clientType);
    }

    public double calculateW(String clientType) {
        //7.44
        double T = calculateT(clientType);
        return T - 1 / mi;
    }

}
