package com.urz.oproject.controller;


import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class Controller2 implements Initializable {

    private final TaskService taskService;
    @FXML
    private VBox pnItems = null;
    @FXML
    private JFXNodesList nodesList;
    @FXML
    private Button btnOverview;
    @FXML
    private Label totalTask;

    private FilteredList<Task> filteredList;
    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;
    @FXML
    TableView<Task> tableView;
    @FXML
    TableColumn<Task, Long> idColumn;
    @FXML
    TableColumn<Task, String> shortDescColumn;
    @FXML
    TableColumn<Task, String> longDesc;
    //    @FXML
//    TableColumn<Task, Boolean> taskStatus;
//    @FXML
//    TableColumn<Task, Boolean> importantStatus;
    @FXML
    private TableColumn<Task, String> editCol;
    @FXML
    ObservableList<Task> taskList;

    @FXML
    private ListView listView;


    public Controller2(TaskService taskService) {
        this.taskService = taskService;
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOrders.toBack();
        pnlOverview.setStyle("-fx-background-color : white");
        pnlOverview.toFront();

        taskService.addTask(new Task("test1", "test long", false, false));
        taskService.addTask(new Task("test2", "test long", false, false));
        taskService.addTask(new Task("test3", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("te4st", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("tes5t", "test long", false, false));
        taskService.addTask(new Task("te4st", "test long", false, false));
        taskService.addTask(new Task("tefst", "test long", false, false));
        taskService.addTask(new Task("tefgst", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("teghst", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("tes5t", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test", "test long", false, false));
        taskService.addTask(new Task("test5", "test long", false, false));
        taskService.addTask(new Task("test3", "test long", false, false));
        taskService.addTask(new Task("test1", "test long", false, false));
        taskService.addTask(new Task("test35", "test long", false, false));
        taskService.addTask(new Task("test531", "test long", false, false));
        taskService.addTask(new Task("test55", "test long", false, false));
        taskService.addTask(new Task(6L, "test", "test long", false, false));
        taskService.addTask(new Task.TaskBuilder().shortDesc("test builder").longDesc("long test").taskStatus(true).importantStatus(false).build());

        taskList = FXCollections.observableList(taskService.getTasks());

        totalTask.setText(String.valueOf(taskList.size()));

        filteredList = new FilteredList<>(taskList, p -> true);
        System.out.println(filteredList);
        listView.setItems(filteredList);

        shortDescColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("shortDesc"));

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory = (TableColumn<Task, String> param) -> {
            final TableCell<Task, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        CheckBox checkBox = new CheckBox();
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#00E676;"
                        );

                        tableView.setOnMouseClicked((MouseEvent event) -> {
                            Task selectedTask = tableView.getSelectionModel().getSelectedItem();
                            if (selectedTask != null) {
                                if (event.getClickCount() == 2) //Checking double click
                                {
                                    Stage dialog = new Stage();
                                    dialog.initModality(Modality.APPLICATION_MODAL);
                                    //   dialog.initOwner(primaryStage);
                                    VBox dialogVbox = new VBox(20);
                                    Label label = new Label(selectedTask.getLongDesc());
                                    dialogVbox.getChildren().add(label);
                                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                                    dialog.setScene(dialogScene);
                                    dialog.show();
                                    System.out.println("You clicked on " + tableView.getSelectionModel().getSelectedItem().getShortDesc());

                                }
                            }
                        });
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                            Task task = tableView.getSelectionModel().getSelectedItem();
                            taskService.deleteTask(task.getId());
                            refreshTable();
                            totalTask.setText(String.valueOf(taskList.size()));
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Task task = tableView.getSelectionModel().getSelectedItem();
                            //   AddStudentController addStudentController = loader.getController();
                            //    addStudentController.setUpdate(true);
//                            addStudentController.setTextField(student.getId(), student.getName(),
//                                    student.getBirth().toLocalDate(), student.getAdress(), student.getEmail());
                            Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            //   dialog.initOwner(primaryStage);
                            VBox dialogVbox = new VBox(20);
                            dialogVbox.getChildren().add(new Text("This is a Dialog"));
                            Scene dialogScene = new Scene(dialogVbox, 300, 200);
                            dialog.setScene(dialogScene);
                            dialog.show();


                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        editCol.setCellFactory(cellFactory);
        tableView.getStyleClass().add("noheader");
        tableView.setItems(taskList);
//
//        Parent parent = new FXMLLoader().load(getClass().getResource("/test.fxml"));
//        Stage stage = new Stage();
//        stage.setScene(new Scene(parent));
//        stage.initStyle(StageStyle.UTILITY);
//        stage.show();

        //tableView.getSelectionModel().getSelectedItem().getLongDesc();

//        Node[] nodes = new Node[taskService.getTasks().size()];
//        for (int i = 0; i < taskService.getTasks().size(); i++) {
//            Item item = new Item(taskService.getTasks().get(i).getShortDesc());
//            if (item.getShortText() == null) System.out.println("nie dziala");
//            else {
//                System.out.println("DZIALA");
//                System.out.println("opis zadania: " + item.getShortText());
//            }
//            nodes[i] = item;
//
//            // nodes[i] = FXMLLoader.load(getClass().getResource("/Item.fxml"));
//            pnItems.getChildren().add(nodes[i]);
//        }
//        List<Node> list = Arrays.asList(nodes);
//        ObservableList<Node> lista = FXCollections.observableList(list);
        // listView.setItems(lista);
    }

    @FXML
    private void refreshTable() {
        taskList.clear();
        taskList = FXCollections.observableList(taskService.getTasks());
        tableView.setItems(taskList);
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnCustomers) {
            pnlCustomer.setStyle("-fx-background-color : white");
            pnlCustomer.toFront();
        }
        if (actionEvent.getSource() == btnMenus) {
            pnlMenus.setStyle("-fx-background-color : white");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnOverview) {
            pnlOverview.setStyle("-fx-background-color : white");
            pnlOverview.toFront();
        }
        if (actionEvent.getSource() == btnOrders) {
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        }
        if (actionEvent.getSource() == tableView.getSelectionModel()) {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            //   dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("This is a Dialog"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }
}
