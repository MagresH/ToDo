package com.sample.todoproject.controller;


import com.jfoenix.controls.JFXButton;
import com.sample.todoproject.factory.RowFactory;
import com.sample.todoproject.enums.TaskListType;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.UserService;
import com.sample.todoproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;

@Controller
public class ToDoController implements Initializable {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Label totalTaskLabel, completedTaskLabel, toDoTask, taskListLabel, helloLabel;
    @FXML
    private JFXButton todayTasksButton, allTasksButton, importantTasksButton;
    @FXML
    private AnchorPane pnlOverview;
    @FXML
    private TextField search;
    @FXML
    public TableView<Task> toDoTableView;
    @FXML
    private TableColumn<Task, String> toDoDescColumn;
    @FXML
    private TableColumn<Task, Task> toDoCustomColumn;
    @FXML
    private TableColumn<Task, LocalDate> deadLineDateColumn;
    @FXML
    private ObservableList<Task> toDoTaskList;
    @FXML
    private DatePicker minDate, maxDate;
    @FXML
    private FontAwesomeIconView checkIcon, starIcon, editIcon, calendar;
    private final TaskService taskService;
    private final ApplicationContext applicationContext;

    @Autowired
    public ToDoController(TaskService taskService, ApplicationContext applicationContext) {
        this.taskService = taskService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enableDateSearch(false);
        //Description column
        toDoDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        toDoDescColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));
        toDoDescColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.6));

        //Deadline column
        deadLineDateColumn.setCellValueFactory(new PropertyValueFactory<>("deadLineDate"));
        deadLineDateColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));
        deadLineDateColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.2));

        //Custom column
        toDoCustomColumn.setCellFactory(this::customCellFactory);
        toDoCustomColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        toDoCustomColumn.minWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));
        toDoCustomColumn.maxWidthProperty().bind(toDoTableView.widthProperty().multiply(0.175));

        toDoTableView.getStyleClass().add("noheader");
        toDoTableView.setRowFactory(RowFactory.getRowFactoryCallback());
        toDoTableView.setItems(toDoTaskList);

        helloLabelController();
        refreshToDoTable();
        toDoTableView.addEventFilter(ScrollEvent.ANY, scrollEvent -> toDoTableView.refresh());
    }


    private Task getSelectedTask() {
        return toDoTableView.getSelectionModel().getSelectedItem();
    }

    public void refreshToDoTable() {
        TaskListType taskListType = taskService.getCurrentTaskListType();

        if (taskListType.equals(TaskListType.TODAY))
            toDoTaskList = FXCollections.observableList(taskService.getTodayTasks());
        else if (taskListType.equals(TaskListType.ALL))
            toDoTaskList = FXCollections.observableList(taskService.getAllTasks());
        else if (taskListType.equals(TaskListType.IMPORTANT))
            toDoTaskList = FXCollections.observableList(taskService.getImportantTasks());

        minDate.setValue(LocalDate.now());
        maxDate.setValue(toDoTaskList.stream().map(Task::getDeadLineDate).max(Comparator.comparing(LocalDate::toEpochDay)).orElse(LocalDate.now()));

        toDoTaskList = getFilteredTaskList();

        totalTaskLabel.setText(String.valueOf(toDoTaskList.size()));
        completedTaskLabel.setText(String.valueOf(toDoTaskList.stream().filter(Task::getTaskStatus).count()));
        toDoTask.setText(String.valueOf(toDoTaskList.stream().filter(task -> !task.getTaskStatus()).count()));

        toDoTableView.setItems(toDoTaskList);
        toDoTableView.refresh();
        toDoTableView.getSelectionModel().clearSelection();

    }

    private FilteredList<Task> getFilteredTaskList() {
        if (taskService.getCurrentTaskListType().equals(TaskListType.TODAY)) return getFilteredListBySearch();
        else return getFilteredListBySearchAndDates();
    }

    @FXML
    private FilteredList<Task> getFilteredListBySearchAndDates() {
        FilteredList<Task> filteredListBySearchAndDates = new FilteredList<>(getFilteredListBySearch());
        filteredListBySearchAndDates.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                    LocalDate startDate = minDate.getValue();
                    LocalDate lastDate = maxDate.getValue();
                    return task -> !startDate.isAfter(task.getDeadLineDate()) && !lastDate.isBefore(task.getDeadLineDate());
                },
                minDate.valueProperty(),
                maxDate.valueProperty()));
        toDoTableView.refresh();
        return filteredListBySearchAndDates;
    }

    private FilteredList<Task> getFilteredListBySearch() {
        FilteredList<Task> filteredListBySearch = new FilteredList<>(toDoTaskList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredListBySearch.setPredicate(task -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (task.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return task.getDeadLineDate().toString().contains(lowerCaseFilter);
                }));
        toDoTableView.refresh();
        return filteredListBySearch;
    }
    public void onMenuItemClick(ActionEvent actionEvent) {
        var event = actionEvent.getSource();
        if (event == todayTasksButton) {
            enableDateSearch(false);
            pnlOverview.toFront();
            taskListLabel.setText("Today Tasks");
            taskService.setCurrentTaskListType(TaskListType.TODAY);
            refreshToDoTable();
        } else if (event == allTasksButton) {
            enableDateSearch(true);
            pnlOverview.toFront();
            taskListLabel.setText("All Tasks");
            taskService.setCurrentTaskListType(TaskListType.ALL);
            refreshToDoTable();
        } else if (event == importantTasksButton) {
            enableDateSearch(true);
            pnlOverview.toFront();
            taskListLabel.setText("Important Tasks");
            taskService.setCurrentTaskListType(TaskListType.IMPORTANT);
            refreshToDoTable();
        }
    }

    private void enableDateSearch(boolean flag) {
        if (flag) {
            minDate.setVisible(true);
            maxDate.setVisible(true);
            calendar.setVisible(true);
        } else {
            minDate.setVisible(false);
            maxDate.setVisible(false);
            calendar.setVisible(false);
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnHiding(hideEvent -> refreshToDoTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TableCell<Task, Task> customCellFactory(TableColumn<Task, Task> taskTaskTableColumn) {
        return new TableCell<>() {
            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    checkIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
                    starIcon = new FontAwesomeIconView(FontAwesomeIcon.STAR);

                    if (item.getTaskStatus()) checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
                    else checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    if (item.getImportantStatus()) starIcon.setIcon(FontAwesomeIcon.STAR);
                    else starIcon.setIcon(FontAwesomeIcon.STAR_ALT);

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem mi1 = new MenuItem("Mark as done");
                    MenuItem mi2 = new MenuItem("Mark as important");
                    MenuItem mi3 = new MenuItem("Edit task");
                    MenuItem mi4 = new MenuItem("DELETE TASK");

                    contextMenu.getItems().add(mi1);
                    contextMenu.getItems().add(mi2);
                    contextMenu.getItems().add(mi3);
                    contextMenu.getItems().add(mi4);

                    mi1.setOnAction(event -> onCheckIconClick(checkIcon, getSelectedTask()));
                    mi2.setOnAction(event -> onStarIconClick(starIcon, getSelectedTask()));
                    mi3.setOnAction(event -> onEditIconClick(getSelectedTask()));
                    mi4.setOnAction(event -> {
                        Task task = getSelectedTask();
                        taskService.deleteTask(task);
                        refreshToDoTable();
                    });
                    toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(toDoTableView.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                        }
                        if ((event.getClickCount() == 2) && (getSelectedTask() != null) && (event.getSource() != checkIcon))
                            onEditIconClick(getSelectedTask());
                    });
                    checkIcon.setOnMouseClicked((MouseEvent event) -> onCheckIconClick(checkIcon, getSelectedTask()));
                    editIcon.setOnMouseClicked((MouseEvent event) -> onEditIconClick(getSelectedTask()));
                    starIcon.setOnMouseClicked((MouseEvent event) -> onStarIconClick(starIcon, getSelectedTask()));

                    HBox buttonBox = new HBox(starIcon, editIcon, checkIcon);
                    buttonBox.setStyle("-fx-alignment:center");
                    HBox.setMargin(starIcon, new Insets(2, 3, 0, 3));
                    HBox.setMargin(checkIcon, new Insets(2, 0, 0, 3));
                    HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                    setGraphic(buttonBox);
                }
            }

        };
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
        refreshToDoTable();
    }

    public void onEditIconClick(Task task) {
        taskService.setSelectedTask(task);
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            fxmlLoader.setLocation(getClass().getResource("/fxml/EditTask.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnHiding(hideEvent -> refreshToDoTable());
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
        refreshToDoTable();
    }

    private void helloLabelController() {
        final double MAX_TEXT_WIDTH = 256;
        final double defaultFontSize = 28;
        final Font defaultFont = Font.font(defaultFontSize);
        Text text = new Text("Hello " + UserService.loggedUser.getFirstName() + " " + UserService.loggedUser.getLastName());
        helloLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tmpText = new Text(newValue);
            tmpText.setFont(defaultFont);
            double textWidth = tmpText.getLayoutBounds().getWidth();
            if (textWidth <= MAX_TEXT_WIDTH) {
                helloLabel.setFont(defaultFont);
            } else {
                double newFontSize = defaultFontSize * MAX_TEXT_WIDTH / textWidth;
                helloLabel.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
        helloLabel.textProperty().bind(text.textProperty());
    }
}
