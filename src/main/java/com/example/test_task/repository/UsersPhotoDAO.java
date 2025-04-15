package com.example.test_task.repository;

import com.example.test_task.model.UsersPhoto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsersPhotoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<UsersPhoto> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UsersPhoto.class, id));
    }

    public void save(UsersPhoto photo) {
        entityManager.persist(photo);
    }

    public UsersPhoto update(UsersPhoto photo) {
        return entityManager.merge(photo);
    }

    public void delete(UsersPhoto photo) {
        entityManager.remove(entityManager.contains(photo) ? photo : entityManager.merge(photo));
    }
}