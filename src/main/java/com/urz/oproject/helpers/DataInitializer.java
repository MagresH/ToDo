package com.urz.oproject.helpers;


import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import com.urz.oproject.service.TaskService;
import com.urz.oproject.service.UserService;
import de.jensd.fx.glyphs.testapps.App;
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
        taskService.addTask(new Task("Visit Mary", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023,1,6),false,false,user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023,1,6),false,false,user));

       // taskService.addTask(new Task(6L, "test", "test long", false, false));
       // taskService.addTask(new Task.TaskBuilder().shortDesc("test builder").longDesc("long test").taskStatus(true).importantStatus(false).build());

    }
}
