package com.sample.todoproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.sample.todoproject.model.Task;
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
public class EditTaskController implements Initializable {

    private final TaskService taskService;
    @FXML
    private JFXButton btnApply,btnCancel;
    @FXML
    private DatePicker dataPicker;
    @FXML
    private TextField description;
    @FXML
    private JFXCheckBox importantCheckBox;
    private Task selectedTask;

    @Autowired
    public EditTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedTask = taskService.getSelectedTask();
        description.setText(selectedTask.getDescription());
        dataPicker.setValue(selectedTask.getDeadLineDate());
        if (selectedTask.getImportantStatus()) importantCheckBox.selectedProperty().setValue(true);

    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnApply) {
            if ((description.getText()==null||(description.getText()==""))){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Set description!");
                alert.show();
            }
            else {
                if (dataPicker.getValue()==null) selectedTask.setDeadLineDate(LocalDate.now());
                else selectedTask.setDeadLineDate(dataPicker.getValue());
                selectedTask.setDeadLineDate(dataPicker.getValue());
                selectedTask.setImportantStatus(importantCheckBox.isSelected());
                selectedTask.setDescription(description.getText());
                taskService.editTask(selectedTask);
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
