package queue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.exceptions.QueueException;
import queue.systems.QueueSystem;
import queue.systems.sum.ClientLambdaCalculator;
import queue.systems.sum.NodeArrivalsCalculator;

public class ClosedNetwork extends QueueNetwork {

	private RealMatrix avgVisits;
	private LinkedHashMap<String, Integer> networkCapacity = new LinkedHashMap<>();
	private LinkedHashMap<String, QueueSystem> systems = new LinkedHashMap<>();
	private RealVector clientLambdas;
	private RealMatrix avgArrivals, queueLength, waitTime, residenceTime, lambdas, mis;
	
	public ClosedNetwork(Map<String, QueueSystem> systems, Map<String, Integer> networkCapacity) {
		super(systems, null);
		// populate maps with correct order
		systems.values().stream().sorted(QueueSystem.ORDER_COMPARATOR).forEach(queueSystem -> {
			this.systems.put(queueSystem.getId(), queueSystem);
		});
		networkCapacity.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
			this.networkCapacity.put(entry.getKey(), entry.getValue());
		});
	}

	public QueueSystem[] getSystemsAsArray() {
		return systems.values().toArray(new QueueSystem[] {});
	}
	
	public String[] getClientsAsArray() {
		return networkCapacity.keySet().toArray(new String[] {});
	}
	
	public int[] getCapacitiesAsArray() {
		int[] capacities = new int[networkCapacity.size()];
		int i = 0;
		for(Integer capacity : networkCapacity.values()) {
			capacities[i++] = capacity;
		}
		return capacities;
	}
	
	@Override
	public double getStateProbability(HashMap<String, Integer> coditionMap) {
		throw new UnsupportedOperationException();
	}
	
	public RealMatrix computeEir(QueueSystem[] systems, String[] clients) {
		int r = clients.length;
		int i = systems.length;
		RealMatrix eir = MatrixUtils.createRealMatrix(i, r);
		
		RealVector constant = new ArrayRealVector(i);
		constant.setEntry(0, -1);
		
		// without class transformation, we can solve r sets of linear equations for each r
		for(int clientIdx = 0; clientIdx < r; clientIdx++) {
			RealMatrix coeffs = MatrixUtils.createRealMatrix(i, i);
			for(int to = 0; to < i; to++) {
				Map<QueueSystem, Data> toSystemProbabilities = systems[to].getInputs();
				for(int from = 0; from < i; from++) {
					if (from == to) {
						coeffs.setEntry(to, from, -1);
					} else if (to != 0 && toSystemProbabilities.containsKey(systems[from])) {
						// get probability of transition fromSystem to toSystem
						Data transitionProbability = toSystemProbabilities.get(systems[from]);
						coeffs.setEntry(to, from, transitionProbability.getValue(clients[clientIdx]));
					}
				}
			}
			System.out.println("Coeffs for " + clients[clientIdx] + " " + coeffs);
			
			// solve set of linear equations
			DecompositionSolver solver = new LUDecomposition(coeffs).getSolver();
			RealVector ei = solver.solve(constant);
			eir.setColumnVector(clientIdx, ei);
		}
		return eir;
	}

	@Override
	public void calculateParameters(boolean calculateLambdas) throws QueueException {
		
		// compute eir 
		avgVisits = computeEir(getSystemsAsArray(), getClientsAsArray());
		
		// compute client lambdas
		ClientLambdaCalculator clientLambdasCalc = new ClientLambdaCalculator(getSystemsAsArray(), getCapacitiesAsArray(), avgVisits);
		clientLambdas = clientLambdasCalc.calculate();
		lambdas = clientLambdasCalc.calculateForNetwork();
		System.out.println("ClientLambdas: " + clientLambdas);
		
		// compute average client arrivals for each node
		NodeArrivalsCalculator nodeArrivalsCalc = new NodeArrivalsCalculator(getSystemsAsArray(), getCapacitiesAsArray(), avgVisits);
		avgArrivals = nodeArrivalsCalc.calculate(clientLambdas);
		System.out.println("K: "+  avgArrivals);
		
		/*
		 * calculate standard parameters
		 */
		
		// Tir
		residenceTime = Utils.ebeDivide(avgArrivals, lambdas);
		System.out.println("T: " + residenceTime);
		
		// mi ir
		mis = new Array2DRowRealMatrix(systems.size(), networkCapacity.size());
		int i =0;
		for(QueueSystem system : systems.values()) {
			mis.setRowVector(i++, new ArrayRealVector(networkCapacity.size(), system.getMi()));
		}
		
		// Wir
		waitTime = Utils.ebeApply(residenceTime, mis, (a, b) -> a - 1/b);
		System.out.println("W: " + waitTime);
		
		// Qir
		queueLength = Utils.ebeMultiply(lambdas, waitTime);
		System.out.println("Q: " + queueLength);
	}
	
}
