package com.urz.oproject.controller;

import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class TestController implements Initializable {
    @FXML
    Label label;


    private final TaskService taskService;

    public TestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task task = taskService.getTasks().get(0);
        label.setText(task.getShortDesc());
    }
}
