package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}