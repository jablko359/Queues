package view;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import queue.graph.NodeData;
import queue.systems.SystemType;

import java.util.ArrayList;
import java.util.List;


/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 07 sty 2017.
 * 00 : 03
 */
public class SystemCreatorDialog {

    DataCallback<NodeData> callback;

    NodeData currentNodeData = new NodeData();

    int nextPosition;

    public SystemCreatorDialog(int nextPosition, DataCallback<NodeData> callback) {
        this.callback = callback;
        this.nextPosition = nextPosition;
    }

    public void show() {
        Dialog<NodeData> dialog = new Dialog<>();

        ButtonType okButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        VBox rootView = new VBox();

        HBox targetIdLayout = new HBox();
        TextField targetIdTextField = new TextField(String.valueOf(nextPosition));
        targetIdLayout.getChildren().add(new Label("id"));
        targetIdLayout.getChildren().add(targetIdTextField);
        rootView.getChildren().add(targetIdLayout);

        HBox serviceRatio = new HBox();
        serviceRatio.getChildren().add(new Label("ServiceRatio"));
        TextField serviceRatioTextField = new TextField("8");
        serviceRatio.getChildren().add(serviceRatioTextField);
        rootView.getChildren().add(serviceRatio);

        List<String> systemNames = new ArrayList<>();
        for (SystemType type : SystemType.values()) {
            systemNames.add(type.name());
        }
        ChoiceBox<String> systemType = new ChoiceBox<>(FXCollections.observableArrayList(systemNames));
        systemType.valueProperty().setValue("FIFO");

        rootView.getChildren().add(systemType);

        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(x -> {
            if (x == okButton) {

                String id = targetIdTextField.getText();
                String serviceRatioSelected = serviceRatioTextField.getText();
                String systemTypeSelected = systemType.valueProperty().get();

                if (!systemTypeSelected.equals("FIFO")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WTF");
                    alert.setHeaderText("Error occurred");
                    alert.setContentText("System type " + systemTypeSelected + " not yet supported");
                    alert.showAndWait();
                    return null;
                }

                currentNodeData.setM(Integer.valueOf(id));
                currentNodeData.setSystemType(SystemType.valueOf(systemTypeSelected));
                currentNodeData.setMi(Double.parseDouble(serviceRatioSelected));

                return currentNodeData;
            } else return null;
        });

        dialog.showAndWait().ifPresent(edgeData -> {
            if (callback != null) callback.onReceive(edgeData);
            dialog.close();
        });

    }
}
