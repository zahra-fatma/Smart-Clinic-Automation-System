package com.dsaproj.ui;

import com.dsaproj.ui.model.PatientFx;
import com.dsaproj.ui.service.PatientServiceHttp;
import com.dsaproj.ui.util.Toast;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainApp extends Application {

    private final PatientServiceHttp service = new PatientServiceHttp();
    private final ObservableList<PatientFx> patients = FXCollections.observableArrayList();

    private Label waitingLabel = new Label("Waiting: 0");
    private Label attendedLabel = new Label("Attended: 0");
    private Label highPriorityLabel = new Label("High Priority: 0");

    private PieChart chart = new PieChart();

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏥 Hospital Queue Management System");

        // ================= PATIENT TABLE =================
        TableView<PatientFx> patientTable = new TableView<>(patients);
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PatientFx, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<PatientFx, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<PatientFx, Number> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> data.getValue().ageProperty());

        TableColumn<PatientFx, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(data -> data.getValue().priorityProperty());

        TableColumn<PatientFx, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        TableColumn<PatientFx, String> createdAtCol = new TableColumn<>("Created At");
        createdAtCol.setCellValueFactory(data -> data.getValue().createdAtProperty());

        patientTable.getColumns().addAll(idCol, nameCol, ageCol, priorityCol, statusCol, createdAtCol);

        // ================= BUTTONS =================
        Button attendBtn = new Button("🩺 Attend Next Patient");
        attendBtn.setOnAction(e -> {
            PatientFx attended = service.attendNextPatient();
            if (attended != null) {
                Toast.show(stage, "Now attending: " + attended.getName(), 3);
                loadPatients();
            }
        });

        Button addBtn = new Button("➕ Add Patient");
        addBtn.setOnAction(e -> openAddPatientDialog(stage));

        ToolBar toolbar = new ToolBar(attendBtn, addBtn);

        // ================= RIGHT PANEL =================
        VBox statsBox = new VBox(10, waitingLabel, attendedLabel, highPriorityLabel);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        chart.setTitle("Patient Priorities");

        VBox rightPanel = new VBox(15, statsBox, chart);
        rightPanel.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(patientTable);
        root.setRight(rightPanel);

        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

        // 🔄 Auto refresh every 5s
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> loadPatients());
            }
        }, 0, 5000);
    }

    private void loadPatients() {
        List<PatientFx> list = service.getPatients();
        patients.setAll(list);

        long waiting = list.stream().filter(p -> "WAITING".equalsIgnoreCase(p.getStatus())).count();
        long attended = list.stream().filter(p -> "ATTENDED".equalsIgnoreCase(p.getStatus())).count();
        long high = list.stream().filter(p -> "HIGH".equalsIgnoreCase(p.getPriority())).count();
        long medium = list.stream().filter(p -> "MEDIUM".equalsIgnoreCase(p.getPriority())).count();
        long low = list.stream().filter(p -> "LOW".equalsIgnoreCase(p.getPriority())).count();

        waitingLabel.setText("Waiting: " + waiting);
        attendedLabel.setText("Attended: " + attended);
        highPriorityLabel.setText("High Priority: " + high);

        chart.setData(FXCollections.observableArrayList(
                new PieChart.Data("HIGH", high),
                new PieChart.Data("MEDIUM", medium),
                new PieChart.Data("LOW", low)
        ));
    }

    private void openAddPatientDialog(Stage stage) {
        Dialog<PatientFx> dialog = new Dialog<>();
        dialog.setTitle("➕ Add Patient");

        TextField nameField = new TextField();
        Spinner<Integer> ageField = new Spinner<>(0, 120, 30);
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("HIGH", "MEDIUM", "LOW");
        priorityBox.setValue("MEDIUM");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Priority:"), 0, 2);
        grid.add(priorityBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK && !nameField.getText().isBlank()) {
                return service.addPatient(
                        nameField.getText(),
                        ageField.getValue(),
                        priorityBox.getValue()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(p -> {
            Toast.show(stage, "✅ Patient " + p.getName() + " added!", 3);
            loadPatients();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}