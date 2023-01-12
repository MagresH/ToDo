package com.sample.todoproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trash")
public class Trash {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Trash_SEQ")
    @SequenceGenerator(name = "Trash_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "dead_line_date")
    private LocalDate deadLineDate;

    @Column(name = "delete_date", nullable = false)
    private LocalDate deleteDate;
    @ManyToOne
    private AppUser appUser;



}