package queue.exceptions;

public class IncorrectProbabilityException extends QueueException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String systemId, probabilityDescription;
	double probability;
	
	public IncorrectProbabilityException(String systemId, String probabilityDescription, double probability) {
		this.systemId = systemId;
		this.probabilityDescription = probabilityDescription;
		this.probability = probability;
	}
	
	@Override
	public String getMessage() {
		return String.format("System %s has incorrect '%s' state probability = %f", systemId, probabilityDescription, probability);
	}
	
}
