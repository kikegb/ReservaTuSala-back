package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {

}