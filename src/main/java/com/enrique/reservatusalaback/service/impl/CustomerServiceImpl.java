package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /*
    @Override
    public int softDelete(Integer id) {
        return 0;
    }
    */
}
