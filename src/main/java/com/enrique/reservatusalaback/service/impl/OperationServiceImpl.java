package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.service.OperationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Override
    public Operation add(final Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> findAll() {
        return operationRepository.findAll();
    }

    @Override
    public Operation findById(final Long id) {
        return operationRepository.findById(id).orElse(null);
    }

    @Override
    public Operation update(final Operation operation) {
        if (operationRepository.existsById(operation.getId())) {
            return operationRepository.save(operation);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Operation> result = operationRepository.findById(id);
        if (result.isPresent()) {
            operationRepository.deleteById(id);
            return 0;
        }
        return 1;
    }
}
