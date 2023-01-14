package com.sample.todoproject.model;

import com.sample.todoproject.service.UserService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;
    @ManyToOne
    private AppUser appUser;

    public Trash(String description, LocalDate deadLineDate, LocalDateTime deleteDate, AppUser appUser) {
        this.description = description;
        this.deadLineDate = deadLineDate;
        this.deleteDate = deleteDate;
        this.appUser= appUser;
    }

    public static TrashBuilder builder() {
        return new TrashBuilder();
    }


    public static class TrashBuilder {
        private Long id;
        private String description;
        private LocalDate deadLineDate;
        private LocalDateTime deleteDate;
        private AppUser appUser;

        public TrashBuilder() {
        }


        public TrashBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TrashBuilder deadLineDate(LocalDate deadLineDate) {
            this.deadLineDate = deadLineDate;
            return this;
        }

        public TrashBuilder deleteDate(LocalDateTime deleteDate) {
            this.deleteDate = deleteDate;
            return this;
        }

        public TrashBuilder appUser(AppUser appUser) {
            this.appUser = appUser;
            return this;
        }

        public Trash build() {
            return new Trash(description, deadLineDate, deleteDate, appUser);
        }

        public String toString() {
            return "Trash.TrashBuilder(id=" + this.id + ", description=" + this.description + ", deadLineDate=" + this.deadLineDate + ", deleteDate=" + this.deleteDate + ", appUser=" + this.appUser + ")";
        }
    }
}
