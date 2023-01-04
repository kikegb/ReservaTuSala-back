package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCnifAndEmail(String cnif, String Email);
}