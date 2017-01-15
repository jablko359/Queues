package queue.exceptions;

public class InvalidNetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String systemId;
	String clientId;
	double probability;
	
	public InvalidNetworkException(String systemId, String clientId, double probability) {
		this.systemId = systemId;
		this.clientId = clientId;
		this.probability = probability;
	}
	
	@Override
	public String getMessage() {
		return String.format("System '%s' has inconsistent ouput probability value = %f for client class = %s", systemId, probability, clientId);
	}
}
