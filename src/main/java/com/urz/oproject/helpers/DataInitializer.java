package com.urz.oproject.helpers;


import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final TaskService taskService;

    @Autowired
    public DataInitializer(TaskService taskService) {

        this.taskService = taskService;

        taskService.addTask(new Task("Visit John", "test long", false, false));
        taskService.addTask(new Task("Visit John", "test long", false, false));
        taskService.addTask(new Task("Visit John", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Schedule haircut", "test long", false, false));
        taskService.addTask(new Task("Cat food", "test long", false, false));
        taskService.addTask(new Task(6L, "test", "test long", false, false));
       // taskService.addTask(new Task.TaskBuilder().shortDesc("test builder").longDesc("long test").taskStatus(true).importantStatus(false).build());

    }
}
