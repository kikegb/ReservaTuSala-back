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
    public Operation add(Long businessId, Long customerId, Long roomId, Operation operation) {
        Operation newOperation = operationRepository.save(operation);
        int resultBusiness = businessService.addOperation(businessId, operation);
        int resultCustomer = customerService.addOperation(customerId, operation);
        int resultRoom = roomService.addOperation(roomId, operation);
        if (resultBusiness < 0 || resultCustomer < 0 || resultRoom < 0) {
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
    public Operation findById(Long id) {
        return operationRepository.findById(id).orElse(null);
    }

    @Override
    public Operation update(Operation operation) {
        if (operationRepository.existsById(operation.getId())) {
            return operationRepository.save(operation);
        }
        return null;
    }

    @Override
    public int deleteById(Long id) {
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
