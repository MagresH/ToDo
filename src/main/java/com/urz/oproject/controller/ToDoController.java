package com.urz.oproject.controller;

import com.urz.oproject.model.AppUser;
import com.urz.oproject.repository.UserRepository;
import com.urz.oproject.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ToDoController implements Initializable {


    @FXML
    private Button substractButton;
    @FXML
    private Button addButton;
    private double progres = 0.0;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label label;


private final UserService userService;

    public ToDoController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    protected void onSubstractButtonClick() {
        if (progres >= 1.0) System.out.println("max");
        else System.out.println("substract progress");
        progres -= 0.1;
        progressBar.setProgress(progres);
        // var popupFactory = new PopupFactory().createWaitingPopup("max");
        // popupFactory.show();
    }

    @FXML
    protected void onAddButtonClick() {
        userService.addUser(new AppUser(1L,"John"));
        userService.addUser(new AppUser(2L,"Camille"));
        userService.addUser(new AppUser(3L,"Patrick"));
        if (userService != null) {
            List<AppUser> appUsers = userService.getUsers();
            appUsers.stream()
                    .forEach(user -> label.setText(label.getText()+" "+user.getName()));
        } else System.out.println("error");


        System.out.println("add progress");
        progres += 0.1;
        progressBar.setProgress(progres);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
