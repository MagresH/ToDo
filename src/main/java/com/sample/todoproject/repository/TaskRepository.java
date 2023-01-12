package com.sample.todoproject.repository;

import com.sample.todoproject.model.Task;
import com.sample.todoproject.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    void deleteTaskById(Long id);

    Optional<List<Task>> findTasksByAppUser(AppUser appUser);

    Optional<List<Task>> findTasksByTaskStatusTrue();

    Optional<List<Task>> findTasksByTaskStatusFalse();

    @Deprecated
    Optional<List<Task>> findTasksByAppUserOrderByTaskStatusAsc(AppUser appUser);

}
