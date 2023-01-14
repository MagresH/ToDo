package com.sample.todoproject.factory;

import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.TaskService;
import com.sample.todoproject.controller.ToDoController;
import com.sample.todoproject.service.TrashService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CustomCellFactory {
    private final ToDoController toDoController;
    private final TaskService taskService;
    private final TrashService trashService;
    Callback<TableColumn<Task, Task>, TableCell<Task, Task>> cellFactory;

    public Callback<TableColumn<Task, Task>, TableCell<Task, Task>> getCellFactory() {
        return cellFactory;
    }

    public CustomCellFactory(ToDoController toDoController, TaskService taskService, TableView<Task> toDoTableView, TrashService trashService) {
        this.taskService = taskService;
        this.toDoController = toDoController;
        this.trashService = trashService;
        this.cellFactory = param -> new TableCell<>() {
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

                    mi1.setOnAction(event -> {
                        toDoController.onCheckIconClick(checkIcon, toDoTableView.getSelectionModel().getSelectedItem());
                    });
                    mi2.setOnAction(event -> {
                        toDoController.onStarIconClick(checkIcon, toDoTableView.getSelectionModel().getSelectedItem());
                    });
                    mi3.setOnAction(event -> {
                        toDoController.onEditIconClick(toDoTableView.getSelectionModel().getSelectedItem());
                    });
                    mi4.setOnAction(event -> {
                        Task task = toDoTableView.getSelectionModel().getSelectedItem();
                        System.out.println(task.getDescription());
                        trashService.addTrash(task);
                        taskService.deleteTask(task);
                        toDoController.refreshTrashTable();
                        toDoController.refreshToDoTable();
                    });
                    toDoTableView.setOnMouseClicked((MouseEvent event) -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(toDoTableView.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                        }
                        if ((event.getClickCount() == 2) && (toDoTableView.getSelectionModel().getSelectedItem() != null))
                            toDoController.onEditIconClick(toDoTableView.getSelectionModel().getSelectedItem());
                    });

                    checkIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onCheckIconClick(checkIcon, toDoTableView.getSelectionModel().getSelectedItem());
                    });

                    editIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onEditIconClick(toDoTableView.getSelectionModel().getSelectedItem());
                    });

                    starIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onStarIconClick(starIcon, toDoTableView.getSelectionModel().getSelectedItem());
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
}
