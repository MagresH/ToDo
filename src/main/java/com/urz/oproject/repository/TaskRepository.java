package com.urz.oproject.repository;

import com.urz.oproject.model.AppUser;
import com.urz.oproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
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
