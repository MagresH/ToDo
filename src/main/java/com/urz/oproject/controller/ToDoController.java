package com.urz.oproject.controller;


import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ToDoController implements Initializable {

    @FXML
    private Label totalTask, completedTask, toDoTask;
    @FXML
    private Button btnOverview, btnTrash, btnSignout;
    @FXML
    private AnchorPane pnlOverview, pnlTrash;
    @FXML
    private TableView<Task> toDoTableView, toDoTableView1, trashTableView;
    @FXML
    private TableColumn<Task, String> shortDescColumn, shortDescColumn1, longDesc, editCol, editCol1, shortDescColumn11, editCol11;
    @FXML
    private ObservableList<Task> taskList, doneTaskList;
    private final TaskService taskService;

    @Autowired
    public ToDoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pnlOverview.setStyle("-fx-background-color : white");
        pnlOverview.toFront();

        taskService.addTask(new Task("Visit John", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
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
                        deleteIcon.setStyle(" -fx-cursor: hand ;" + "-glyph-size:28px;" + "-fx-fill:#ff1744;");
                        editIcon.setStyle(" -fx-cursor: hand ;" + "-glyph-size:28px;" + "-fx-fill:#00E676;");

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
                            if (!doneTaskList.isEmpty()) {
                                toDoTableView1.setItems(doneTaskList);
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Task task = toDoTableView.getSelectionModel().getSelectedItem();
                            Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            VBox dialogVbox = new VBox(20);
                            dialogVbox.getChildren().add(new Text("Change task"));
                            Scene dialogScene = new Scene(dialogVbox, 300, 200);
                            dialog.setScene(dialogScene);
                            dialog.show();
                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
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

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory11 = (TableColumn<Task, String> param) -> {
            final TableCell<Task, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
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

        editCol11.setCellFactory(cellFactory11);

        shortDescColumn11.setCellValueFactory(new PropertyValueFactory<Task, String>("shortDesc"));
        toDoTableView1.getStyleClass().add("noheader");
        toDoTableView.getStyleClass().add("noheader");

        toDoTableView.setItems(taskList);
        toDoTableView1.setItems(doneTaskList);

    }

//    @FXML
//    private void refreshTable() {
//        taskList.clear();
//        taskList = FXCollections.observableList(taskService.getTasks());
//        toDoTableView.setItems(taskList);
//    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnOverview) {
            System.out.println("overview");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnTrash) {
            trashTableView.setItems(toDoTableView1.getItems());
            shortDescColumn11 = shortDescColumn1;
            pnlTrash.toFront();
        }

    }
}
