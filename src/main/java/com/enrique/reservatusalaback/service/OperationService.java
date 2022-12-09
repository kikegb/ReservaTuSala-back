package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Operation;

import java.util.List;

public interface OperationService {
    Operation add(Long businessId, Long customerId, Long roomId, Operation operation);
    List<Operation> findAll();
    Operation findById(Long id);
    Operation update(Operation operation);
    int deleteById(Long id);

}
