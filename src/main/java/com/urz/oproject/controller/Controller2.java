package com.urz.oproject.controller;


import com.jfoenix.controls.JFXNodesList;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class Controller2 implements Initializable {

    private final TaskService taskService;
    @FXML
    private VBox pnItems = null;
    @FXML
    private JFXNodesList nodesList;
    @FXML
    private Button btnOverview;
    @FXML
    private Label totalTask;
    @FXML
    private Label completedTask;
    @FXML
    private Label toDoTask;


    private FilteredList<Task> filteredList;
    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    @FXML
    TableView<Task> toDoTableView;
    @FXML
    TableView<Task> toDoTableView1;
    @FXML
    TableColumn<Task, Long> idColumn;
    @FXML
    TableColumn<Task, String> shortDescColumn;
    @FXML
    TableColumn<Task, String> shortDescColumn1;
    @FXML
    TableColumn<Task, String> longDesc;
    //    @FXML
//    TableColumn<Task, Boolean> taskStatus;
//    @FXML
//    TableColumn<Task, Boolean> importantStatus;
    @FXML
    private TableColumn<Task, String> editCol;
    @FXML
    private TableColumn<Task, String> editCol1;
    @FXML
    ObservableList<Task> taskList;
    @FXML
    ObservableList<Task> doneTaskList;

    @FXML
    private ListView listView;


    public Controller2(TaskService taskService) {
        this.taskService = taskService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOrders.toBack();
        pnlOverview.setStyle("-fx-background-color : white");
        pnlOverview.toFront();

        taskService.addTask(new Task("Visit John", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Cat food", "test long", false, false));
        taskService.addTask(new Task(6L, "test", "test long", false, false));
        taskService.addTask(new Task.TaskBuilder().shortDesc("test builder").longDesc("long test").taskStatus(true).importantStatus(false).build());

        taskList = FXCollections.observableList(taskService.getTasks());
        doneTaskList = FXCollections.observableList(taskService.getTasks());
        doneTaskList.clear();


        totalTask.setText(String.valueOf(taskService.getTasks().size()));

        shortDescColumn.setCellValueFactory(new PropertyValueFactory<>("shortDesc"));
        longDesc.setCellValueFactory(new PropertyValueFactory<>("longDesc"));

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory = (TableColumn<Task, String> param) -> {
            final TableCell<Task, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_SQUARE_ALT);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                            Task selectedTask = toDoTableView.getSelectionModel().getSelectedItem();
                            if (selectedTask != null) {
                                if (event.getClickCount() == 2) //Checking double click
                                {
                                    Stage dialog = new Stage();
                                    dialog.initModality(Modality.APPLICATION_MODAL);

                                    VBox dialogVbox = new VBox(20);
                                    Label label = new Label(selectedTask.getLongDesc());
                                    dialogVbox.getChildren().add(label);
                                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                                    dialog.setScene(dialogScene);
                                    dialog.show();
                                    System.out.println("You clicked on " + toDoTableView.getSelectionModel().getSelectedItem().getShortDesc());

                                }
                            }
                        });
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            Task task = toDoTableView.getSelectionModel().getSelectedItem();
                            taskList.remove(task);
                            toDoTableView.setItems(taskList);
                            doneTaskList.add(task);
                            completedTask.setText(String.valueOf(doneTaskList.size()));
                            toDoTask.setText(String.valueOf(taskList.size()));
                            if (!doneTaskList.isEmpty()){
                                toDoTableView1.setItems(doneTaskList);
                            }
                            //taskService.deleteTask(task.getId());
                            //refreshTable();

                        });


                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Task task = toDoTableView.getSelectionModel().getSelectedItem();
                            //   AddStudentController addStudentController = loader.getController();
                            //    addStudentController.setUpdate(true);
//                            addStudentController.setTextField(student.getId(), student.getName(),
//                                    student.getBirth().toLocalDate(), student.getAdress(), student.getEmail());
                            Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            //   dialog.initOwner(primaryStage);
                            VBox dialogVbox = new VBox(20);
                            dialogVbox.getChildren().add(new Text("This is a Dialog"));
                            Scene dialogScene = new Scene(dialogVbox, 300, 200);
                            dialog.setScene(dialogScene);
                            dialog.show();


                        });

                        HBox managebtn = new HBox( editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editCol.setCellFactory(cellFactory);
        shortDescColumn1.setCellValueFactory(new PropertyValueFactory<Task, String>("shortDesc"));

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory1 = (TableColumn<Task, String> param) -> {
            final TableCell<Task, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            //   dialog.initOwner(primaryStage);
                            VBox dialogVbox = new VBox(20);
                            HBox hbox = new HBox(10);
                            dialogVbox.getChildren().add(new Label("Do you want to permanently delete task?"));
                            Button yesButton = new Button("Yes");
                            Button noButton = new Button("No");
                            hbox.getChildren().add(yesButton);
                            hbox.getChildren().add(noButton);
                            dialogVbox.getChildren().add(hbox);
                            Scene dialogScene = new Scene(dialogVbox, 300, 200);
                            dialog.setScene(dialogScene);
                            dialog.show();
                            yesButton.setOnMouseClicked((MouseEvent event1) -> {
                                Task task = toDoTableView1.getSelectionModel().getSelectedItem();
                                doneTaskList.remove(task);
                                toDoTableView1.setItems(doneTaskList);
                                taskService.deleteTask(task.getId());
                                totalTask.setText(String.valueOf(taskService.getTasks().size()));
                                toDoTask.setText(String.valueOf(taskList.size()));

                                // totalTask.setText(String.valueOf(taskList.size()));
                                dialog.close();
                            });
                            noButton.setOnMouseClicked((MouseEvent event2) -> {
                                dialog.close();
                            });


                        });


                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editCol1.setCellFactory(cellFactory1);
        toDoTableView1.getStyleClass().add("noheader");
        toDoTableView.getStyleClass().add("noheader");
        toDoTableView.setItems(taskList);
        toDoTableView1.setItems(doneTaskList);

    }

    @FXML
    private void refreshTable() {
        taskList.clear();
        taskList = FXCollections.observableList(taskService.getTasks());
        toDoTableView.setItems(taskList);
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : white");
            System.out.println("dupa");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : white");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : white");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }

    }
}
