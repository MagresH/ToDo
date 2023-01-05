package com.urz.oproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="AppUser")
@Builder
public class AppUser implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "user_sequence")
    private Long id;
    @Column
    private String name;

    @Column
    private String password;

    public AppUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @OneToMany(mappedBy = "appUser")
    private List<Task> tasks;


}
