package com.sample.todoproject.service;

import com.sample.todoproject.enums.TaskListType;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.repository.TaskRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Getter
    @Setter
    private Task selectedTask;
    @Getter
    @Setter
    TaskListType currentTaskListType = TaskListType.TODAY;
    private final TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository
                .findTasksByAppUser(AppUserService.loggedUser)
                .orElse(List.of())
                .stream()
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }
    public List<Task> getImportantTasks() {
        return taskRepository
                .findTasksByAppUser(AppUserService.loggedUser)
                .orElse(List.of())
                .stream()
                .filter(Task::getImportantStatus)
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }
    public List<Task> getTodayTasks() {
        return taskRepository
                .findTasksByAppUser(AppUserService.loggedUser)
                .orElse(List.of())
                .stream()
                .filter(task -> Objects.equals(task.getDeadLineDate(), LocalDate.now()))
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void editTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }


}
