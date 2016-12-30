package view;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 30 gru 2016.
 * 14 : 29
 */
public interface AppCallbacks {

    void showResults(Results results);

    /**
     * value from range [0,1]
     *
     * @param value
     */
    void showProgress(double value);

    void hideProgress();

    void showError(String errorMsg);

    void showSomethingElse();
}
