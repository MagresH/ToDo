package com.sample.todoproject.service;

import org.springframework.stereotype.Service;

@Service
public class TrashService {
    private final UserService userService;
    private final TaskService taskService;

    public TrashService(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

}
