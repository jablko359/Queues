package view.bees;

import java.io.File;

import javax.xml.bind.JAXBException;

import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.exceptions.InvalidNetworkException;
import queue.exceptions.QueueException;
import queue.graph.QueueSerialization;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 14 sty 2017.
 * 13 : 53
 */
public class BeesPresenter {
    BeesCallbacks callbacks;
    QueueNetwork parsedNetwork;

    public BeesPresenter(BeesCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void onFileChosen(File file) {
        if (file == null) callbacks.showError("Choose file first!");
        else {

            parsedNetwork = getNetwork(file);
            if (parsedNetwork != null) {
                callbacks.showEditBeeParameters();
            }
        }
    }

    private QueueNetwork getNetwork(File file) {
        String errorMessage = "";
        try {
            QueueSerialization serialization = QueueSerialization.fromFile(file);
            QueueBuilder builder = new QueueBuilder(serialization);
            QueueNetwork network = builder.buildQueue();
            return network;

        } catch (QueueException | InvalidNetworkException ex) {
            ex.printStackTrace();
            errorMessage += " build queueNetwork: " + ex.getMessage() + "\n";
        } catch (JAXBException e) {
            e.printStackTrace();
            errorMessage += " parse file: " + e.getMessage() + "\n";
        }
        callbacks.showError("Error: " + errorMessage);
        return null;
    }

    public QueueNetwork getQueue() {
        return parsedNetwork;
    }
}
