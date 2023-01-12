package com.sample.todoproject.controller;


import com.jfoenix.controls.JFXButton;
import com.sample.todoproject.factory.CellFactory;
import com.sample.todoproject.factory.RowFactory;
import com.sample.todoproject.helpers.TaskListType;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Controller
public class ToDoController implements Initializable {


    @FXML
    AnchorPane anchorPane;
    @FXML
    private Label totalTaskLabel, completedTaskLabel, toDoTask, taskListLabel;
    @FXML
    private JFXButton todayTasksButton, trashButton, logoutButton, allTasksButton, importantTasksButton, addTaskButton;
    @FXML
    private AnchorPane pnlOverview, pnlTrash;
    @FXML
    private TextField search;
    @FXML
    private TableView<Task> toDoTableView, doneTableView, trashTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn;

    @FXML

    private TableColumn<Task, Task> toDoCustomColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadLineDateColumn;
    @FXML
    private ObservableList<Task> toDoTaskList, doneTaskList;
    private final TaskService taskService;
    private final ApplicationContext applicationContext;
    private final CellFactory cellFactory;


    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext) {
        this.cellFactory = new CellFactory(this, taskService);
        this.taskService = taskService;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOverview.toFront();        //Moving main panel to front

        //Description column
        toDoDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        toDoDescColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));
        toDoDescColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));

        //Deadline column
        deadLineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        deadLineDateColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));
        deadLineDateColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));

        //Custom column
        toDoCustomColumn.setCellFactory(cellFactory.getCellFactory());
        toDoCustomColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        toDoCustomColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));
        toDoCustomColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));


        toDoTableView.getStyleClass().add("noheader");
        toDoTableView.setItems(toDoTaskList);
        toDoTableView.setRowFactory(RowFactory.getRowFactoryCallback(toDoTableView));
        refreshTable();
        toDoTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> toDoTableView.refresh());
    }

    public Task getSelectedTask(TableView<Task> tableView) {
        Task task = tableView.getSelectionModel().getSelectedItem();
        return task;
    }

    public void refreshTable() {
        TaskListType taskListType = taskService.getCurrentTaskListType();

        if (taskListType.equals(TaskListType.TODAY))
            toDoTaskList = FXCollections.observableList(taskService.getTodayTasks());
        else if (taskListType.equals(TaskListType.ALL))
            toDoTaskList = FXCollections.observableList(taskService.getAllTasks());
        else if (taskListType.equals(TaskListType.IMPORTANT))
            toDoTaskList = FXCollections.observableList(taskService.getImportantTasks());

        toDoTaskList = getSortedList();
        totalTaskLabel.setText(String.valueOf(toDoTaskList.size()));
        completedTaskLabel.setText(String.valueOf(toDoTaskList.stream().filter(Task::getTaskStatus).count()));
        toDoTask.setText(String.valueOf(toDoTaskList.stream().filter(task -> !task.getTaskStatus()).count()));

        toDoTableView.setItems(toDoTaskList);
        toDoTableView.refresh();
        toDoTableView.getSelectionModel().clearSelection();
    }

    private SortedList<Task> getSortedList() {
        SortedList<Task> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(toDoTableView.comparatorProperty());
        toDoTableView.refresh();
        return sortedList;
    }

    private FilteredList<Task> getFilteredList() {
        FilteredList<Task> filteredList = new FilteredList<>(toDoTaskList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(task -> {
                    if (newValue == null || newValue.isEmpty()) {
                        toDoTableView.refresh();
                        return true;
                    }
                    toDoTableView.refresh();
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (task.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return task.getDeadLineDate().toString().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == todayTasksButton) {
            taskListLabel.setText("Today Tasks");
            taskService.setCurrentTaskListType(TaskListType.TODAY);
            refreshTable();
        } else if (actionEvent.getSource() == allTasksButton) {
            taskListLabel.setText("All Tasks");
            taskService.setCurrentTaskListType(TaskListType.ALL);
            refreshTable();
        } else if (actionEvent.getSource() == importantTasksButton) {
            taskListLabel.setText("Important Tasks");
            taskService.setCurrentTaskListType(TaskListType.IMPORTANT);
            refreshTable();
        } else if (actionEvent.getSource() == trashButton) {
            //   trashTableView.setItems(doneTableView.getItems());
        } else if (actionEvent.getSource() == addTaskButton) {
            onAddButtonClick();
        }

    }
    public void onLogout(){
        anchorPane.setEffect(new GaussianBlur());
        Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to logout?", ButtonType.YES,ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                fxmlLoader.setLocation(getClass().getResource("/Login.fxml"));
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else anchorPane.setEffect(null);

    }
    public void onEditIconClick(Task task) {
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

    public void onStarIconClick(FontAwesomeIconView starIcon, Task task) {
        if (task.getImportantStatus()) {
            starIcon.setIcon(FontAwesomeIcon.STAR_ALT);
            task.setImportantStatus(false);
        } else {
            starIcon.setIcon(FontAwesomeIcon.STAR);
            task.setImportantStatus(true);
        }
        taskService.editTask(task);
        refreshTable();
    }

    public void onCheckIconClick(FontAwesomeIconView checkIcon, Task task) {
        if (task.getTaskStatus()) {
            checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE_ALT);
            task.setTaskStatus(false);
        } else {
            checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
            task.setTaskStatus(true);
        }
        taskService.editTask(task);
        refreshTable();
    }

    public TableView<Task> getToDoTableView() {
        return toDoTableView;
    }

}
