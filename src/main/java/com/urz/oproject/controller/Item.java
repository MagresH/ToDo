package com.urz.oproject.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleNode;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Item extends HBox{

    @FXML
    HBox item;
    @FXML
    JFXCheckBox taskStatusCheckBox;

    private boolean taskStatus;
    private boolean isFavourite;


    @Setter
    @Getter
    private String shortText;
    @FXML
    JFXToggleNode favourite;

    @FXML
    FontAwesomeIconView star;

    public Item(String shortText) {
        this.shortText = shortText;
        //
    }

    public HBox getNode(){
        item.getChildren().add(taskStatusCheckBox);
        item.getChildren().add(favourite);
        item.getChildren().add(star);
        taskStatusCheckBox.setText(shortText);
        System.out.println("TEST");
        return item;
    }
    public void onCheckBoxClick() {
        if (taskStatusCheckBox.isSelected()) System.out.printf("on");
        else if (!taskStatusCheckBox.isSelected()) System.out.printf("off");
    }

    public void onClick() {
        System.out.print(taskStatusCheckBox.isSelected());
    }


}
