package com.example.test_task.model;

import com.example.test_task.model.listener.UsersListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EntityListeners(UsersListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "middle_name", nullable = true, length = 100)
    private String middleName;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<UserContacts> contacts;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private UsersPhoto photo;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Tokens> tokens;

    public void addToken(Tokens token) {
        if (tokens == null) {
            tokens = new ArrayList<>();
        }
        tokens.add(token);
    }
}