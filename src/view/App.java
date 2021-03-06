package view;

/**
 * Queues
 *
 * @author Lukasz Marczak
 * @since 30 gru 2016.
 * 14 : 15
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application implements AppCallbacks {

    public static void main(String[] args) {
        launch(args);
    }

    AppPresenter presenter;

    ProgressBar progressIndicator;

    Button fileChooseButton;

    Button createNewQueueNetworkButton;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");
        presenter = new AppPresenter(this);

        BorderPane root = new BorderPane();

        VBox box = new VBox();

        addInputFileButton(primaryStage, box);
        addProgressBar(primaryStage, box);

        addQueueBuilderButton(box);
        root.setCenter(box);

        primaryStage.setTitle("BCMP");

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private void addQueueBuilderButton(VBox parent) {
        createNewQueueNetworkButton = new Button("Create new queue network");
        createNewQueueNetworkButton
                .setOnAction(c -> new QueueBuilderDialog(queueNetwork -> {

                    double K = queueNetwork.getK();

                    System.out.println("Performance measure: " + K);
                }).show());
        parent.getChildren().add(createNewQueueNetworkButton);

    }

    private void addProgressBar(Stage primaryStage, Pane parent) {
        progressIndicator = new ProgressBar(0);
        progressIndicator.setVisible(false);
        progressIndicator.setMinSize(80, 20);
        progressIndicator.setPrefSize(80, 20);
        parent.getChildren().add(progressIndicator);
    }

    private void addInputFileButton(Stage primaryStage, Pane parent) {
        fileChooseButton = new Button();
        fileChooseButton.setText("Open input file");
        fileChooseButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open input data");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                presenter.onFileChosen(file);
            }
        });
        parent.getChildren().add(fileChooseButton);
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (presenter != null) {
            presenter.stop();
            presenter = null;
        }
        System.out.println("stop!");
    }

    @Override
    public void showResults(Results results) {
        System.out.println("RESULTS : " + String.valueOf(results));
    }

    @Override
    public void showProgress(double value) {
        if (progressIndicator == null) return;

        progressIndicator.setVisible(true);
        progressIndicator.setProgress(value);
    }

    @Override
    public void hideProgress() {
        progressIndicator.setVisible(false);
    }

    @Override
    public void showError(String errorMsg) {
        showErr(errorMsg);
    }

    public static void showErr(String errorMsg) {
        System.out.println("Error occurred");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("WTF");
//        alert.setHeaderText("Error occurred");
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    @Override
    public void showSomethingElse() {
        System.out.println("SOMETHING ELSE");
    }
}
