package com.urz.oproject.controller;

import com.jfoenix.controls.JFXButton;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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


    @Autowired
    public EditTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task selectedTask = taskService.getSelectedTask();
        //Task task = taskService.getTasks().get(1);
        description.setText(selectedTask.getDescription());
    }
}
