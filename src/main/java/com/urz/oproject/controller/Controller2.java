package com.urz.oproject.controller;


import com.jfoenix.controls.JFXNodesList;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class Controller2 implements Initializable {

    private final TaskService taskService;
    @FXML
    private VBox pnItems = null;
    @FXML
    private JFXNodesList nodesList;
    @FXML
    private Button btnOverview;

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
    TableColumn<Task,Long> idColumn;
    @FXML
    TableColumn<Task,String> shortDescColumn;
    @FXML
    TableColumn<Task, String> longDesc;
    @FXML
    TableColumn<Task, Boolean> taskStatus;
    @FXML
    TableColumn<Task, Boolean> importantStatus;



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
        ObservableList<Task> tasks = FXCollections.observableList(taskService.getTasks());
        filteredList = new FilteredList<>(tasks, p -> true);
        System.out.println(filteredList);
        listView.setItems(filteredList);

        Task test = tasks.get(1);

        idColumn.setCellValueFactory(new PropertyValueFactory<Task,Long>("id"));
        shortDescColumn.setCellValueFactory(new PropertyValueFactory<Task,String>("shortDesc"));
        longDesc.setCellValueFactory(new PropertyValueFactory<Task,String>("longDesc"));
        taskStatus.setCellValueFactory(new PropertyValueFactory<Task,Boolean>("taskStatus"));
        importantStatus.setCellValueFactory(new PropertyValueFactory<Task,Boolean>("importantStatus"));


        tableView.setItems(tasks);


        Node[] nodes = new Node[taskService.getTasks().size()];
        for (int i = 0; i < taskService.getTasks().size(); i++) {
            Item item = new Item(taskService.getTasks().get(i).getShortDesc());
            if (item.getShortText() == null) System.out.println("nie dziala");
            else {
                System.out.println("DZIALA");
                System.out.println("opis zadania: " + item.getShortText());
            }
            nodes[i] = item;

            // nodes[i] = FXMLLoader.load(getClass().getResource("/Item.fxml"));
            pnItems.getChildren().add(nodes[i]);
        }
        List<Node> list = Arrays.asList(nodes);
        ObservableList<Node> lista = FXCollections.observableList(list);
        // listView.setItems(lista);
    }


    public void onExitButtonClick() {
        Platform.exit();
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
    }
}
