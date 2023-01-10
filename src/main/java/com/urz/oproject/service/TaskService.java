package com.urz.oproject.service;

import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import com.urz.oproject.repository.TaskRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Getter
    @Setter
    private Task selectedTask;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findTasksByAppUserOrderByTaskStatusAsc(UserService.loggedUser).get();
        // return taskRepository.findTasksByAppUser(UserService.loggedUser).get();
    }
//    public List<Task> getTasks(){
//        return taskRepository.findTasksByAppUserOrderByTaskStatusAsc(UserService.loggedUser).get().stream()
//                .filter(task -> Objects.equals(task.getDeadLineDate(), LocalDate.now()))
//                .collect(Collectors.toCollection(ArrayList::new));
//    }

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
