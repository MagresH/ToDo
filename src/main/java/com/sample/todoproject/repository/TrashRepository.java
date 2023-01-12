package com.sample.todoproject.repository;

import com.sample.todoproject.model.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {
}