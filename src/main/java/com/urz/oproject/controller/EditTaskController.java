package com.urz.oproject.controller;

import com.jfoenix.controls.JFXButton;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class EditTaskController implements Initializable {

    private final TaskService taskService;
    @FXML
    private JFXButton btnApply;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private TextField description;

    private Task selectedTask;

    @Autowired
    public EditTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedTask = taskService.getSelectedTask();
        description.setText(selectedTask.getDescription());
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnApply) {
            selectedTask.setDescription(description.getText());
            selectedTask.setDeadLineDate(dataPicker.getValue());
            taskService.addTask(selectedTask);
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

        if (actionEvent.getSource() == btnCancel) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

    }
}
