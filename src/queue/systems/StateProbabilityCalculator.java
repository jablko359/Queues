package queue.systems;

/**
 * Created by Igor on 05.01.2017.
 */
public abstract class StateProbabilityCalculator {

    QueueSystem system;

    public StateProbabilityCalculator(QueueSystem system){
        this.system = system;
    }

    public abstract double getProbability(int state);
}
