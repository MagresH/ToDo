package com.sample.todoproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ToDoFXApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(ToDoProjectApplication.class).run();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
    public class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }
        public ConfigurableApplicationContext getAppContext(){
            return applicationContext;
        }
        public Stage getStage() {
            return (Stage) getSource();
        }

    }
}

