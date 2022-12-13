package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.CustomerService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final BusinessService businessService;
    private final CustomerService customerService;
    private final RoomService roomService;

    @Override
    public Operation add(final Long businessId, final Long customerId, final Long roomId, final Operation operation) {
        Operation newOperation = operationRepository.save(operation);
        int addedToBusiness = businessService.addOperation(businessId, operation);
        int addedToCustomer = customerService.addOperation(customerId, operation);
        int addedToRoom = roomService.addOperation(roomId, operation);
        if (addedToBusiness < 0 || addedToCustomer < 0 || addedToRoom < 0) {
            operationRepository.deleteById(newOperation.getId());
            return null;
        }
        return newOperation;
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
            Operation operation = result.get();
            operation.setDeleted(true);
            operationRepository.save(operation);
            return 0;
        }
        return -1;
    }
}
