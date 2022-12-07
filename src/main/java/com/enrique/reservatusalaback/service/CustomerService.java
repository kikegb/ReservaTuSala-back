package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer add(Customer customer);
    List<Customer> findAll();
    Customer findById(Long id);
    Customer update(Customer customer);
    int deleteById(Long id);
}
