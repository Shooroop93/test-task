package com.example.test_task.model.listener;

import com.example.test_task.model.UsersPhoto;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class UsersPhotoListener {

    @PrePersist
    public void beforeCreate(UsersPhoto photo) {
        LocalDateTime now = LocalDateTime.now();
        photo.setCreatedAt(now);
        photo.setUpdatedAt(now);
        log.info("User '{}', added photo: '{}'", photo.getOwner() != null ? photo.getOwner().getId() : "unknown", photo.getUrlPhoto());
    }

    @PreUpdate
    public void beforeUpdate(UsersPhoto photo) {
        photo.setUpdatedAt(LocalDateTime.now());
        log.info("User '{}', update photo: '{}'", photo.getOwner() != null ? photo.getOwner().getId() : "unknown", photo.getUrlPhoto());
    }

    @PostRemove
    public void afterRemove(UsersPhoto photo) {
        log.info("User '{}', removed photo: '{}'", photo.getOwner() != null ? photo.getOwner().getId() : "unknown", photo.getUrlPhoto());
    }
}
