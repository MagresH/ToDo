package com.sample.todoproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.UserService;
import com.sample.todoproject.service.TaskService;
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
        dataPicker.setValue(LocalDate.now());
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnApply) {
            Task newTask = new Task();

            if ((description.getText()==null||(description.getText()==""))){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Set description!");
                alert.show();
            }
            else {
                if (dataPicker.getValue()==null) newTask.setDeadLineDate(LocalDate.now());
                else newTask.setDeadLineDate(dataPicker.getValue());

                newTask.setImportantStatus(importantCheckBox.isSelected());
                newTask.setDescription(description.getText());
                newTask.setImportantStatus(importantCheckBox.isSelected());
                newTask.setTaskStatus(false);
                newTask.setAppUser(UserService.loggedUser);
                System.out.println("test");
                taskService.addTask(newTask);
                Stage stage = (Stage) btnApply.getScene().getWindow();
                stage.close();
            }

        }
        if (actionEvent.getSource() == btnCancel) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

    }
}
