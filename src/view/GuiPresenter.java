package view;



import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.graph.QueueSerialization;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 30 gru 2016.
 * 14 : 11
 */
public class GuiPresenter {

    final AppCallbacks appCallbacks;

    Thread spidermanThread;

    double value = 0.0;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            value = (value + 0.01);
            if (value > 1) value -= 1;

            appCallbacks.showProgress(value);
        }
    };

    public GuiPresenter(AppCallbacks connector) {
        this.appCallbacks = connector;

        showMockProgressBar();

    }

    private void showMockProgressBar() {
        spidermanThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runnable.run();
            }
        });
        spidermanThread.start();
    }

    public void onFileChosen(File file) {
        QueueSerialization serialization;
        try {
            serialization = QueueSerialization.fromFile(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            appCallbacks.showError(e.getMessage());
            return;
        }

        QueueNetwork queueNetwork = new QueueBuilder(serialization).buildQueue();

        if (queueNetwork != null) {
            appCallbacks.showResults(new Results(queueNetwork));
        } else {
            appCallbacks.showError("Error creating queueNetwork");
        }
    }

    public void stop() {
        if (spidermanThread != null) {
            spidermanThread.interrupt();
            spidermanThread = null;
        }
    }

}
