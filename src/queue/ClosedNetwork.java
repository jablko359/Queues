package queue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.exceptions.IncorrectUtilizationException;
import queue.systems.QueueSystem;

public class ClosedNetwork extends QueueNetwork {

	private RealMatrix avgVisits;
	private LinkedHashMap<String, Integer> networkCapacity = new LinkedHashMap<>();
	private LinkedHashMap<String, QueueSystem> systems = new LinkedHashMap<>();
	
	public ClosedNetwork(Map<String, QueueSystem> systems, Map<String, Integer> networkCapacity) {
		super(systems, null);
		// populate maps with correct order
		systems.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
			this.systems.put(entry.getKey(), entry.getValue());
		});
		networkCapacity.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
			this.networkCapacity.put(entry.getKey(), entry.getValue());
		});
	}

	@Override
	public double getStateProbability(HashMap<String, Integer> coditionMap) {
		throw new UnsupportedOperationException();
	}
	
	public RealMatrix computeEir(Collection<QueueSystem> systemVector, Collection<String> clientClasses) {
		int r = clientClasses.size();
		int i = systemVector.size();
		RealMatrix eir = MatrixUtils.createRealMatrix(i, r);
		
		RealVector constant = new ArrayRealVector(i);
		constant.setEntry(0, -1);
		
		// without class transformation, we can solve r sets of linear equations for each r
		int clientIdx = 0; // [0, r]
		for(String clientId : clientClasses) {
			RealMatrix coeffs = MatrixUtils.createRealMatrix(i, i);
			int to = 0; // [0, i]
			for(QueueSystem toSystem : systemVector) {
				Map<QueueSystem, Data> toSystemProbabilities = toSystem.getInputs();
				int from = 0; // [0, i]
				for(QueueSystem fromSystem : systemVector) {
					if (from == to) {
						coeffs.setEntry(to, from, -1);
					} else if (to != 0 && toSystemProbabilities.containsKey(fromSystem)) {
						// get probability of transition fromSystem to toSystem
						Data transitionProbability = toSystemProbabilities.get(fromSystem);
						coeffs.setEntry(to, from, transitionProbability.getValue(clientId));
					}
					from++;
				}
				to++;
			}
			System.out.println("Coeffs for " + clientId + " " + coeffs);
			
			// solve set of linear equations
			DecompositionSolver solver = new LUDecomposition(coeffs).getSolver();
			RealVector ei = solver.solve(constant);
			eir.setColumnVector(clientIdx, ei);
			clientIdx++;
		}
		return eir;
	}

	@Override
	public void calculateParameters(boolean calculateLambdas) throws IncorrectUtilizationException {
		
		// compute eir 
		avgVisits = computeEir(systems.values(), networkCapacity.keySet());
		
		int r = 0;
		for(String clientId : networkCapacity.keySet()) {
			int i = 0;
			for(String systemId : systems.keySet()) {
				System.out.println(String.format("Avg %s visits @ %s: %f", clientId, systemId, avgVisits.getEntry(i, r)));
				i++;
			}
			r++;
		}
	}
	
}
