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
        return findById(id, false);
    }

    public Optional<Users> findById(Long id, boolean includeInactive) {
        String query = """
                SELECT u FROM Users u
                LEFT JOIN FETCH u.contacts
                LEFT JOIN FETCH u.photo
                WHERE u.id = :id
                """ + (includeInactive ? "" : " AND u.isActive = true");

        return entityManager.createQuery(query, Users.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public List<Users> findAll() {
        return findAll(true);
    }

    public List<Users> findAll(boolean includeInactive) {
        String query = """
                SELECT DISTINCT u FROM Users u
                LEFT JOIN FETCH u.contacts
                LEFT JOIN FETCH u.photo
                """ + (includeInactive ? "" : " WHERE u.isActive = true");

        return entityManager.createQuery(query, Users.class)
                .getResultList();
    }

    public Optional<Users> findByLogin(String login) {
        return findByLogin(login, true);
    }

    public Optional<Users> findByLogin(String login, boolean includeInactive) {
        String query = """
                SELECT u FROM Users u
                WHERE u.login = :login
                """ + (includeInactive ? "" : " AND u.isActive = true");

        try {
            Users user = entityManager.createQuery(query, Users.class)
                    .setParameter("login", login)
                    .getSingleResult();

            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void save(Users user) {
        user.setIsActive(true);
        entityManager.persist(user);
    }

    public Users update(Users user) {
        return entityManager.merge(user);
    }

    public void deactivateUser(Users user) {
        user.setIsActive(false);
        entityManager.merge(user);
    }
}