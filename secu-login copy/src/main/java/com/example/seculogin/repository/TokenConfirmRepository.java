package com.example.seculogin.repository;

import com.example.seculogin.entity.TokenConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenConfirmRepository extends JpaRepository<TokenConfirm, Integer> {
    Optional<TokenConfirm> findByToken(String token);
}
