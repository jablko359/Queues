package queue.systems;

/**
 * Created by Igor on 05.01.2017.
 */
public interface CalculatorFactory {
    StateProbabilityCalculator getCalculator(QueueSystem system);
}
