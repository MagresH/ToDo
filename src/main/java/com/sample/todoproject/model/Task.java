package com.sample.todoproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "dead_line_date")
    private LocalDate deadLineDate;
    @Column(name = "task_status", nullable = false)
    private Boolean taskStatus;

    @Column(name = "important_status", nullable = false)
    private Boolean importantStatus;

    @ManyToOne
    AppUser appUser;

    public Task(String description, LocalDate deadLineDate, boolean taskStatus, boolean importantStatus, AppUser appUser) {
        this.description = description;
        this.deadLineDate = deadLineDate;
        this.taskStatus = taskStatus;
        this.importantStatus = importantStatus;
        this.appUser = appUser;
    }

}
