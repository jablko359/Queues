package queue.systems.sum;

import java.util.stream.IntStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.exceptions.QueueException;
import queue.systems.QueueSystem;

public class ClientLambdaCalculator {

	static final double METHOD_EPSILON = 0.00001;

	QueueSystem[] systems;
	int[] capacities;
	RealMatrix avgVisits;
	UtilizationCalculator utilCalc;
	int networkCapacity, i, r;
	
	public ClientLambdaCalculator(QueueSystem[] systems, int[] capacities, RealMatrix avgVisits) {
		this.systems = systems;
		this.capacities = capacities;
		this.avgVisits = avgVisits;
		
		i = systems.length;
		r = capacities.length;
		utilCalc = new UtilizationCalculator(systems, avgVisits);
		networkCapacity = IntStream.of(capacities).sum();
	}
	
	public RealMatrix calculateForNetwork() throws QueueException {
		RealMatrix lambdas = new Array2DRowRealMatrix(i, r);
		RealVector clientLambdas = calculate();
		
		for(int systemIdx = 0; systemIdx < i; systemIdx++) {
			lambdas.setRowVector(systemIdx, clientLambdas.ebeMultiply(avgVisits.getRowVector(systemIdx)));
		}
		
		return lambdas;
	}
	
	public RealVector calculate() throws QueueException {
		RealVector clientLambda = new ArrayRealVector(r);
		RealVector previousClientLambda = new ArrayRealVector(r);
		clientLambda.set(METHOD_EPSILON);
		
		while(clientLambda.getDistance(previousClientLambda) > METHOD_EPSILON) {
			previousClientLambda = clientLambda.copy();
			clientLambda = computeOnce(clientLambda);
		}
		
		return clientLambda;
	}
	
	public RealVector computeOnce(RealVector previousLambdas) throws QueueException {
		RealVector newClientLambda = new ArrayRealVector(r);
		
		for (int clientIdx = 0; clientIdx < r; clientIdx++) {
			double denominator = 0;
			for(int systemIdx = 0; systemIdx < i; systemIdx++) {
				double utilization = utilCalc.getSystemUtilization(systemIdx, previousLambdas);
				double overloadProbability = utilCalc.getChannelOverloadProbability(systemIdx, previousLambdas);
				double visits = avgVisits.getEntry(systemIdx, clientIdx);
					
				FixCalculator fix = getFixCalculator(systems[systemIdx]);
				denominator += fix.computeFix(systems[systemIdx], visits, utilization, overloadProbability);
			}
			newClientLambda.setEntry(clientIdx, capacities[clientIdx] / denominator);
		}
		
		return newClientLambda;
	}
	
	// fix calculating
	
	FixCalculator getFixCalculator(QueueSystem system) {
		switch(system.getType()) {
		case FIFO:
			if (system.getM() > 1)
				return MULTI_CHANNEL;
		case PS:
		case LIFO:
			if (system.getM() == 1)
				return SINGLE_CHANNEL;
//			System.out.println(system.getId() + " " + system.getType() + " " + system.getM());
			throw new UnsupportedOperationException();
		case IS:
			return IS;
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	abstract class FixCalculator {
	
		abstract double computeFix(QueueSystem system, double avgVisits, double utilization, double overloadProbability);
	}
	
	FixCalculator SINGLE_CHANNEL = new FixCalculator() {
		
		@Override
		double computeFix(QueueSystem system, double avgVisits, double utilization, double overloadProbability) {
			double fix = avgVisits / system.getMi();
			fix /= (1 - utilization * (networkCapacity - 1) / networkCapacity);
			
			return fix;
		}
	};
	
	FixCalculator MULTI_CHANNEL = new FixCalculator() {
		
		@Override
		double computeFix(QueueSystem system, double avgVisits, double utilization, double overloadProbability) {
			double fix = overloadProbability * avgVisits;
			fix /= system.getM() * system.getMi();
			fix /= (1 - utilization * (networkCapacity - system.getM() - 1) / (networkCapacity - system.getM()));
			fix += avgVisits / system.getMi();
			
			return fix;
		}
	};
	
	FixCalculator IS = new FixCalculator() {
		
		@Override
		double computeFix(QueueSystem system, double avgVisits, double utilization, double overloadProbability) {
			return avgVisits / system.getMi();
		}
	};
}
