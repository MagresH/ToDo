package com.urz.oproject.service;

import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import com.urz.oproject.repository.TaskRepository;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }
}
