package com.enrique.reservatusalaback.config;

import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.CustomerService;
import com.enrique.reservatusalaback.service.LocationService;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.ScheduleService;
import com.enrique.reservatusalaback.service.impl.BusinessServiceImpl;
import com.enrique.reservatusalaback.service.impl.CustomerServiceImpl;
import com.enrique.reservatusalaback.service.impl.LocationServiceImpl;
import com.enrique.reservatusalaback.service.impl.MaterialServiceImpl;
import com.enrique.reservatusalaback.service.impl.OperationServiceImpl;
import com.enrique.reservatusalaback.service.impl.RoomServiceImpl;
import com.enrique.reservatusalaback.service.impl.ScheduleServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public BusinessService businessService(BusinessRepository businessRepository) {
        return new BusinessServiceImpl(businessRepository);
    }

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerServiceImpl(customerRepository);
    }

    @Bean
    public RoomService roomService(RoomRepository roomRepository, BusinessService businessService) {
        return new RoomServiceImpl(roomRepository, businessService);
    }

    @Bean
    public LocationService locationService(LocationRepository locationRepository) {
        return new LocationServiceImpl(locationRepository);
    }

    @Bean
    public MaterialService materialService(MaterialRepository materialRepository, RoomService roomService) {
        return new MaterialServiceImpl(materialRepository, roomService);
    }

    @Bean
    public ScheduleService scheduleService(ScheduleRepository scheduleRepository, RoomService roomService) {
        return new ScheduleServiceImpl(scheduleRepository, roomService);
    }

    @Bean
    public OperationService operationService(OperationRepository operationRepository, BusinessService businessService,
                                             CustomerService customerService, RoomService roomService)
    {
        return new OperationServiceImpl(operationRepository, businessService, customerService, roomService);
    }

}
