package com.urz.oproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "short_desc", nullable = false)
    private String shortDesc;

    @Lob
    @Column(name = "long_desc")
    private String longDesc;

    @Column(name = "task_status", nullable = false)
    private boolean taskStatus;

    @Column(name = "important_status", nullable = false)
    private boolean importantStatus;

    public Task(String shortDesc, String longDesc, boolean taskStatus, boolean importantStatus) {
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.taskStatus = taskStatus;
        this.importantStatus = importantStatus;
    }
}
