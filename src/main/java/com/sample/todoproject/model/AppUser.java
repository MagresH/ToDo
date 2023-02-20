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


    public static class AppUserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;


        public AppUserBuilder() {
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
            return new AppUser(firstName, lastName, username, password);
        }

    }
}
