package com.sample.todoproject.factory;

import com.sample.todoproject.model.Task;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class RowFactory {
    public static Callback<TableView<Task>, TableRow<Task>> getRowFactoryCallback(){
        return RowFactory::rowFactory;
    }
    public static TableRow<Task> rowFactory(TableView<Task> tbl) {
        return new TableRow<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                updateStyleAndTooltip();
            }

            private void updateStyleAndTooltip() {
                Task item = getItem();
                if (item == null || isEmpty()) {
                    setStyle("");
                    setTooltip(null);
                } else {
                    if (item.getTaskStatus()){
                        getStyleClass().add("highlightedRow");
                    }
                    else getStyleClass().remove("highlightedRow");
                }
            }
        };
    }
}
