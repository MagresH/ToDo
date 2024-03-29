package com.sample.todoproject.repository;

import com.sample.todoproject.model.Task;
import com.sample.todoproject.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<List<Task>> findTasksByAppUser(AppUser appUser);
    @Query("""
            select t from Task t
            where t.appUser = ?1 and t.importantStatus = true
            order by t.importantStatus DESC, t.taskStatus""")
    Optional<List<Task>> findTasksByAppUserAndImportantStatusTrueOrderByImportantStatusDescTaskStatus(AppUser appUser);


}
