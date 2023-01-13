package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    boolean existsByCifAndEmail(String cif, String email);
    Optional<Business> findByEmail(String email);
}