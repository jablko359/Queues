package view;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import queue.Data;
import queue.graph.EdgeData;


/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 07 sty 2017.
 * 00 : 03
 */
public class EdgeCreatorDialog {

    DataCallback<EdgeData> newEdgeCallback;

    EdgeData currentEdge = new EdgeData();
    int nextId;

    public EdgeCreatorDialog(int nextId, DataCallback<EdgeData> newEdgeCallback) {
        this.newEdgeCallback = newEdgeCallback;
        this.nextId = nextId;
    }

    public void show() {
        Dialog<EdgeData> dialog = new Dialog<>();

        ButtonType okButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        VBox rootView = new VBox();

        HBox sourceIdLayout = new HBox();
        TextField sourceIdTextField = new TextField(String.valueOf(nextId - 1));
        sourceIdLayout.getChildren().add(new Label("sourceId"));
        sourceIdLayout.getChildren().add(sourceIdTextField);
        rootView.getChildren().add(sourceIdLayout);

        HBox targetIdLayout = new HBox();
        TextField targetIdTextField = new TextField(String.valueOf(nextId));
        targetIdLayout.getChildren().add(new Label("targetId"));
        targetIdLayout.getChildren().add(targetIdTextField);
        rootView.getChildren().add(targetIdLayout);


        HBox personName = new HBox();
        TextField personNameTextField = new TextField("Student");
        personName.getChildren().add(new Label("Person"));
        personName.getChildren().add(personNameTextField);
        rootView.getChildren().add(personName);

        HBox personValue = new HBox();
        TextField personValueTextField = new TextField("0.5");
        personValue.getChildren().add(new Label("Value"));
        personValue.getChildren().add(personValueTextField);
        rootView.getChildren().add(personValue);


        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(clickedButton -> {
            if (clickedButton == okButton) {

                String targetId = targetIdTextField.getText();
                String sourceId = sourceIdTextField.getText();
                String person = personNameTextField.getText();
                String value = personValueTextField.getText();

                currentEdge.setTargetId(targetId);
                currentEdge.setSourceId(sourceId);
                Data data = new Data();
                data.setValue(person, Double.parseDouble(value));
                currentEdge.setProbabilities(data);
                return currentEdge;
            } else return null;
        });

        dialog.showAndWait().ifPresent(edgeData -> {
            if (newEdgeCallback != null) newEdgeCallback.onReceive(edgeData);
            dialog.close();
        });

    }
}
