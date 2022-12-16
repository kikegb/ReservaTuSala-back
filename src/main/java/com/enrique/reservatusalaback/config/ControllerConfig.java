package com.enrique.reservatusalaback.config;

import com.enrique.reservatusalaback.controller.BusinessController;
import com.enrique.reservatusalaback.service.BusinessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Bean
    public BusinessController businessController(BusinessService businessService) {
        return new BusinessController(businessService);
    }
}
