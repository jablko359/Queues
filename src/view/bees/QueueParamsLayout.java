package view.bees;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import org.apache.commons.math3.linear.RealMatrix;
import queue.ClosedNetwork;
import queue.QueueNetwork;
import queue.bees.Bee;
import queue.bees.BeeAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 15 sty 2017.
 * 01 : 18
 */
public class QueueParamsLayout {
    public QueueParamsLayout(QueueNetwork network, BeeAlgorithm beeAlgorithm) {
        Dialog<Boolean> dialog = new Dialog<>();

        VBox rootView = new VBox();

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        ScrollPane pane = new ScrollPane(newParamsLayout(network, beeAlgorithm));

        pane.setMaxHeight(400);

        rootView.getChildren().add(pane);

        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(clickedButton -> null);

        dialog.showAndWait().ifPresent(createdQueue -> dialog.close());
    }

    public static Node newParamsLayout(QueueNetwork network, BeeAlgorithm beeAlgorithm) {
        VBox node = new VBox();

        List<String> infos = produceInfo(network, beeAlgorithm);

        Button clipBoard = new Button("Copy to clipboard");
        clipBoard.setOnAction(c -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            StringBuilder sb = new StringBuilder();
            for (String s : infos) {
                sb.append(s).append("\n");
            }
            content.putString(sb.toString());
            clipboard.setContent(content);

            System.out.println("copied to clipboard");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WTF");
            alert.setContentText("COPIED TO CLIPBOARD!");
            alert.showAndWait();
        });
        node.getChildren().add(clipBoard);
        for (String s : infos) {
            node.getChildren().add(new Label(s));
        }

        return node;
    }

    private static List<String> produceInfo(QueueNetwork network, BeeAlgorithm beeAlgorithm) {

        if (network instanceof ClosedNetwork) {
            ClosedNetwork net = (ClosedNetwork) network;
            return produceInfoForClosedNetwork(net, beeAlgorithm);
        } else {
            return produceInfoForOpenNetwork(network, beeAlgorithm);
        }

    }

    private static List<String> produceInfoForOpenNetwork(QueueNetwork network, BeeAlgorithm beeAlgorithm) {
        List<String> data = new ArrayList<>();


        data.add("K: " + network.getK());

        String[] systems = new String[]{"dzienne", "zaoczne", "doktoranckie", "socjalne", "dziekan"};
        String[] clients = new String[]{"Student", "Pracownik"};

        for (String sys : systems) {
            for (String cli : clients) {
                try {
                    data.add("System: " + sys + ", klient: " + cli + ", K = " + network.getK(cli, sys));
                    data.add("System: " + sys + ", klient: " + cli + ", Q = " + network.getQ(sys, cli));
                    data.add("System: " + sys + ", klient: " + cli + ", T = " + network.getT(sys, cli));
                    data.add("System: " + sys + ", klient: " + cli + ", W = " + network.getW(sys, cli));
                } catch (IllegalArgumentException | IllegalStateException x) {
                }
            }
        }

        String prev = "";
        if (beeAlgorithm != null) {
            for (Bee bee : beeAlgorithm.getGlobalBestBees()) {
                if (!prev.equals(bee.toString())) {
                    prev = bee.toString();
                    data.add(bee.toString());
                }
            }
        }

        return data;

    }

    private static List<String> produceInfoForClosedNetwork(ClosedNetwork net, BeeAlgorithm beeAlgorithm) {
        List<String> data = new ArrayList<>();
        data.add("Client lambdas: ");
        data.add(net.getClientLambdas().toString());
		data.add("A: ");
		data.add(printRealmMatrix(net.getAvgVisits()));
        data.add("K: ");
        data.add(printRealmMatrix(net.getAvgArrivals()));
        data.add("T: ");
        data.add(printRealmMatrix(net.getResidenceTime()));
        data.add("W: ");
        data.add(printRealmMatrix(net.getWaitTime()));
        data.add("Q: ");
        data.add(printRealmMatrix(net.getQueueLength()));

        String prev = "";
        if (beeAlgorithm != null) {
            for (Bee bee : beeAlgorithm.getGlobalBestBees()) {
                if (!prev.equals(bee.toString())) {
                    prev = bee.toString();
                    data.add(bee.toString());
                }
            }
        }

        return data;
    }

    private static String printRealmMatrix(RealMatrix matrix) {
        String k = matrix.toString().replace("Array2DRowRealMatrix", "").replaceAll("},", "},\n");
        return k;
    }
}
