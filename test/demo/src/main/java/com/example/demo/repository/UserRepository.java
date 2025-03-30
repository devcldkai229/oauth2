package com.example.demo.repository;

import com.example.demo.domain.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Accounts, Integer> {

    Optional<Accounts> findByUsername(String username);
}

