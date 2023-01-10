package com.urz.oproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import com.urz.oproject.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Controller
public class AddTaskController implements Initializable {

    private final TaskService taskService;
    @FXML
    private JFXButton btnApply, btnCancel;
    @FXML
    private DatePicker dataPicker;
    @FXML
    private TextField description;
    @FXML
    private JFXCheckBox importantCheckBox;


    @Autowired
    public AddTaskController(TaskService taskService) {
        this.taskService = taskService;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("adding started");
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnApply) {
            Task newTask = new Task();

            if (description.getText()==null){
            }
            newTask.setDescription(description.getText());

            if (dataPicker.getValue() == null) {
                newTask.setDeadLineDate(LocalDate.now());
            } else newTask.setDeadLineDate(dataPicker.getValue());

            newTask.setImportantStatus(importantCheckBox.isSelected());
            newTask.setTaskStatus(false);
            newTask.setAppUser(UserService.loggedUser);
            taskService.addTask(newTask);
            System.out.println("test dodania");
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
        if (actionEvent.getSource() == btnCancel) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

    }
}
