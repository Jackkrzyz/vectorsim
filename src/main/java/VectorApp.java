import javafx.application.Application;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.util.Optional;

import java.lang.IO;

public class VectorApp extends Application {

    @Override
    public void start(Stage stage) { // stage = desktop window

        ListView<Vector3> vectorList = new ListView<>();
        Label title = new Label("Create Vector");

        TextField xInput = new TextField();
        TextField yInput = new TextField();
        TextField zInput = new TextField();

        xInput.setPromptText("X");
        yInput.setPromptText("Y");
        zInput.setPromptText("Z");

        Button createButton = new Button("Create Vector");
        createButton.setOnAction(event -> {
            double x = Double.parseDouble(xInput.getText());
            double y = Double.parseDouble(yInput.getText());
            double z = Double.parseDouble(zInput.getText());

            Vector3 vector = new Vector3(x, y, z);
            vectorList.getItems().add(vector);
            IO.println(
                    "<" +
                    vector.getX() + ", " +
                    vector.getY() + ", " +
                    vector.getZ() +
                    ">"

            );
        });

        Button addButton = new Button("+");
        Button subtractButton = new Button("-");
        Button dotButton = new Button("·");
        Button crossButton = new Button("×");
        Button magnitudeButton = new Button("|v|");

        Button angleButton = new Button("Δθ");

        Label resultLabel = new Label("Result:");
        resultLabel.setVisible(false);
        resultLabel.setManaged(false);


        magnitudeButton.setOnAction(event -> {
            Vector3 selected = vectorList.getSelectionModel().getSelectedItem();
            if (selected != null) { noVectorSelectedAlert(); return; }
        });

        addButton.setOnAction(event -> {
            Vector3 a = vectorList.getSelectionModel().getSelectedItem();
            if (a == null) { noVectorSelectedAlert(); return; }

            chooseSecondVector(vectorList).ifPresent(b -> {
                Vector3 result = a.add(b);
                vectorList.getItems().add(result);
            });
        });

        subtractButton.setOnAction(event -> {
            Vector3 a = vectorList.getSelectionModel().getSelectedItem();
            if (a == null) { noVectorSelectedAlert(); return; }

            chooseSecondVector(vectorList).ifPresent(b -> {
                Vector3 result = a.subtract(b);
                vectorList.getItems().add(result);
            });
        });


        HBox operations = new HBox(
                10,
                addButton,
                subtractButton,
                dotButton,
                crossButton,
                magnitudeButton,
                angleButton
        );
        VBox root = new VBox(
                10,
                title,
                xInput,
                yInput,
                zInput,
                createButton,
                operations,
                resultLabel,
                vectorList
        ); // stack elements vertically with 10px between them



        Scene scene = new Scene(root, 400, 400);


        stage.setTitle("Vector Simulator");
        stage.setScene(scene);
        stage.show();
    }
    void noVectorSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Vector Selected");
        alert.setHeaderText(null);
        alert.setContentText("Select a vector first.");
        alert.showAndWait();
        return;
    }
    private Optional<Vector3> chooseSecondVector(ListView<Vector3> vectorList) {
        ChoiceDialog<Vector3> dialog = new ChoiceDialog<>();

        dialog.getItems().addAll(vectorList.getItems());
        dialog.setTitle("Select Vector");
        dialog.setHeaderText("Choose second vector");

        return dialog.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
