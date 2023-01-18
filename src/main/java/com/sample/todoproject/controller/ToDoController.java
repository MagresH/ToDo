package com.sample.todoproject.controller;


import com.jfoenix.controls.JFXButton;
import com.sample.todoproject.factory.CustomCellFactory;
import com.sample.todoproject.factory.RowFactory;
import com.sample.todoproject.enums.TaskListType;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.model.Trash;
import com.sample.todoproject.service.TaskService;
import com.sample.todoproject.service.TrashService;
import com.sample.todoproject.service.AppUserService;
import javafx.beans.binding.Bindings;
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
import java.time.format.DateTimeFormatter;
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
    public TableView<Task> toDoTableView;
    @FXML
    private TableView<Trash> trashTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn;
    @FXML
    private TableColumn<Trash, String> trashDescColumn,trashDeleteColumn;

    @FXML
    private TableColumn<Task, Task> toDoCustomColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadLineDateColumn;
    @FXML
    private TableColumn<Trash, LocalDate> trashDeadLineColumn;
    @FXML
    private ObservableList<Task> toDoTaskList;
    @FXML
    private ObservableList<Trash> trashTaskList;
    @FXML
    private DatePicker minDate, maxDate;
    private final TaskService taskService;
    private final ApplicationContext applicationContext;
    private final TrashService trashService;


    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext, TrashService trashService) {
        this.taskService = taskService;
        this.applicationContext = applicationContext;
        this.trashService = trashService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOverview.toFront();        //Moving main panel to front
        swapDateSearch(false);

        //Description column
        toDoDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        toDoDescColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));
        toDoDescColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));

        //Deadline column
        deadLineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        deadLineDateColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));
        deadLineDateColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));

        //Custom column
        toDoCustomColumn.setCellFactory(CustomCellFactory.getCellFactory());
        toDoCustomColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        toDoCustomColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));
        toDoCustomColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));

        toDoTableView.getStyleClass().add("noheader");
        toDoTableView.setItems(toDoTaskList);
        toDoTableView.setRowFactory(RowFactory.getRowFactoryCallback(toDoTableView));
        refreshToDoTable();
        toDoTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> toDoTableView.refresh());

        trashDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        trashDescColumn.minWidthProperty().bind(trashTableView.widthProperty().multiply(0.6));
        trashDescColumn.maxWidthProperty().bind(trashTableView.widthProperty().multiply(0.6));

        trashDeadLineColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        trashDeadLineColumn.minWidthProperty().bind(trashTableView.widthProperty().multiply(0.2));
        trashDeadLineColumn.maxWidthProperty().bind(trashTableView.widthProperty().multiply(0.2));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        trashDeleteColumn.setCellValueFactory(cell1 -> new SimpleObjectProperty<>(cell1.getValue().getDeleteDate().format(formatter)));
        trashDeleteColumn.minWidthProperty().bind(trashTableView.widthProperty().multiply(0.18));
        trashDeleteColumn.maxWidthProperty().bind(trashTableView.widthProperty().multiply(0.18));

        trashTableView.setItems(trashTaskList);
        trashTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> trashTableView.refresh());
        refreshTrashTable();

    }

    public void refreshToDoTable() {
        minDate.setValue(LocalDate.now());
        maxDate.setValue(LocalDate.now());
        TaskListType taskListType = taskService.getCurrentTaskListType();

        if (taskListType.equals(TaskListType.TODAY))
            toDoTaskList = FXCollections.observableList(taskService.getTodayTasks());
        else if (taskListType.equals(TaskListType.ALL))
            toDoTaskList = FXCollections.observableList(taskService.getAllTasks());
        else if (taskListType.equals(TaskListType.IMPORTANT))
            toDoTaskList = FXCollections.observableList(taskService.getImportantTasks());

        toDoTaskList = getFilteredListByDates();
        totalTaskLabel.setText(String.valueOf(toDoTaskList.size()));
        completedTaskLabel.setText(String.valueOf(toDoTaskList.stream().filter(Task::getTaskStatus).count()));
        toDoTask.setText(String.valueOf(toDoTaskList.stream().filter(task -> !task.getTaskStatus()).count()));

        toDoTableView.setItems(toDoTaskList);
        toDoTableView.refresh();
        toDoTableView.getSelectionModel().clearSelection();
    }

    public void refreshTrashTable() {
        trashTaskList = FXCollections.observableList(trashService.getAllTrashes());
        trashTableView.setItems(trashTaskList);
        trashTableView.refresh();
    }

    @FXML
    private FilteredList<Task> getFilteredListByDates() {
        FilteredList<Task> filteredItems = new FilteredList<>(getSortedListBySearch(toDoTableView));
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate startDate = minDate.getValue();
                    LocalDate lastDate = maxDate.getValue();
                    final LocalDate finalMin = minDate == null ? LocalDate.now() : startDate;
                    final LocalDate finalMax = maxDate == null ? LocalDate.now() : lastDate;
                    return task -> !finalMin.isAfter(task.getDeadLineDate()) && !finalMax.isBefore(task.getDeadLineDate());
                },
                minDate.valueProperty(),
                maxDate.valueProperty()));
        toDoTableView.refresh();
        return filteredItems;
    }

    private SortedList<Task> getSortedListBySearch(TableView<Task> tableView) {
        SortedList<Task> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.refresh();
        return sortedList;
    }

    private FilteredList<Task> getFilteredList() {
        FilteredList<Task> filteredList = new FilteredList<>(toDoTaskList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(task -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (task.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return task.getDeadLineDate().toString().contains(lowerCaseFilter);
                }));
        toDoTableView.refresh();
        return filteredList;
    }

    public void onMenuItemClick(ActionEvent actionEvent) {
        var event = actionEvent.getSource();

        if (event == todayTasksButton) {
            pnlOverview.toFront();
            swapDateSearch(false);
            taskListLabel.setText("Today Tasks");
            taskService.setCurrentTaskListType(TaskListType.TODAY);
            refreshToDoTable();
        } else if (event == allTasksButton) {
            swapDateSearch(true);
            pnlOverview.toFront();
            taskListLabel.setText("All Tasks");
            taskService.setCurrentTaskListType(TaskListType.ALL);
            refreshToDoTable();
        } else if (event == importantTasksButton) {
            swapDateSearch(true);
            pnlOverview.toFront();
            taskListLabel.setText("Important Tasks");
            taskService.setCurrentTaskListType(TaskListType.IMPORTANT);
            refreshToDoTable();
        } else if (event == trashButton) {
            pnlTrash.toFront();
        }

    }

    @FXML
    public void onRestoreButtonClick() {
        Trash trash = trashTableView.getSelectionModel().getSelectedItem();
        if (trash != null) {
            Task restoredTask = new Task.TaskBuilder()
                    .description(trash.getDescription())
                    .taskStatus(false)
                    .importantStatus(false)
                    .appUser(AppUserService.loggedUser)
                    .deadLineDate(trash.getDeadLineDate())
                    .build();
            taskService.addTask(restoredTask);
            trashService.deleteTask(trash);
            trashTableView.refresh();

            refreshTrashTable();
            refreshToDoTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Task restored succesfully!");
            alert.show();
        }
    }

    private void swapDateSearch(boolean flag) {
        if (flag) {
            minDate.setVisible(true);
            maxDate.setVisible(true);
        } else {
            minDate.setVisible(false);
            maxDate.setVisible(false);
        }
    }

    @FXML
    public void onLogout() {
        anchorPane.setEffect(new GaussianBlur());
        Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to logout?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                fxmlLoader.setLocation(getClass().getResource("/fxml/Login.fxml"));
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else anchorPane.setEffect(null);

    }

    @FXML
    public void onAddButtonClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            fxmlLoader.setLocation(getClass().getResource("/fxml/AddTask.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            stage.setOnHiding(hideEvent -> refreshToDoTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
