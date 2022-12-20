package com.enrique.reservatusalaback.config;

import com.enrique.reservatusalaback.controller.BusinessController;
import com.enrique.reservatusalaback.controller.CustomerController;
import com.enrique.reservatusalaback.controller.LocationController;
import com.enrique.reservatusalaback.controller.MaterialController;
import com.enrique.reservatusalaback.controller.OperationController;
import com.enrique.reservatusalaback.controller.RoomController;
import com.enrique.reservatusalaback.controller.ScheduleController;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.CustomerService;
import com.enrique.reservatusalaback.service.LocationService;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.ScheduleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Bean
    public BusinessController businessController(BusinessService businessService) {
        return new BusinessController(businessService);
    }

    @Bean
    public CustomerController customerController(CustomerService customerService) {
        return new CustomerController(customerService);
    }

    @Bean
    public LocationController locationController(LocationService locationService) {
        return new LocationController(locationService);
    }

    @Bean
    public MaterialController materialController(MaterialService materialService) {
        return new MaterialController(materialService);
    }
    @Bean
    public OperationController operationController(OperationService operationService) {
        return new OperationController(operationService);
    }

    @Bean
    public RoomController roomController(RoomService roomService) {
        return new RoomController(roomService);
    }

    @Bean
    public ScheduleController scheduleController(ScheduleService scheduleService) {
        return new ScheduleController(scheduleService);
    }
}
