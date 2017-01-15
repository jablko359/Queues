package queue.systems.sum;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import queue.systems.QueueSystem;

public abstract class KirCalculator {

	public static KirCalculator getCalculator(QueueSystem system) {
		switch(system.getType()) {
		case FIFO:
			if (system.getMi() > 1)
				return MULTI_CHANNEL;
		case PS:
		case LIFO:
			if (system.getMi() == 1)
				return SINGLE_CHANNEL;
			throw new UnsupportedOperationException();
		case IS:
			return IS;
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private KirCalculator() {
		// hide constructor
	}
	
	public abstract Double calculate(QueueSystem system, RealVector capacity, RealMatrix avgVisits);
	
	static KirCalculator SINGLE_CHANNEL = new KirCalculator() {
		
		@Override
		public Double calculate(QueueSystem system, RealVector capacity, RealMatrix avgVisits) {
			// TODO Auto-generated method stub
			return null;
		
		}
	};
	
	static KirCalculator MULTI_CHANNEL = new KirCalculator() {
		
		@Override
		public Double calculate(QueueSystem system, RealVector capacity, RealMatrix avgVisits) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	static KirCalculator IS = new KirCalculator() {
		
		@Override
		public Double calculate(QueueSystem system, RealVector capacity, RealMatrix avgVisits) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	
}
