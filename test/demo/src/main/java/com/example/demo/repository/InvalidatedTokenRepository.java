package com.example.demo.repository;

import com.example.demo.domain.model.InvalidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidationToken, String> {
}
