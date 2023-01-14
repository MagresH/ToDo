package com.sample.todoproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<ToDoFXApplication.StageReadyEvent> {
    @Value("classpath:/fxml/Login.fxml")
    private Resource toDoResource;
    private String applicationTitle;
    private ApplicationContext applicationContext;

    @Autowired
    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ToDoFXApplication.StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(toDoResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            System.setProperty("prism.lcdtext", "false");
            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            Scene scene = new Scene(parent);
            stage.setTitle(applicationTitle);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
