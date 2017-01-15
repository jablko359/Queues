package view.bees;

import javafx.application.Application;
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

    Button runBeesAlgorithm;
    Button showParamsLayout;
    Button fileChooseButton;
    BeesPresenter beesPresenter;

    HBox editBeeParams;

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        editBeeParams.setVisible(false);


        box.getChildren().add(editBeeParams);

        //additional config
//        HBox iterations = new HBox();
//        Label iterationsLAbel = new Label("Number of iterations");
//        TextField iterationsField = new TextField(String.valueOf(BeeAlgorithm.ITERATIONS_NUMBER));
//        iterationsField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.length() > 0) try {
//                BeeAlgorithm.ITERATIONS_NUMBER = Integer.parseInt(newValue);
//                System.out.println("ITERATIONS_NUMBER set to " + newValue);
//            } catch (Exception x) {
//            }
//        });
//        iterations.getChildren().addAll(iterationsLAbel, iterationsField);
//        box.getChildren().add(iterations);
//
//        HBox channels = new HBox();
//        Label channelMins = new Label("Min number of channels");
//        TextField minChannelField = new TextField(String.valueOf(BeeAlgorithm.MIN_CHANNEL));
//        minChannelField.textProperty().addListener((oo, o, newValue) -> {
//            if (newValue.length() > 0) try {
//                BeeAlgorithm.MIN_CHANNEL = Integer.parseInt(newValue);
//                System.out.println("MIN_CHANNEL set to " + newValue);
//            } catch (Exception x) {
//            }
//        });
//        Label channelMax = new Label("Max number of channels");
//        TextField maxChannelField = new TextField(String.valueOf(BeeAlgorithm.MAX_CHANNEL));
//        minChannelField.textProperty().addListener((xxx, xx, newValue) -> {
//            if (newValue.length() > 0) try {
//                BeeAlgorithm.MAX_CHANNEL = Integer.parseInt(newValue);
//                System.out.println("MAX_CHANNEL set to " + newValue);
//            } catch (Exception x) {
//
//            }
//        });
//        channels.getChildren().addAll(channelMins, minChannelField, channelMax, maxChannelField);
//        box.getChildren().add(iterations);
//
//        Label TTLable = new Label("TTL");
//        TextField ttlField = new TextField(String.valueOf(BeeAlgorithm.TTL));
//        HBox ttlBo = new HBox();
//        ttlField.textProperty().addListener((o, oo, nev) -> {
//            if (nev.length() > 0) try {
//                BeeAlgorithm.TTL = Integer.parseInt(nev);
//                System.out.println("TTL set to " + nev);
//            } catch (Exception x) {
//
//            }
//        });
//        ttlBo.getChildren().addAll(TTLable, ttlField);
//        box.getChildren().add(ttlBo);

        //primary parameters

        showParamsLayout = new Button("Show system parameters");
        showParamsLayout.setOnAction(x -> {
            if (beesPresenter == null || beesPresenter.getQueue() == null) return;
            new QueueParamsLayout(beesPresenter.getQueue());
        });
        runBeesAlgorithm = new Button("Run");
        runBeesAlgorithm.setOnAction(c -> {
            BeeAlgorithm beeAlgorithm = null;
            try {
                beeAlgorithm = new BeeAlgorithm(
                        beesPresenter.getQueue(),
                        Integer.valueOf(e_bestSolutionsNumber.getText()),
                        Integer.valueOf(e_exclusiveSolutionsNumber.getText()),
                        Integer.valueOf(e_totalSolutionsNumber.getText()),
                        Integer.valueOf(e_bestSolutionsNeighberhoodNumber.getText()),
                        Integer.valueOf(e_exclusiveSolutionsNeighberhoodNumber.getText())
                );
            } catch (NumberFormatException x) {
                showError("Invalid parameters!");
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
        runBeesAlgorithm.setVisible(false);
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
        editBeeParams.setVisible(true);
        runBeesAlgorithm.setVisible(true);

    }
}
