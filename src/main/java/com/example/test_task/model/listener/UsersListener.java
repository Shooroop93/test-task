package com.example.test_task.model.listener;

import com.example.test_task.model.Users;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class UsersListener {

    @PrePersist
    public void beforeCreate(Users users) {
        LocalDateTime now = LocalDateTime.now();
        users.setCreatedAt(now);
        users.setUpdatedAt(now);
        log.info("Create user with email: {}", users.getEmail());
    }

    @PreUpdate
    public void beforeUpdate(Users users) {
        users.setUpdatedAt(LocalDateTime.now());
        log.info("Updating user with email: {}", users.getEmail());
    }

    @PostRemove
    public void afterRemove(Users users) {
        log.info("User removed with email: {}", users.getEmail());
    }
}