package com.example.test_task.repository;

import com.example.test_task.model.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, Long> {

}