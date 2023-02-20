package com.sample.todoproject.helpers;
import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.service.TaskService;
import com.sample.todoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class SampleDataInitializer {
    private final TaskService taskService;
    private final UserService userService;
    @Autowired
    public SampleDataInitializer(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
        initializeSampleUserAndTasks();
    }

    private void initializeSampleUserAndTasks() {
        if (userService.getAppUserByUsername("login") == null) {
            AppUser user = new AppUser("John", "Doe", "login", "password");
            userService.addAppUser(user);
            taskService.addTask(new Task("Visit Mary", LocalDate.now(), false, true, user));
            taskService.addTask(new Task("Visit Erick", LocalDate.now(), false, false, user));
            taskService.addTask(new Task("Buy food", LocalDate.now(), false, false, user));
            taskService.addTask(new Task("Visit John", LocalDate.now().plusDays(5), false, false, user));
            taskService.addTask(new Task("Cat food", LocalDate.now().plusDays(5), false, false, user));
        }
    }
}
