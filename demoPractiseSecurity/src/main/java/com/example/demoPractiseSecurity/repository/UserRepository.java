package com.example.demoPractiseSecurity.repository;


import com.example.demoPractiseSecurity.models.Userrs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Userrs, Long> {
    Optional<Userrs> findByUsername(String username);
}
