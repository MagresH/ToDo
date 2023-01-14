package com.sample.todoproject.service;

import com.sample.todoproject.model.Task;
import com.sample.todoproject.model.Trash;
import com.sample.todoproject.repository.TrashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrashService {
    private final TrashRepository trashRepository;

    @Autowired
    public TrashService(TrashRepository trashRepository) {
        this.trashRepository = trashRepository;
    }

    public List<Trash> getAllTrashes(){
        return trashRepository.findTrashesByAppUser(UserService.loggedUser).orElse(List.of());
    }
    public Trash addTrash(Task task) {
        Trash trash = new Trash(task.getDescription(),task.getDeadLineDate(),LocalDateTime.now(),UserService.loggedUser);
        return trashRepository.save(trash);
    }

    public void deleteTask(Trash trash) {
        trashRepository.delete(trash);
    }
}
