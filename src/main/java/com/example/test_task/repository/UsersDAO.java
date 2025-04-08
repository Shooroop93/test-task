package com.example.test_task.repository;

import com.example.test_task.model.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Users> findById(Long id) {
        return entityManager.createQuery("""
                        SELECT u FROM Users u
                        LEFT JOIN FETCH u.contacts
                        LEFT JOIN FETCH u.photo
                        WHERE u.id = :id
                        """, Users.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public List<Users> findAll() {
        return entityManager.createQuery("""
                        SELECT DISTINCT u FROM Users u
                        LEFT JOIN FETCH u.contacts
                        LEFT JOIN FETCH u.photo
                        """, Users.class)
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

    public Optional<Users> findByLogin(String login) {
        try {
            Users user = entityManager.createQuery("""
                            SELECT u FROM Users u 
                            WHERE u.login = :login
                            """, Users.class)
                    .setParameter("login", login)
                    .getSingleResult();

            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}