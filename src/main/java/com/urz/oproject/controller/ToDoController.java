package com.urz.oproject.controller;


import com.jfoenix.controls.JFXButton;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.input.ScrollEvent;
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
import java.util.ResourceBundle;

@Controller
public class ToDoController implements Initializable {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Label totalTaskLabel, completedTaskLabel, toDoTask;
    @FXML
    private JFXButton btnOverview, btnTrash, btnSignout, addTaskButton;
    @FXML
    private AnchorPane pnlOverview, pnlTrash;
    @FXML
    private TableView<Task> toDoTableView, doneTableView, trashTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn;
    @FXML
    private TableColumn<Task, String> doneDescColumn;
    @FXML
    private TableColumn<Task, Task> toDoCustomColumn;
    @FXML
    private TableColumn<Task, String> doneCustomColumn;
    @FXML
    private TableColumn<Task, String> trashDescColumn;
    @FXML
    private TableColumn<Task, String> trashCustomColumn;
    @FXML
    private TableColumn<Task, Task> deadLineDateColumn;
    @FXML
    private ObservableList<Task> toDoTaskList, doneTaskList;
    private final TaskService taskService;
    private final ApplicationContext applicationContext;


    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext) {
        this.taskService = taskService;
        this.applicationContext = applicationContext;
    }

    private static TableRow<Task> rowFactory(TableView<Task> tbl) {
        return new TableRow<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                updateStyleAndTooltip();
            }

            private void updateStyleAndTooltip() {
                Task item = getItem();
                if (item == null || isEmpty()) {
                    setStyle("");
                    setTooltip(null);
                } else {
                    if (item.isTaskStatus()) getStyleClass().add("highlightedRow");
                    else getStyleClass().remove("highlightedRow");
                }
            }
        };
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Perfomance
        anchorPane.setCache(true);
        anchorPane.setCacheShape(true);
        anchorPane.setCacheHint(CacheHint.SPEED);

        toDoTableView.setCache(true);
        toDoTableView.setCacheShape(true);
        toDoTableView.setCacheHint(CacheHint.SPEED);

        //Moving main panel to front
        pnlOverview.toFront();


        //Description column
        Callback<TableColumn<Task, Task>, TableCell<Task, Task>> cellFactory = this::cellFactory;
        toDoDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        toDoDescColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));
        toDoDescColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));

        //Deadline column
        deadLineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        deadLineDateColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));
        deadLineDateColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));

        //Custom column
        toDoCustomColumn.setCellFactory(cellFactory);
        toDoCustomColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        toDoCustomColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));
        toDoCustomColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));

//        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory11 = this::call3;
//        trashCustomColumn.setCellFactory(cellFactory11);
//        trashDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        toDoTableView.getStyleClass().add("noheader");
        toDoTableView.setItems(toDoTaskList);
        toDoTableView.setRowFactory(ToDoController::rowFactory);
        refreshTable();
        toDoTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> toDoTableView.refresh());
    }

    private Task getSelectedTask(TableView<Task> tableView) {
        Task task = tableView.getSelectionModel().getSelectedItem();
        return task;
    }

    public void refreshTable() {
        toDoTaskList = FXCollections.observableList(taskService.getTasks());
        totalTaskLabel.setText(String.valueOf(taskService.getTasks().size()));
        completedTaskLabel.setText(String.valueOf(taskService.getDoneTasks().size()));
        toDoTask.setText(String.valueOf(taskService.getUnDoneTasks().size()));

        toDoTableView.setItems(toDoTaskList);
        toDoTableView.refresh();
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
        if (actionEvent.getSource() == addTaskButton) {
            System.out.println("test");
            onAddButtonClick();
        }

    }

    private TableCell<Task, Task> cellFactory(TableColumn<Task, Task> param) {
        return new TableCell<>() {
            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {
                    FontAwesomeIconView checkIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
                    FontAwesomeIconView starIcon = new FontAwesomeIconView(FontAwesomeIcon.STAR);

                    if (item.isTaskStatus()) {
                        checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
                    } else {
                        checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    }

                    if (item.isImportantStatus()) starIcon.setIcon(FontAwesomeIcon.STAR);
                    else starIcon.setIcon(FontAwesomeIcon.STAR_ALT);

                    //TODO bug, fast clicking on check icon trigger this method with no task selected(null)
                    toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                        if (event.getClickCount() == 2) onEditIconClick(getSelectedTask(toDoTableView));
                    });

                    checkIcon.setOnMouseClicked((MouseEvent event) -> {
                        onCheckIconClick(checkIcon, getSelectedTask(toDoTableView));
                    });

                    editIcon.setOnMouseClicked((MouseEvent event) -> {
                        onEditIconClick(getSelectedTask(toDoTableView));
                    });

                    starIcon.setOnMouseClicked((MouseEvent event) -> {
                        onStarIconClick(starIcon, getSelectedTask(toDoTableView));
                    });

                    HBox managebtn = new HBox(starIcon, editIcon, checkIcon);
                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(starIcon, new Insets(2, 3, 0, 3));
                    HBox.setMargin(checkIcon, new Insets(2, 0, 0, 3));
                    HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                    setGraphic(managebtn);
                }
            }

        };
    }

    private void onEditIconClick(Task task) {
        taskService.setSelectedTask(task);
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            fxmlLoader.setLocation(getClass().getResource("/EditTask.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            stage.setOnHiding(hideEvent -> refreshTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddButtonClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            fxmlLoader.setLocation(getClass().getResource("/AddTask.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            stage.setOnHiding(hideEvent -> refreshTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onStarIconClick(FontAwesomeIconView starIcon, Task task) {
        if (task.isImportantStatus()) {
            starIcon.setIcon(FontAwesomeIcon.STAR_ALT);
            task.setImportantStatus(false);
        } else {
            starIcon.setIcon(FontAwesomeIcon.STAR);
            task.setImportantStatus(true);
        }
        taskService.editTask(task);
        refreshTable();
    }

    private void onCheckIconClick(FontAwesomeIconView checkIcon, Task task) {
        if (task.isTaskStatus()) {
            checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE_ALT);
            task.setTaskStatus(false);
        } else {
            checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
            task.setTaskStatus(true);
        }
        taskService.editTask(task);
        completedTaskLabel.setText(String.valueOf(taskService.getDoneTasks().size()));
        toDoTask.setText(String.valueOf(taskService.getUnDoneTasks().size()));
        refreshTable();
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
