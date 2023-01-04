package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Customer;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.service.CustomerService;
import com.enrique.reservatusalaback.service.OperationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OperationService operationService;
    @Override
    public Customer add(final Customer customer) {
        if (customerRepository.existsByCnifAndEmail(customer.getCnif(), customer.getEmail())) {
            return null;
        }
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(final Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer update(final Customer customer) {
        if (customerRepository.existsById(customer.getId())) {
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            customer.setDeleted(true);
            customerRepository.save(customer);
            return 0;
        }
        return 1;
    }

    @Override
    public Operation addOperation(final Long id, final Operation operation) {
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            Operation newOperation = operationService.add(operation);
            customer.getOperations().add(newOperation);
            customerRepository.save(customer);
            return newOperation;
        }
        return null;
    }
}
