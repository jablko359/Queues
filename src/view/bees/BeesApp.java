package view.bees;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import queue.bees.BeeAlgorithm;
import view.App;
import view.Results;

import java.io.File;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 14 sty 2017.
 * 13 : 51
 */
public class BeesApp extends Application implements BeesCallbacks {

    public static void main(String[] args) {
        launch(args);
    }

    Button runBeesAlgorithm;
    Button showParamsLayout;
    Button fileChooseButton;
    BeesPresenter beesPresenter;

    HBox editBeeParams;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);

        beesPresenter = new BeesPresenter(this);
        BorderPane root = new BorderPane();

        VBox box = new VBox();

        editBeeParams = new HBox();

        addInputFileButton(primaryStage, box);

        TextField e_bestSolutionsNumber = new TextField("2");
        TextField e_exclusiveSolutionsNumber = new TextField("1");
        TextField e_totalSolutionsNumber = new TextField("5");
        TextField e_bestSolutionsNeighberhoodNumber = new TextField("1");
        TextField e_exclusiveSolutionsNeighberhoodNumber = new TextField("2");


        VBox textFields = new VBox();

        textFields.getChildren().add(e_bestSolutionsNumber);//= 2;
        textFields.getChildren().add(e_exclusiveSolutionsNumber);//= 1;
        textFields.getChildren().add(e_totalSolutionsNumber);//= 5;
        textFields.getChildren().add(e_bestSolutionsNeighberhoodNumber);//= 1;
        textFields.getChildren().add(e_exclusiveSolutionsNeighberhoodNumber);//= 2;


        Label l_bestSolutionsNumber = new Label("best solution number");
        Label l_exclusiveSolutionsNumber = new Label("exclusive solution number");
        Label l_totalSolutionsNumber = new Label("total solutions number");
        Label l_bestSolutionsNeighberhoodNumber = new Label("best solution neighbourhood number");
        Label l_exclusiveSolutionsNeighberhoodNumber = new Label("exclusive solutions neighbourhood number");

        l_bestSolutionsNumber.setMinHeight(27);
        l_exclusiveSolutionsNumber.setMinHeight(27);
        l_totalSolutionsNumber.setMinHeight(27);
        l_bestSolutionsNeighberhoodNumber.setMinHeight(27);
        l_exclusiveSolutionsNeighberhoodNumber.setMinHeight(27);

        VBox labels = new VBox();

        labels.getChildren().add(l_bestSolutionsNumber);//= 2;
        labels.getChildren().add(l_exclusiveSolutionsNumber);//= 1;
        labels.getChildren().add(l_totalSolutionsNumber);//= 5;
        labels.getChildren().add(l_bestSolutionsNeighberhoodNumber);//= 1;
        labels.getChildren().add(l_exclusiveSolutionsNeighberhoodNumber);//= 2;

        editBeeParams.getChildren().addAll(labels, textFields);

//      editBeeParams.setVisible(false);

        box.getChildren().add(editBeeParams);


        VBox configLabels = new VBox();
        VBox configTextFields = new VBox();

        //additional config
        Label iterationsLAbel = new Label("Number of iterations");
        iterationsLAbel.setMinHeight(27);
        TextField iterationsField = new TextField(String.valueOf(BeeAlgorithm.ITERATIONS_NUMBER));


        Label channelMins = new Label("Min number of channels");
        channelMins.setMinHeight(27);
        TextField minChannelField = new TextField(String.valueOf(BeeAlgorithm.MIN_CHANNEL));

        Label channelMax = new Label("Max number of channels");
        channelMax.setMinHeight(27);
        TextField maxChannelField = new TextField(String.valueOf(BeeAlgorithm.MAX_CHANNEL));


        Label TTL_label = new Label("TTL");
        TTL_label.setMinHeight(27);
        TextField ttlField = new TextField(String.valueOf(BeeAlgorithm.TTL));

        configLabels.getChildren().addAll(iterationsLAbel, channelMins, channelMax, TTL_label);
        configTextFields.getChildren().addAll(iterationsField, minChannelField, maxChannelField, ttlField);

        HBox configBox = new HBox();

        configBox.getChildren().addAll(configLabels, configTextFields);
        box.getChildren().add(configBox);
        //primary parameters


        Label fCeluLAbel = new Label("Objective function parameters: ");
        fCeluLAbel.setPadding(new Insets(10, 10, 10, 10));
        HBox objectiveFunctionLayout = new HBox();
        Label averageTimeLabel = new Label("average time coefficient");
        averageTimeLabel.setMinHeight(27);

        Label nrOfChannelsLabel = new Label("number of channels coefficient");
        nrOfChannelsLabel.setMinHeight(27);

        TextField averageTimeField = new TextField(String.valueOf(BeeAlgorithm.AVERAGE_TIME_COEFFICIENT));
        TextField numberOfChannelsField = new TextField(String.valueOf(BeeAlgorithm.NUMBER_OF_CHANNELS_COEFFICIENT));
        VBox objlabels = new VBox();
        VBox objFields = new VBox();
        objlabels.getChildren().addAll(averageTimeLabel, nrOfChannelsLabel);
        objFields.getChildren().addAll(averageTimeField, numberOfChannelsField);

        objectiveFunctionLayout.getChildren().addAll(objlabels, objFields);
        box.getChildren().addAll(fCeluLAbel, objectiveFunctionLayout);


        showParamsLayout = new Button("Show system parameters");
        showParamsLayout.setOnAction(x -> {
            if (beesPresenter == null || beesPresenter.getQueue() == null) {
                showError("Choose file first!");
            } else new QueueParamsLayout(beesPresenter.getQueue(), null);
        });
        runBeesAlgorithm = new Button("Run");
        runBeesAlgorithm.setOnAction(c -> {

            BeeAlgorithm beeAlgorithm = null;

            try {

                BeeAlgorithm.AVERAGE_TIME_COEFFICIENT = Double.valueOf(averageTimeField.getText());
                BeeAlgorithm.NUMBER_OF_CHANNELS_COEFFICIENT = Double.valueOf(numberOfChannelsField.getText());

                BeeAlgorithm.ITERATIONS_NUMBER = Integer.valueOf(iterationsField.getText());
                BeeAlgorithm.MAX_CHANNEL = Integer.valueOf(maxChannelField.getText());
                BeeAlgorithm.MIN_CHANNEL = Integer.valueOf(minChannelField.getText());
                BeeAlgorithm.TTL = Integer.valueOf(ttlField.getText());

                beeAlgorithm = new BeeAlgorithm(
                        beesPresenter.getQueue(),
                        Integer.valueOf(e_bestSolutionsNumber.getText()),
                        Integer.valueOf(e_exclusiveSolutionsNumber.getText()),
                        Integer.valueOf(e_totalSolutionsNumber.getText()),
                        Integer.valueOf(e_bestSolutionsNeighberhoodNumber.getText()),
                        Integer.valueOf(e_exclusiveSolutionsNeighberhoodNumber.getText())
                );
            } catch (NumberFormatException x) {
                showError("Invalid parameters! Check if parameters " +
                        "(iterations number, min,max channel, TTL)" +
                        " are valid");
            }
            if (beeAlgorithm != null) {
                //initiate beeAlgorithm
                beeAlgorithm.initialize();

                //calculate
                beeAlgorithm.calculate();

                new BeesResultDialog(beeAlgorithm);
            }
        });

        box.getChildren().add(showParamsLayout);
        box.getChildren().add(runBeesAlgorithm);
//        runBeesAlgorithm.setVisible(false);
        root.setCenter(box);

        primaryStage.setTitle("Bees");

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }


    private void addInputFileButton(Stage primaryStage, Pane parent) {
        fileChooseButton = new Button();
        fileChooseButton.setText("Open input file");
        fileChooseButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("input file");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (beesPresenter != null) beesPresenter.onFileChosen(file);

            if (file != null) fileChooseButton.setText("Current file: " + file.getName());

        });
        parent.getChildren().add(fileChooseButton);
    }

    @Override
    public void showError(String errorMsg) {
        System.out.println("ERROR:" + errorMsg);
        App.showErr(errorMsg);
    }

    @Override
    public void showResults(Results results) {

    }

    @Override
    public void showEditBeeParameters() {
        System.out.println("showEditBeeParameters");
//        editBeeParams.setVisible(true);
//        runBeesAlgorithm.setVisible(true);

    }
}
