package com.sample.todoproject.repository;

import com.sample.todoproject.model.AppUser;
import com.sample.todoproject.model.Task;
import com.sample.todoproject.model.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrashRepository extends JpaRepository<Trash, Long> {
    Optional<List<Trash>> findTrashesByAppUser(AppUser appUser);
}
