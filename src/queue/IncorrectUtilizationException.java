package queue;

/**
 * Created by Igor on 07.01.2017.
 */
public class IncorrectUtilizationException extends Exception {
    private String systemId;
    private double utilizationValue;

    public IncorrectUtilizationException(String systemId, double utilizationValue) {
        this.systemId = systemId;
        this.utilizationValue = utilizationValue;
    }

    @Override
    public String getMessage() {
        return "System with id: " + systemId + " utilization is incorrect: " + utilizationValue;
    }
}
