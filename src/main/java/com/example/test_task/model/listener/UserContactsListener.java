package com.example.test_task.model.listener;

import com.example.test_task.model.UserContacts;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class UserContactsListener {

    @PrePersist
    public void beforeCreate(UserContacts contact) {
        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);
        log.info("User '{}', added contact: email='{}', phone='{}'",
                contact.getOwner() != null ? contact.getOwner().getId() : "unknown",
                contact.getEmail(),
                contact.getPhone());
    }

    @PreUpdate
    public void beforeUpdate(UserContacts contact) {
        contact.setUpdatedAt(LocalDateTime.now());
        log.info("User '{}', updated contact: email='{}', phone='{}'",
                contact.getOwner() != null ? contact.getOwner().getId() : "unknown",
                contact.getEmail(),
                contact.getPhone());
    }

    @PostRemove
    public void afterRemove(UserContacts contact) {
        log.info("The contact of user '{}' has been deleted: email='{}', phone='{}'",
                contact.getOwner() != null ? contact.getOwner().getId() : "unknown",
                contact.getEmail(),
                contact.getPhone());
    }
}