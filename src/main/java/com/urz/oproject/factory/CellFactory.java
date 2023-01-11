package com.urz.oproject.factory;

import com.urz.oproject.controller.ToDoController;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class CellFactory {
    private final ToDoController toDoController;
    private final TaskService taskService;
    Callback<TableColumn<Task, Task>, TableCell<Task, Task>> cellFactory;

    public Callback<TableColumn<Task, Task>, TableCell<Task, Task>> getCellFactory() {
        return cellFactory;
    }

    public CellFactory(ToDoController toDoController, TaskService taskService) {
        this.taskService = taskService;
        this.toDoController = toDoController;
        this.cellFactory = param -> new TableCell<Task, Task>() {
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
                    MenuItem mi1 = new MenuItem("DELETE TASK");
                    contextMenu.getItems().add(mi1);
                    mi1.setOnAction(event -> {
                        taskService.deleteTask(toDoController.getSelectedTask(toDoController.getToDoTableView()));
                        toDoController.refreshTable();
                    });

                    toDoController.getToDoTableView().setOnMouseClicked((MouseEvent event) -> {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(toDoController.getToDoTableView(), event.getScreenX(), event.getScreenY());
                        }
                        if ((event.getClickCount() == 2) && (toDoController.getSelectedTask(toDoController.getToDoTableView()) != null))
                            toDoController.onEditIconClick(toDoController.getSelectedTask(toDoController.getToDoTableView()));
                    });

                    checkIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onCheckIconClick(checkIcon, toDoController.getSelectedTask(toDoController.getToDoTableView()));
                    });

                    editIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onEditIconClick(toDoController.getSelectedTask(toDoController.getToDoTableView()));
                    });

                    starIcon.setOnMouseClicked((MouseEvent event) -> {
                        toDoController.onStarIconClick(starIcon, toDoController.getSelectedTask(toDoController.getToDoTableView()));
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
