package com.urz.oproject;

import com.urz.oproject.ToDoApplication.StageReadyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<ToDoApplication.StageReadyEvent> {
    private double x, y;
    @Value("classpath:/Home.fxml")
    private Resource toDoResource;
    private String applicationTitle;
    private ApplicationContext applicationContext;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(toDoResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
            Parent parent = fxmlLoader.load();

            Stage stage = event.getStage();
            stage.setTitle(applicationTitle);
            stage.setScene(new Scene(parent));
            //set stage borderless
           // stage.initStyle(StageStyle.UNDECORATED);
            //stage.setResizable(false);
            //drag it here
            parent.setOnMousePressed(eventL -> {
                x = eventL.getSceneX();
                y = eventL.getSceneY();
            });
            parent.setOnMouseDragged(eventL -> {

                stage.setX(eventL.getScreenX() - x);
                stage.setY(eventL.getScreenY() - y);

            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
