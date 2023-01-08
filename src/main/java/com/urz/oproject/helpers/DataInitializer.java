package com.urz.oproject.helpers;


import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import com.urz.oproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataInitializer {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public DataInitializer(TaskService taskService, UserService userService) {

        this.taskService = taskService;
        this.userService = userService;
        AppUser user = new AppUser("Thomas","123");
        userService.addUser(user);
        userService.setLoggedUser(user);

        taskService.addTask(new Task("Visit Mary12", LocalDate.of(2023,1,6),true,false,user));
        taskService.addTask(new Task("Visit Mary13", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary14", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary15", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary157", LocalDate.of(2023,1,6),false,true,user));
        taskService.addTask(new Task("Visit Mary158", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary159", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary150", LocalDate.of(2023,1,6),false,true,user));
        taskService.addTask(new Task("Visit Mary16", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary17", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit Mary18", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023,1,6),false,false,user));


    }
}
