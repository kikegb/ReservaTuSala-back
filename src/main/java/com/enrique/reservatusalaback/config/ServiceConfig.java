package com.enrique.reservatusalaback.config;

import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.repository.UserRepository;
import com.enrique.reservatusalaback.service.LocationService;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.ScheduleService;
import com.enrique.reservatusalaback.service.UserService;
import com.enrique.reservatusalaback.service.impl.LocationServiceImpl;
import com.enrique.reservatusalaback.service.impl.MaterialServiceImpl;
import com.enrique.reservatusalaback.service.impl.OperationServiceImpl;
import com.enrique.reservatusalaback.service.impl.RoomServiceImpl;
import com.enrique.reservatusalaback.service.impl.ScheduleServiceImpl;
import com.enrique.reservatusalaback.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public LocationService locationService(LocationRepository locationRepository) {
        return new LocationServiceImpl(locationRepository);
    }

    @Bean
    public MaterialService materialService(MaterialRepository materialRepository) {
        return new MaterialServiceImpl(materialRepository);
    }

    @Bean
    public ScheduleService scheduleService(ScheduleRepository scheduleRepository) {
        return new ScheduleServiceImpl(scheduleRepository);
    }

    @Bean
    public OperationService operationService(OperationRepository operationRepository)
    {
        return new OperationServiceImpl(operationRepository);
    }

    @Bean
    public UserService UserService(UserRepository userRepository, RoomService roomService,
                                   OperationService operationService) {
        return new UserServiceImpl(userRepository, roomService, operationService);
    }

    @Bean
    public RoomService roomService(RoomRepository roomRepository, MaterialService materialService,
                                   ScheduleService scheduleService, OperationService operationService) {
        return new RoomServiceImpl(roomRepository, materialService, scheduleService, operationService);
    }

}
