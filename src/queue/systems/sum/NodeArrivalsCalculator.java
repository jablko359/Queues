package queue.systems.sum;

import java.util.stream.IntStream;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.exceptions.QueueException;
import queue.systems.QueueSystem;

public class NodeArrivalsCalculator {
	
	int networkCapacity;
	QueueSystem[] systems;
	int[] capacities;
	UtilizationCalculator utilCalc;
	int i, r;
	
	public NodeArrivalsCalculator(QueueSystem[] systems, int[] capacities, RealMatrix avgVisits) {
		this.systems = systems;
		this.capacities = capacities;
		this.networkCapacity = IntStream.of(capacities).sum();
		this.utilCalc = new UtilizationCalculator(systems, avgVisits);
		this.i = systems.length;
		this.r = capacities.length;
	}
	
	public RealMatrix calculate(RealVector clientLambdas) throws QueueException {
		RealMatrix arrivals = new Array2DRowRealMatrix(i, r);
		
		for(int systemIdx = 0; systemIdx < i; systemIdx++) {
			QueueSystem system = systems[systemIdx];
			KirCalculator calc = getKir(system);
			double overloadP = utilCalc.getChannelOverloadProbability(systemIdx, clientLambdas);
			double systemUtilization = utilCalc.getSystemUtilization(systemIdx, clientLambdas);
			
			for(int clientIdx = 0; clientIdx < r; clientIdx++) {
				double clientLambda = clientLambdas.getEntry(clientIdx);
				double clientUtilization = utilCalc.getSystemUtilizationForClient(systemIdx, clientIdx, clientLambda);
				double arrival = calc.compute(system, systemUtilization, clientLambda, clientUtilization, overloadP);
				arrivals.addToEntry(systemIdx, clientIdx, arrival);
			}
		}
		
		return arrivals;
	}
	
	KirCalculator getKir(QueueSystem system) {
		switch(system.getType()) {
		case FIFO:
			if (system.getM() > 1)
				return MULTI_CHANNEL;
		case PS:
		case LIFO:
			if (system.getM() == 1)
				return SINGLE_CHANNEL;
			System.out.println(system.getId() + " " + system.getType() + " " + system.getM());
			throw new UnsupportedOperationException();
		case IS:
			return IS;
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	abstract class KirCalculator {
		abstract double compute(QueueSystem system, double systemUtilization, double lambda, double utilization, double overloadProbability);
	}
	
	KirCalculator SINGLE_CHANNEL = new KirCalculator() {

		@Override
		double compute(QueueSystem system, double systemUtilization, double lambda, double utilization,
				double overloadProbability) {
			return utilization / (1 - systemUtilization * (networkCapacity - 1) / networkCapacity);
		}
	};
	
	KirCalculator MULTI_CHANNEL = new KirCalculator() {

		@Override
		double compute(QueueSystem system, double systemUtilization, double lambda, double utilization,
				double overloadProbability) {
			double denominator = 1 - systemUtilization * (networkCapacity - system.getM() - 1) / (networkCapacity - system.getM());
			double kir = utilization * overloadProbability / denominator;
			kir += system.getM() * utilization;
			
			return kir;
		}
	};
	
	KirCalculator IS = new KirCalculator() {

		@Override
		double compute(QueueSystem system, double systemUtilization, double lambda, double utilization,
				double overloadProbability) {
			return lambda / system.getMi();
		}
	};
	
}
