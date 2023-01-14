package com.sample.todoproject.service;

import com.sample.todoproject.helpers.TaskListType;
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
    private final TrashService trashService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TrashService trashService) {
        this.taskRepository = taskRepository;
        this.trashService = trashService;
    }

    public List<Task> getAllTasks() {
        return taskRepository
                .findTasksByAppUser(UserService.loggedUser)
                .orElse(List.of())
                .stream()
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }
    public List<Task> getImportantTasks() {
        return taskRepository
                .findTasksByAppUser(UserService.loggedUser)
                .orElse(List.of())
                .stream()
                .filter(Task::getImportantStatus)
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }

    public List<Task> getTodayTasks() {
        return taskRepository
                .findTasksByAppUser(UserService.loggedUser)
                .orElse(List.of())
                .stream()
                .filter(task -> Objects.equals(task.getDeadLineDate(), LocalDate.now()))
                .sorted(Comparator.comparing(Task::getImportantStatus).reversed())
                .sorted(Comparator.comparing(Task::getTaskStatus))
                .toList();
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Task editTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }


    public List<Task> getDoneTasks() {
        return taskRepository.findTasksByTaskStatusTrue().get();
    }

    public List<Task> getUnDoneTasks() {
        return taskRepository.findTasksByTaskStatusFalse().get();
    }

}
