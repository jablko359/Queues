package queue.systems.sum;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.Utils;
import queue.exceptions.IncorrectProbabilityException;
import queue.exceptions.IncorrectUtilizationException;
import queue.exceptions.QueueException;
import queue.systems.QueueSystem;

public class UtilizationCalculator {
	
	QueueSystem[] systems;
	RealMatrix avgVisits;
	
	public UtilizationCalculator(QueueSystem[] systems, RealMatrix avgVisits) {
		this.systems = systems;
		this.avgVisits = avgVisits;
	}
	
	public double getChannelOverloadPropability(int systemIdx, RealVector clientLambda) throws QueueException {
		QueueSystem system = systems[systemIdx];
		int systemChannels = system.getM();
		double systemUtilization = getSystemUtilization(systemIdx, clientLambda);
		double systemOverload = systemChannels * systemUtilization;
		
		double probability = Math.pow(systemOverload, systemChannels);
		double denominator = 0;
		for(int channel = 0; channel < systemChannels; channel++) {
			denominator += Math.pow(systemOverload, channel) / Utils.factorial(channel);
			denominator += Math.pow(systemOverload, systemChannels) / (Utils.factorial(systemChannels) * (1 - systemUtilization));
			
		}
		
		probability /= Utils.factorial(systemChannels) * (1 - systemUtilization);
		probability /= denominator;
		
		if (probability > 1)
			throw new IncorrectProbabilityException(system.getId(), "overload", probability);
		return probability;
	}
	
	public double getSystemUtilization(int systemIdx, RealVector clientLambda) throws IncorrectUtilizationException {
		QueueSystem system = systems[systemIdx];
		double systemUtilization = 0;
		int systemChannels = system.getM();
		double systemPerformance = system.getMi();
		
		for(int clientIdx = 0; clientIdx < clientLambda.getDimension(); clientIdx++) {
			systemUtilization += clientLambda.getEntry(clientIdx) * avgVisits.getEntry(systemIdx, clientIdx) / (systemChannels * systemPerformance);
		}
		
		if (systemUtilization >= 1)
			throw new IncorrectUtilizationException(system.getId(), systemUtilization);
		return systemUtilization;
	}
}
