package com.sample.todoproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private Long id;
        private String description;
        private LocalDate deadLineDate;
        private Boolean taskStatus;
        private Boolean importantStatus;
        private AppUser appUser;

        public TaskBuilder() {
        }
        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder deadLineDate(LocalDate deadLineDate) {
            this.deadLineDate = deadLineDate;
            return this;
        }

        public TaskBuilder taskStatus(Boolean taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public TaskBuilder importantStatus(Boolean importantStatus) {
            this.importantStatus = importantStatus;
            return this;
        }

        public TaskBuilder appUser(AppUser appUser) {
            this.appUser = appUser;
            return this;
        }

        public Task build() {
            return new Task(description, deadLineDate, taskStatus, importantStatus, appUser);
        }

        public String toString() {
            return "Task.TaskBuilder(id=" + this.id + ", description=" + this.description + ", deadLineDate=" + this.deadLineDate + ", taskStatus=" + this.taskStatus + ", importantStatus=" + this.importantStatus + ", appUser=" + this.appUser + ")";
        }
    }
}
