package com.urz.oproject.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.urz.oproject.factory.CellFactory;
import com.urz.oproject.factory.RowFactory;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private JFXTextArea search;
    @FXML
    private TableView<Task> toDoTableView, doneTableView, trashTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn;

    @FXML
    private TableColumn<Task, Task> toDoCustomColumn;
    @FXML
    private TableColumn<Task, Task> deadLineDateColumn;
    @FXML
    private ObservableList<Task> toDoTaskList, doneTaskList;
    private final TaskService taskService;
    private final ApplicationContext applicationContext;
    private final CellFactory cellFactory;


    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext) {
        this.cellFactory = new CellFactory(this,taskService);
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
        toDoTaskList = FXCollections.observableList(taskService.getTodayTasks());
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

        if (actionEvent.getSource() == btnOverview) {
            System.out.println("overview");
            pnlOverview.toFront();
        }

        if (actionEvent.getSource() == btnTrash) {
            trashTableView.setItems(doneTableView.getItems());
            pnlTrash.toFront();
        }
        if (actionEvent.getSource() == addTaskButton) {
            System.out.println("test");
            onAddButtonClick();
        }

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
            System.out.println("on na off");
        } else {
            starIcon.setIcon(FontAwesomeIcon.STAR);
            task.setImportantStatus(true);
            System.out.println("off na on");
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
