package com.urz.oproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
//    public static TaskBuilder builder() {
//        return new TaskBuilder();
//    }
//
//    public static class TaskBuilder {
//        private Long id;
//        private String shortDesc;
//        private String longDesc;
//        private boolean taskStatus;
//        private boolean importantStatus;
//
//        public TaskBuilder() {
//        }
//
//        public TaskBuilder id(Long id) {
//            this.id = id;
//            return this;
//        }
//
//        public TaskBuilder shortDesc(String shortDesc) {
//            this.shortDesc = shortDesc;
//            return this;
//        }
//
//        public TaskBuilder longDesc(String longDesc) {
//            this.longDesc = longDesc;
//            return this;
//        }
//
//        public TaskBuilder taskStatus(boolean taskStatus) {
//            this.taskStatus = taskStatus;
//            return this;
//        }
//
//        public TaskBuilder importantStatus(boolean importantStatus) {
//            this.importantStatus = importantStatus;
//            return this;
//        }
//
//        public Task build() {
//            return new Task(id, shortDesc, longDesc, taskStatus, importantStatus);
//        }
//
//        public String toString() {
//            return "Task.TaskBuilder(id=" + this.id + ", shortDesc=" + this.shortDesc + ", longDesc=" + this.longDesc + ", taskStatus=" + this.taskStatus + ", importantStatus=" + this.importantStatus + ")";
//        }
//    }
}
