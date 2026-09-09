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
import javafx.collections.ListChangeListener;

import javafx.scene.SubScene;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;

import javafx.scene.layout.BorderPane;

import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.geometry.Point3D;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import javafx.scene.transform.Rotate;

public class VectorApp extends Application {

    private final Group vectorArrows = new Group();
    private int colorIndex = 0;
    private Color nextVectorColor() {
        double hue = (colorIndex * 137.5) % 360;
        colorIndex++;

        return Color.hsb(hue, 0.85, 0.9);
    }

    @Override
    public void start(Stage stage) { // stage = desktop window

        ListView<Vector3> vectorList = new ListView<>();
        vectorList.getItems().addListener(
            (ListChangeListener<Vector3>) change -> {

                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Vector3 vector : change.getAddedSubList()) {
                            Color color = nextVectorColor();

                            vectorArrows.getChildren().add(
                                    createVectorArrow(vector, color)
                            );
                        }
                    }
                }
            }
        );
        Label title = new Label("Create Vector");

        TextField xInput = new TextField();
        TextField yInput = new TextField();
        TextField zInput = new TextField();

        xInput.setPromptText("X");
        yInput.setPromptText("Y");
        zInput.setPromptText("Z");

        Button createButton = new Button("Create Vector");
        createButton.setOnAction(event -> {

            try {
                double x = Double.parseDouble(xInput.getText());
                double y = Double.parseDouble(yInput.getText());
                double z = Double.parseDouble(zInput.getText());
                Vector3 vector = new Vector3(x, y, z);
                vectorList.getItems().add(vector);
            } catch (NumberFormatException e) {
                invalidInputAlert();
            }
            xInput.clear();
            yInput.clear();
            zInput.clear();
        
            

            
        });

        Button addButton = new Button("+");
        Button subtractButton = new Button("-");
        Button dotButton = new Button("·");
        Button crossButton = new Button("×");
        Button magnitudeButton = new Button("|v|");
        Button angleButton = new Button("Δθ");
        Button normalizeButton = new Button("v̂");

        Label resultLabel = new Label("Result:");
        resultLabel.setVisible(false);
        resultLabel.setManaged(false);


        magnitudeButton.setOnAction(event -> {
            Vector3 selected = vectorList.getSelectionModel().getSelectedItem();
            if (selected == null) { noVectorSelectedAlert(); return; }

            resultLabel.setVisible(true);
            resultLabel.setManaged(true);
            resultLabel.setText("Magnitude: " + selected.magnitude());
        });

        normalizeButton.setOnAction(event -> {
            Vector3 selected = vectorList.getSelectionModel().getSelectedItem();
            if (selected == null) { noVectorSelectedAlert(); return; }

            Vector3 result = selected.normalize();
            vectorList.getItems().add(result);
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

        dotButton.setOnAction(event -> {
            Vector3 a = vectorList.getSelectionModel().getSelectedItem();
            if (a == null) { noVectorSelectedAlert(); return; }

            chooseSecondVector(vectorList).ifPresent(b -> {
                double result = a.dot(b);
                resultLabel.setVisible(true);
                resultLabel.setManaged(true);
                resultLabel.setText("Dot Product: " + result);

            });
        });

        crossButton.setOnAction(event -> {
            Vector3 a = vectorList.getSelectionModel().getSelectedItem();
            if (a == null) { noVectorSelectedAlert(); return; }

            chooseSecondVector(vectorList).ifPresent(b -> {
                Vector3 result = a.cross(b);
                vectorList.getItems().add(result);
            });
        });

        angleButton.setOnAction(event -> {
            Vector3 a = vectorList.getSelectionModel().getSelectedItem();
            if (a == null) { noVectorSelectedAlert(); return; }
            chooseSecondVector(vectorList).ifPresent(b -> {
                double result = a.angleBetween(b);
                resultLabel.setVisible(true);
                resultLabel.setManaged(true);
                resultLabel.setText("Angle: " + result + " rad, " + (Math.toDegrees(result)) + " deg");
                
            });
        });


        HBox operations = new HBox(
                10,
                addButton,
                subtractButton,
                dotButton,
                crossButton,
                magnitudeButton,
                angleButton,
                normalizeButton
        );
        VBox controls = new VBox(
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



        
        SubScene view3D = create3DView();
        
        BorderPane root = new BorderPane();
        root.setLeft(controls);
        root.setCenter(view3D);

        Scene scene = new Scene(root, 1000, 600);

        stage.setTitle("Vector Simulator");
        stage.setScene(scene);
        stage.show();
    }
    private void noVectorSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Vector Selected");
        alert.setHeaderText(null);
        alert.setContentText("Select a vector first.");
        alert.showAndWait();
    }
    private void invalidInputAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Vector Input");
        alert.setHeaderText(null);
        alert.setContentText("Please input only numbers for vector components");
        alert.showAndWait();
    }
    private Optional<Vector3> chooseSecondVector(ListView<Vector3> vectorList) {
        ChoiceDialog<Vector3> dialog = new ChoiceDialog<>();

        dialog.getItems().addAll(vectorList.getItems());
        dialog.setTitle("Select Vector");
        dialog.setHeaderText("Choose second vector");

        return dialog.showAndWait();
    }
    private Group createVectorArrow(Vector3 vector, Color color) {
        final double visualScale = 10;

        Group arrow = new Group();

        

        Vector3 endpoint = vector.scale(visualScale);
        double length = endpoint.magnitude();
        if (length == 0) { return arrow; }

        // positon = cylinder position which is defined by its midpoint
        Vector3 position = new Vector3(
            endpoint.getX() / 2,
            endpoint.getY() / 2,
            endpoint.getZ() / 2
        );

        /* a javafx cylinder naturally points along the y axis (<0,1,0>)
        / find target direction (unit vector)
        / find angle between target & y
        / find axis (<0,1,0> cross direction vector)
        / apply rotate
        */
        Vector3 targetDirection = vector.normalize();
        Vector3 yAxisVec = new Vector3(0,1,0);
        double rotationAngle = Math.toDegrees(targetDirection.angleBetween(yAxisVec));

        Vector3 rotationAxis = yAxisVec.cross(targetDirection);

        PhongMaterial material = new PhongMaterial(color);

        Cylinder shaft = new Cylinder(1, length);
        shaft.setMaterial(material);

        Sphere tip = new Sphere(1); // replace with custom cone mesh
        tip.setMaterial(material);

        Point3D axis = new Point3D(
            rotationAxis.getX(),
            rotationAxis.getY(),
            rotationAxis.getZ()
        );

        Rotate rotation = new Rotate(rotationAngle, axis);
        shaft.getTransforms().add(rotation);

        shaft.setTranslateX(position.getX());
        shaft.setTranslateY(position.getY());
        shaft.setTranslateZ(position.getZ());
        tip.setTranslateX(endpoint.getX());
        tip.setTranslateY(endpoint.getY());
        tip.setTranslateZ(endpoint.getZ());

        arrow.getChildren().addAll(shaft, tip);
        return arrow;
    }
    private SubScene create3DView() {
        Group world = new Group();
        Group objects = new Group();

        Rotate xRotate = new Rotate(-20, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(30, Rotate.Y_AXIS);

        objects.getTransforms().addAll(xRotate, yRotate);

        Cylinder xAxis = new Cylinder(.5, 1000);
        Cylinder yAxis = new Cylinder(.5, 1000);
        Cylinder zAxis = new Cylinder(.5, 1000);

        xAxis.setMaterial(new PhongMaterial(Color.RED));
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));

        xAxis.getTransforms().add(
                new Rotate(90, Rotate.Z_AXIS)
        );

        zAxis.getTransforms().add(
                new Rotate(90, Rotate.X_AXIS)
        );

        objects.getChildren().addAll(xAxis, yAxis, zAxis, vectorArrows);
        world.getChildren().add(objects);

        SubScene subScene = new SubScene(
                world,
                600,
                600,
                true,
                SceneAntialiasing.BALANCED
        );

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-500);

        world.getChildren().add(camera);
        subScene.setCamera(camera);
        subScene.setFill(Color.LIGHTGRAY);

        final double[] mousePosition = new double[2];

        subScene.setOnMousePressed(event -> {
            mousePosition[0] = event.getSceneX();
            mousePosition[1] = event.getSceneY();
        });

        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mousePosition[0];
            double deltaY = event.getSceneY() - mousePosition[1];

            yRotate.setAngle(yRotate.getAngle() + deltaX * 0.5);
            xRotate.setAngle(xRotate.getAngle() - deltaY * 0.5);

            mousePosition[0] = event.getSceneX();
            mousePosition[1] = event.getSceneY();
        });

        subScene.setOnScroll(event -> {
            double newZ = camera.getTranslateZ() + event.getDeltaY();

            newZ = Math.max(-2000, Math.min(-100, newZ));

            camera.setTranslateZ(newZ);
        });


        return subScene;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
