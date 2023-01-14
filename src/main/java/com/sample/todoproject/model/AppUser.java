package com.sample.todoproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "AppUser")
public class AppUser implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "user_sequence")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public AppUser(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    @OneToMany(mappedBy = "appUser")
    private List<Task> tasks;

    @OneToMany(mappedBy = "appUser")
    private List<Trash> trashItems;

    public static AppUserBuilder builder() {
        return new AppUserBuilder();
    }


    public static class AppUserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private List<Task> tasks;
        private List<Trash> trashItems;

        public AppUserBuilder() {
        }

        public AppUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AppUserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AppUser build() {
            return new AppUser(id, firstName, lastName, username, password, tasks, trashItems);
        }

        public String toString() {
            return "AppUser.AppUserBuilder(id=" + this.id + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", username=" + this.username + ", password=" + this.password + ", tasks=" + this.tasks + ", trashItems=" + this.trashItems + ")";
        }
    }
}
