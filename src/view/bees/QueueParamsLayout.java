package view.bees;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import queue.QueueNetwork;

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
    public QueueParamsLayout(QueueNetwork network) {
        Dialog<Boolean> dialog = new Dialog<>();

        VBox rootView = new VBox();

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        ScrollPane pane = new ScrollPane(newParamsLayout(network));

        pane.setMaxHeight(400);

        rootView.getChildren().add(pane);

        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(clickedButton -> null);

        dialog.showAndWait().ifPresent(createdQueue -> dialog.close());
    }

    public static Node newParamsLayout(QueueNetwork network) {
        VBox node = new VBox();

        for (String s : produceInfo(network)) {
            node.getChildren().add(new Label(s));
        }

        return node;
    }

    private static List<String> produceInfo(QueueNetwork network) {
        List<String> data = new ArrayList<>();

        data.add("K: " + network.getK());

        String[] systems = new String[]{"dzienne", "zaoczne", "doktoranckie", "socjalne", "dziekan"};
        String[] clients = new String[]{"Student", "Pracownik"};

        for (String sys : systems) {
            for (String cli : clients) {
                try {
                    data.add("System: " + sys + ", klient: " + cli + ", K = " + network.getK(cli, sys));
                    data.add("System: " + sys + ", klient: " + cli + ", Q = " + network.getQ(sys, cli));
                    data.add("System: " + sys + ", klient: " + cli + ", LambdaT = " + network.getLambdaT(sys, cli));
                    data.add("System: " + sys + ", klient: " + cli + ", T = " + network.getT(sys, cli));
                    data.add("System: " + sys + ", klient: " + cli + ", W = " + network.getW(sys, cli));
                } catch (IllegalStateException x) {
                }
            }
        }


        return data;
    }
}
