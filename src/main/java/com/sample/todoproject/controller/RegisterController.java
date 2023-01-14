package com.sample.todoproject.controller;

import com.jfoenix.controls.JFXButton;
import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.service.TaskService;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Controller
public class RegisterController implements Initializable {
    private final ApplicationContext applicationContext;
    private final UserService userService;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField usernameField, firstNameField, lastNameField;
    @FXML
    PasswordField passwordField, repeatPasswordField;
    @FXML
    JFXButton registerButton, loginButton;

    public RegisterController(ApplicationContext applicationContext, UserService userService) {
        this.applicationContext = applicationContext;
        this.userService = userService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void loadLoginPage() throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setLocation(getClass().getResource("/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        System.setProperty("prism.lcdtext", "false");
        stage.show();
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void onLoginButtonClick() throws IOException {
        loadLoginPage();
    }

    @FXML
    public void onRegisterButtonClick() throws IOException {
        if (canRegister()) {
            var user = new AppUser.AppUserBuilder()
                    .username(usernameField.getText())
                    .firstName(firstNameField.getText())
                    .lastName(lastNameField.getText())
                    .password(passwordField.getText())
                    .build();
            userService.addUser(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Account created!");
            alert.showAndWait();
            loadLoginPage();
        }
    }

    private boolean canRegister() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        Set<String> fields = new HashSet<>(Arrays.asList(username, password, repeatPassword, firstName, lastName));
        if (fields.stream()
                .map(field -> field.isEmpty() || field.isBlank())
                .findFirst()
                .orElse(false)) {
            alert.setContentText("Something is missing!");
            alert.show();
            return false;
        } else {
            if (userService.getAppUserByUsername(username) == null) {
                if (Objects.equals(password, repeatPassword)) return true;
                else {
                    alert.setContentText("Password does not match");
                    alert.show();
                    return false;
                }
            } else {
                alert.setContentText("User with provided username already exist");
                alert.show();
                return false;
            }
        }
    }
}

