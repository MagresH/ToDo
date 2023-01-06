package com.urz.oproject.controller;


import com.jfoenix.controls.JFXButton;
import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import com.urz.oproject.repository.TaskRepository;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Controller
public class ToDoController implements Initializable {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Label totalTaskLabel, completedTaskLabel, toDoTask;
    @FXML
    private JFXButton btnOverview, btnTrash, btnSignout;
    @FXML
    private AnchorPane pnlOverview, pnlTrash;
    @FXML
    private TableView<Task> toDoTableView, doneTableView, trashTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn, doneDescColumn, toDoCustomColumn, doneCustomColumn, trashDescColumn, trashCustomColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadLineDateColumn;
    @FXML
    private ObservableList<Task> toDoTaskList, doneTaskList;

    private final TaskService taskService;
    private final ApplicationContext applicationContext;
    private final TaskRepository taskRepository;

    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext,
                          TaskRepository taskRepository) {
        this.taskService = taskService;
        this.applicationContext = applicationContext;
        this.taskRepository = taskRepository;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //Perfomance
        anchorPane.setCache(true);
        anchorPane.setCacheShape(true);
        anchorPane.setCacheHint(CacheHint.SPEED);

        //Moving main panel to front
        pnlOverview.toFront();

        toDoTaskList = FXCollections.observableList(taskService.getTasks());
        doneTaskList = FXCollections.observableList(taskService.getDoneTasks());

        totalTaskLabel.setText(String.valueOf(taskService.getTasks().size()));

        //Description column
        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory = this::call;
        toDoDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        toDoDescColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));
        toDoDescColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));

        //Deadline column
        deadLineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        deadLineDateColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));
        deadLineDateColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));

        //Custom column
        toDoCustomColumn.setCellFactory(cellFactory);
        toDoCustomColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));
        toDoCustomColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));



        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory1 = this::call2;

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory11 = this::call3;

        doneDescColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("shortDesc"));
        doneCustomColumn.setCellFactory(cellFactory1);

        trashCustomColumn.setCellFactory(cellFactory11);
        trashDescColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("shortDesc"));

        doneTableView.getStyleClass().add("noheader");
        toDoTableView.getStyleClass().add("noheader");

        toDoTableView.setItems(toDoTaskList);
        doneTableView.setItems(doneTaskList);



    }

    private Task getSelectedTask(TableView<Task> tableView) {
        Task task = tableView.getSelectionModel().getSelectedItem();
        return task;
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnOverview) {
            System.out.println("overview");
            pnlOverview.toFront();
        }

        if (actionEvent.getSource() == btnTrash) {
            trashTableView.setItems(doneTableView.getItems());
            trashDescColumn = doneDescColumn;
            pnlTrash.toFront();
        }

    }

    private TableCell<Task, String> call(TableColumn<Task, String> param) {
        final TableCell<Task, String> cell = new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    FontAwesomeIconView checkIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);

                    toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                        Task selectedTask = toDoTableView.getSelectionModel().getSelectedItem();
//                        if (selectedTask != null) {
//                            if (event.getClickCount() == 2) {
//                                Stage dialog = new Stage();
//                                dialog.initModality(Modality.APPLICATION_MODAL);
//                                VBox dialogVbox = new VBox(20);
//                                Label label = new Label(selectedTask.getDescription());
//                                dialogVbox.getChildren().add(label);
//                                Scene dialogScene = new Scene(dialogVbox, 300, 200);
//                                dialog.setScene(dialogScene);
//                                dialog.show();
//                                System.out.println("You clicked on " + toDoTableView.getSelectionModel().getSelectedItem().getDescription());
//
//                            }
//                        }
                    });
                    checkIcon.setOnMouseClicked((MouseEvent event) -> {
                        checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
                        Task task = getSelectedTask(toDoTableView);
                       // toDoTaskList.remove(task);
                        toDoTableView.setItems(toDoTaskList);
                        doneTaskList.add(task);
                        completedTaskLabel.setText(String.valueOf(doneTaskList.size()));
                        toDoTask.setText(String.valueOf(toDoTaskList.size()));
                        if (!doneTaskList.isEmpty()) {
                            doneTableView.setItems(doneTaskList);
                        }
                    });

                    editIcon.setOnMouseClicked((MouseEvent event) -> {
                        Task task = toDoTableView.getSelectionModel().getSelectedItem();
                        taskService.setSelectedTask(task);
                        try {
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setControllerFactory(applicationContext::getBean);
                            fxmlLoader.setLocation(getClass().getResource("/EditTask.fxml"));
                            AnchorPane root = fxmlLoader.load();
                            stage.setTitle("Sample app");
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });

                    HBox managebtn = new HBox(editIcon, checkIcon);
                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(checkIcon, new Insets(2, 0, 0, 3));
                    HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                    setGraphic(managebtn);
                    setText(null);
                }
            }


        };
        return cell;
    }

    private TableCell<Task, String> call2(TableColumn<Task, String> param) {


        final TableCell<Task, String> cell = new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    FontAwesomeIconView reDoIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_CIRCLE_ALT_UP);
                    reDoIcon.setOnMouseClicked((MouseEvent event) -> {
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
                            Task task = doneTableView.getSelectionModel().getSelectedItem();
                            doneTaskList.remove(task);
                            doneTableView.setItems(doneTaskList);
                            taskService.deleteTask(task.getId());
                            totalTaskLabel.setText(String.valueOf(taskService.getTasks().size()));
                            toDoTask.setText(String.valueOf(toDoTaskList.size()));

                            dialog.close();
                        });
                        noButton.setOnMouseClicked((MouseEvent event2) -> {
                            dialog.close();
                        });
                    });
                    HBox managebtn = new HBox(reDoIcon);
                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(reDoIcon, new Insets(2, 2, 0, 3));
                    setGraphic(managebtn);
                    setText(null);
                }
            }
        };
        return cell;
    }

    private TableCell<Task, String> call3(TableColumn<Task, String> param) {
        final TableCell<Task, String> cell = new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

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
                            Task task = doneTableView.getSelectionModel().getSelectedItem();
                            doneTaskList.remove(task);
                            doneTableView.setItems(doneTaskList);
                            taskService.deleteTask(task.getId());
                            totalTaskLabel.setText(String.valueOf(taskService.getTasks().size()));
                            toDoTask.setText(String.valueOf(toDoTaskList.size()));

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
    }
}
