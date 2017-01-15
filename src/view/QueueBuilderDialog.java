package view;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import queue.Data;
import queue.exceptions.IncorrectUtilizationException;
import queue.exceptions.InvalidNetworkException;
import queue.exceptions.QueueException;
import queue.QueueBuilder;
import queue.QueueNetwork;
import queue.graph.EdgeData;
import queue.graph.Edges;
import queue.graph.NodeData;
import queue.graph.QueueSerialization;
import view.GenericListRenderer.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Marczak on 2017-01-03.
 */
public class QueueBuilderDialog {


    public interface QueueNetworkCallback {
        void onReceived(QueueNetwork network);
    }

    final QueueNetworkCallback queueNetworkCallback;
    List<EdgeData> currentEdges = new ArrayList<>();
    List<NodeData> currentSystems = new ArrayList<>();
    QueueSerialization currentQueueSerialization = new QueueSerialization() {{
        //setInputSystemId("1");
        Data data = new Data();
        data.setValue("Student",1.0);
        setClientLambdas(data);
    }};

    public QueueBuilderDialog(QueueNetworkCallback queueNetworkCallback) {
        this.queueNetworkCallback = queueNetworkCallback;
    }

    public void show() {
        Dialog<QueueNetwork> dialog = new Dialog<>();

        ButtonType okButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        dialog.setTitle("Build new Network queue");


        VBox edgesPane = new VBox();
        VBox systemsPane = new VBox();

        GenericListRenderer<EdgeData> edgesCollectionProxy = new GenericListRenderer<>(new Callback<EdgeData>() {
                    @Override
                    public Node recycle(EdgeData item) {
                        Map<String, Double> txt = item.getProbabilities().getMapValues();
                        StringBuilder sb = new StringBuilder();
                        for (String s : txt.keySet()) {
                            sb.append(s).append(" : ").append(txt.get(s)).append('\n');
                            sb.append(item.getSourceId() + "->" + item.getTargetId());
                        }
                        Label label = new Label(sb.toString());
                        return label;
                    }
                });


        GenericListRenderer<NodeData> systemsCollectionProxy = new GenericListRenderer<>(new Callback<NodeData>() {

            @Override
            public Node recycle(NodeData item) {
                String systemTypeName = item.getSystemType().name();
                double ratio = item.getMi();
                Label label = new Label(systemTypeName + ", " + ratio);
                return label;
            }
        });


        Button addEdgeButton = new Button("Add edge");
        addEdgeButton.setOnAction(c -> new EdgeCreatorDialog(edgesCollectionProxy.nextId(),
                data -> {
                    edgesCollectionProxy.add(data);
                    currentEdges.add(data);

                }).show()
        );
        edgesPane.getChildren().add(addEdgeButton);
        edgesCollectionProxy.setup(edgesPane, currentEdges);

        Button addSystemButton = new Button("Add system");
        addSystemButton.setOnAction(c -> new SystemCreatorDialog(systemsCollectionProxy.nextId(),
                data -> {
                    systemsCollectionProxy.add(data);
                    currentSystems.add(data);
                }).show());

        systemsPane.getChildren().add(addSystemButton);
        systemsCollectionProxy.setup(systemsPane, currentSystems);


        HBox rootView = new HBox();
        rootView.getChildren().addAll(edgesPane, systemsPane);

        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(clickedButton -> (clickedButton == okButton) ? buildQueue() : null);

        dialog.showAndWait().ifPresent(createdQueue -> {
            if (queueNetworkCallback != null) queueNetworkCallback.onReceived(createdQueue);
            dialog.close();
        });
    }

    private QueueNetwork buildQueue() {

        Edges edges = new Edges();
        edges.setEdges(currentEdges);
        currentQueueSerialization.setEdges(edges);

        HashMap<String, NodeData> systems = new HashMap<>();
        for (int i = 0; i < currentSystems.size(); i++) {
            int index = i + 1;
            systems.put(String.valueOf(index), currentSystems.get(i));
        }

        currentQueueSerialization.setSystems(systems);

        QueueBuilder builder = new QueueBuilder(currentQueueSerialization);
        try {
            QueueNetwork network = builder.buildQueue();
            return network;
        } catch (QueueException | InvalidNetworkException ex){
            ex.printStackTrace();
            return null;
        }


    }

}
