package view.bees;

import view.Results;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 14 sty 2017.
 * 13 : 54
 */
public interface BeesCallbacks {

    void showError(String errorMsg);

    void showResults(Results results);

    void showEditBeeParameters();
}
