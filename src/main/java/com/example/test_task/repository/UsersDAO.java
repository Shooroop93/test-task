package com.example.test_task.repository;

import com.example.test_task.model.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Users> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Users.class, id));
    }

    public Optional<Users> findByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public List<Users> findAll() {
        return entityManager
                .createQuery("SELECT u FROM Users u", Users.class)
                .getResultList();
    }

    public void save(Users user) {
        entityManager.persist(user);
    }

    public Users update(Users user) {
        return entityManager.merge(user);
    }

    public void delete(Users user) {
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }
}