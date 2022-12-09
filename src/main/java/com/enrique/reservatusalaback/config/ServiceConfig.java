package com.enrique.reservatusalaback.config;

import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.CustomerService;
import com.enrique.reservatusalaback.service.LocationService;
import com.enrique.reservatusalaback.service.impl.BusinessServiceImpl;
import com.enrique.reservatusalaback.service.impl.CustomerServiceImpl;
import com.enrique.reservatusalaback.service.impl.LocationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.enrique.reservatusalaback")
public class ServiceConfig {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Bean
    public BusinessService businessService() {
        return new BusinessServiceImpl(businessRepository);
    }

    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImpl(customerRepository);
    }

    @Bean
    public LocationService locationService() {
        return new LocationServiceImpl(locationRepository);
    }

}
