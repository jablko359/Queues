package view.bees;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import queue.QueueNetwork;
import queue.bees.Bee;
import queue.bees.BeeAlgorithm;

import java.util.List;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 15 sty 2017.
 * 00 : 24
 */
public class BeesResultDialog {

    public BeesResultDialog(BeeAlgorithm beeAlgorithm) {
        Dialog<Boolean> dialog = new Dialog<>();

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        //dialog.setTitle("Build new Network queue");

        VBox rootView = new VBox();

        QueueNetwork network = beeAlgorithm.getQueueNetwork();
        List<Bee> bees = beeAlgorithm.getGlobalBestBees();


        Bee firstBee = bees.get(0);

        double minY = firstBee.getQuality(), maxY = firstBee.getQuality();
        for (Bee b : bees) {
            if (b.getQuality() < minY) {
                minY = b.getQuality();
            }
            if (b.getQuality() > maxY) {
                maxY = b.getQuality();
            }
        }

        final NumberAxis xAxis = new NumberAxis("Nr. iteracji", 0, bees.size() + 1, 10);
        final NumberAxis yAxis = new NumberAxis("Wartość funkcji celu", minY - 1, maxY + 1, 1);
        final AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setMinHeight(600);
        areaChart.setMinWidth(800);
        areaChart.setTitle("Algorytm pszczeli");

        XYChart.Series beeSeries = new XYChart.Series();
        beeSeries.setName("Wartość funkcji celu");

        int iteration = 1;
        for (Bee bee : bees) {
            beeSeries.getData().add(new XYChart.Data(iteration, bee.getQuality()));
            ++iteration;
        }

        areaChart.getData().addAll(beeSeries);
        rootView.getChildren().add(areaChart);

        Button showParamsBtn = new Button("Show queue parameters");
        showParamsBtn.setOnAction(x -> {
            new QueueParamsLayout(network);
        });

        rootView.getChildren().add(showParamsBtn);

        dialog.getDialogPane().setContent(rootView);

        dialog.setResultConverter(clickedButton -> null);

        dialog.showAndWait().ifPresent(createdQueue -> dialog.close());
    }


}
