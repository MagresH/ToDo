package com.sample.todoproject.controller;

import com.jfoenix.controls.JFXButton;
import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {

    private final ApplicationContext applicationContext;
    private final UserService userService;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    JFXButton loginButton;


    @Autowired
    public LoginController(ApplicationContext applicationContext, UserService userService) {
        this.applicationContext = applicationContext;
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void loadMainStage() throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setLocation(getClass().getResource("/fxml/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        stage.setMinWidth(1070);
        stage.setMinHeight(800);
        System.setProperty("prism.lcdtext", "false");
        stage.show();
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onLoginButtonClick() throws IOException {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Provide username and password");
            alert.show();
        } else {
            if (canLogin()) loadMainStage();
        }
    }

    @FXML
    public void onGuestButtonClick() throws IOException {
        usernameField.setText("login");
        passwordField.setText("password");
        onLoginButtonClick();
    }

    private Boolean canLogin() {
        String login = usernameField.getText();
        String password = passwordField.getText();
        AppUser user = userService.getAppUserByUsername(login);
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("User with provided username dont exist");
            alert.show();
        } else {
            if (user.getPassword().equals(password)) {
                UserService.setLoggedUser(user);
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Wrong password");
                alert.show();
                return false;
            }
        }
        return false;
    }

    public void onSignUpButtonClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setLocation(getClass().getResource("/fxml/Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        System.setProperty("prism.lcdtext", "false");
        stage.show();
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }
}
