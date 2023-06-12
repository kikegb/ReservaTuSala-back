package com.enrique.reservatusalaback.repository;


import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Role;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.model.StatusCode;
import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class DbInit {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @PostConstruct
    private void insertUsers() {
        User b1 = new User("87654321X", "Bisnes S.A.", "999999999", "password", "email@example.com", Role.BUSINESS);
        User c1 = new User("87654321X", "Helena Nito", "999999999", "p4ssw0rd", "hnito@gmail.com", Role.CUSTOMER);
        User a1 = new User("87654321X", "Administrator", "999999999", "admin", "admin@admin.com", Role.ADMIN);
        b1 = userService.add(b1);
        c1 = userService.add(c1);
        a1 = userService.add(a1);

        Location l1 = new Location("Lost Avenue", "12", "65428", "Testville", "Segovia", "Spain");
        l1 = locationRepository.save(l1);

        Room r1 = new Room(b1, l1, "Sala reuniones 1", 9.76, 10, 17.5);
        r1 = roomRepository.save(r1);

        Material m1 = new Material("Chair", 5);
        Material m2 = new Material("Whiteboard", 1);
        m1 = roomService.addMaterial(r1.getId(), m1);
        m2 = roomService.addMaterial(r1.getId(), m2);

        LocalTime sStart = LocalTime.of(8, 0);
        LocalTime sEnd = LocalTime.of(18, 0);
        Schedule s1 = new Schedule(0, sStart, sEnd);
        s1 = roomService.addSchedule(r1.getId(), s1);

        LocalDateTime oStart = LocalDateTime.of(2023, 6, 25, 10, 0);
        LocalDateTime oEnd = LocalDateTime.of(2023, 6, 25, 12,0);
        Operation o1 = new Operation(c1, b1, r1, oStart, oEnd, oStart.until(oEnd, ChronoUnit.HOURS), StatusCode.PENDING);
        o1 = operationRepository.save(o1);
    }

    //@PostConstruct
    private void postConstruct() {
        //Create customers
        User c1 = new User("87654321X", "Helena Nito", "999999999", "p4ssw0rd", "hnito@gmail.com", Role.CUSTOMER);
        User c2 = new User("87654321X", "Encarna Vales", "888888888", "p4ssw0rd", "evales@gmail.com", Role.CUSTOMER);
        userRepository.save(c1);
        userRepository.save(c2);

        //Create business
        User b1 = new User("87654321X", "Bisnes S.A.", "999999999", "p4ssw0rd", "bisnes@gmail.com", Role.BUSINESS);
        userRepository.save(b1);

        // Create room with location, schedule and material
        Location l1 = new Location("Lost Avenue", "12", "65428", "Testville", "Segovia", "Spain");
        locationRepository.save(l1);

        double rCost = 17.5;
        Room r1 = new Room(b1, l1, "Sala reuniones 1", 9.76, 10, rCost);

        LocalTime sStart = LocalTime.of(8, 0);
        LocalTime sEnd = LocalTime.of(18, 30);
        Schedule s1 = new Schedule(0, sStart, sEnd);
        r1.setSchedules(List.of(s1));
        scheduleRepository.save(s1);

        Material m1 = new Material("Chair", 10);
        Material m2 = new Material("Table", 1);
        r1.setMaterials(List.of(m1, m2));
        materialRepository.save(m1);
        materialRepository.save(m2);

        // Associate room to business
        b1.setRooms(List.of(r1));
        roomRepository.save(r1);

        // Create an operation associated with a business, a customer and a room
        LocalDateTime oStart = LocalDateTime.of(2022, 11, 23, 15, 0);
        LocalDateTime oEnd = LocalDateTime.of(2022, 11, 23, 18,0);
        Operation o1 = new Operation(c1, b1, r1, oStart, oEnd, oStart.until(oEnd, ChronoUnit.HOURS), StatusCode.PENDING);
        c1.setCustomerOperations(List.of(o1));
        b1.setBusinessOperations(List.of(o1));
        r1.setOperations(List.of(o1));
        operationRepository.save(o1);

        //Update user, user and repository
        userRepository.save(c1);
        userRepository.save(b1);
        roomRepository.save(r1);

    }

    @PreDestroy
    private void destroy() {
        userRepository.deleteAll();
        locationRepository.deleteAll();
        roomRepository.deleteAll();
        scheduleRepository.deleteAll();
        materialRepository.deleteAll();
        operationRepository.deleteAll();
    }

}