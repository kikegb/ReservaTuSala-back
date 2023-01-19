package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByCnifAndEmail(String cnif, String email);
    Optional<User> findByEmail(String email);
}
