package queue.exceptions;

/**
 * Created by Igor on 07.01.2017.
 */
public class IncorrectUtilizationException extends QueueException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
