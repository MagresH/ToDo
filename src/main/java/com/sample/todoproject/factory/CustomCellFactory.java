package com.sample.todoproject.factory;

import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.TaskService;
import com.sample.todoproject.controller.ToDoController;
import com.sample.todoproject.service.TrashService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomCellFactory {
    private final ToDoController toDoController;
    private final TaskService taskService;
    private final TrashService trashService;
    private final ApplicationContext applicationContext;
    static Callback<TableColumn<Task, Task>, TableCell<Task, Task>> cellFactory;

    public static Callback<TableColumn<Task, Task>, TableCell<Task, Task>> getCellFactory() {
        return cellFactory;
    }

    @Autowired
    public CustomCellFactory(ToDoController toDoController, TaskService taskService, TrashService trashService, ApplicationContext applicationContext) {
        this.taskService = taskService;
        this.toDoController = toDoController;
        this.trashService = trashService;
        this.applicationContext = applicationContext;
        cellFactory = param -> new TableCell<>() {
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

                    if (item.getTaskStatus()) {
                        checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE);
                    } else {
                        checkIcon.setIcon(FontAwesomeIcon.CHECK_SQUARE_ALT);
                    }

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
                    mi2.setOnAction(event -> onStarIconClick(checkIcon, getSelectedTask()));
                    mi3.setOnAction(event -> onEditIconClick(getSelectedTask()));
                    mi4.setOnAction(event -> {
                        Task task = getSelectedTask();
                        trashService.addTrash(task);
                        taskService.deleteTask(task);
                        toDoController.refreshTrashTable();
                        toDoController.refreshToDoTable();
                    });
                    toDoController.toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(toDoController.toDoTableView.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                        }
                        if ((event.getClickCount() == 2) && (getSelectedTask() != null) && (event.getSource()!=checkIcon))
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

    private Task getSelectedTask() {
        return toDoController.toDoTableView.getSelectionModel().getSelectedItem();
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
        toDoController.refreshToDoTable();
    }

    public void onEditIconClick(Task task) {
        taskService.setSelectedTask(task);
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            fxmlLoader.setLocation(getClass().getResource("/fxml/EditTask.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
            stage.setOnHiding(hideEvent -> toDoController.refreshToDoTable());
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
        toDoController.refreshToDoTable();
    }

}
