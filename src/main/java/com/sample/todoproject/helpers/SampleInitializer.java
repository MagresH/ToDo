package com.sample.todoproject.helpers;


import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.TaskService;
import com.sample.todoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class SampleInitializer {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public SampleInitializer(TaskService taskService, UserService userService) {

        this.taskService = taskService;
        this.userService = userService;
        AppUser user = new AppUser("Thomas","Long","login","password");
        userService.addUser(user);
        taskService.addTask(new Task("Visit Mary158", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023, 1, 8), false, false, user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023, 1, 6), false, false, user));        taskService.addTask(new Task("Visit Mary158", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023, 1, 8), false, false, user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023, 1, 6), false, false, user));        taskService.addTask(new Task("Visit Mary158", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023, 1, 8), false, false, user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023, 1, 6), false, false, user));        taskService.addTask(new Task("Visit Mary158", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit John", LocalDate.of(2023, 1, 8), false, false, user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023, 1, 6), false, false, user));        taskService.addTask(new Task("Visit Mary158", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
        taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
        Task task = new Task("Buy food", LocalDate.now(), false, false, user);
        taskService.addTask(new Task("Visit John", LocalDate.of(2023, 1, 8), false, false, user));
        taskService.addTask(new Task("Cat food", LocalDate.of(2023, 1, 6), false, false, user));


    }
}
