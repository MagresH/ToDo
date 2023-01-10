package com.urz.oproject.factory;

import com.urz.oproject.model.Task;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.springframework.stereotype.Component;
public class RowFactory {
    public static Callback<TableView<Task>, TableRow<Task>> getRowFactoryCallback(TableView<Task> tbl){
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
                    if (item.isTaskStatus()) getStyleClass().add("highlightedRow");
                    else getStyleClass().remove("highlightedRow");
                }
            }
        };
    }
}
