package com.example.test_task.repository;

import com.example.test_task.model.UserContacts;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserContactsDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<UserContacts> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserContacts.class, id));
    }

    public List<UserContacts> findByUserId(Long userId) {
        return entityManager
                .createQuery("""
                        SELECT c FROM UserContacts c 
                        WHERE c.owner.id = :userId 
                          AND c.owner.isActive = true
                        """, UserContacts.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void save(UserContacts contact) {
        entityManager.persist(contact);
    }

    public void update(UserContacts contact) {
        entityManager.merge(contact);
    }

    public void delete(UserContacts contact) {
        if (entityManager.contains(contact)) {
            entityManager.remove(contact);
        } else {
            entityManager.remove(entityManager.merge(contact));
        }
        entityManager.flush();
    }
}