package view;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 30 gru 2016.
 * 14 : 35
 */

public class Results {

    Object results;

    public Results(Object results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return String.valueOf(results);
    }
}
