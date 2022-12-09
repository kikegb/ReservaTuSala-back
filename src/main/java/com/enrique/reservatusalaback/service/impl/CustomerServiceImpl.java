package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Customer;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Override
    public Customer add(final Customer customer) {
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
        return -1;
    }

    @Override
    public int addOperation(Long id, Operation operation) {
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            customer.getOperations().add(operation);
            customerRepository.save(customer);
            return 0;
        }
        return -1;
    }
}
