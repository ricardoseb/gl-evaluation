package dev.riqui.evaluation.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private String token;

    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Phone> phones;
}
