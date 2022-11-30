package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.*;
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
/*
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BusinessRepository businessRepository;
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

    @PostConstruct
    private void postConstruct() {
        Customer c1 = new Customer("87654321X", "Helena Nito", "999999999", "p4ssw0rd", "hnito@gmail.com");
        Customer c2 = new Customer("87654321X", "Encarna Vales", "888888888", "p4ssw0rd", "evales@gmail.com");
        customerRepository.save(c1);
        customerRepository.save(c2);

        Business b1 = new Business("87654321X", "Bisnes S.A.", "999999999", "p4ssw0rd", "bisnes@gmail.com");
        businessRepository.save(b1);

        Location l1 = new Location("Lost Avenue", "12", "65428", "Testville", "Segovia", "Spain");
        locationRepository.save(l1);

        double rCost = 17.5;
        Room r1 = new Room(l1, "Sala reuniones 1", 9.76, rCost);
        roomRepository.save(r1);

        LocalTime sStart = LocalTime.of(8, 0);
        LocalTime sEnd = LocalTime.of(18, 30);
        Schedule s1 = new Schedule(r1, 0, sStart, sEnd);
        r1.setSchedule(List.of(s1));
        scheduleRepository.save(s1);

        Material m1 = new Material("Chair", 10);
        Material m2 = new Material("Table", 1);
        materialRepository.save(m1);
        materialRepository.save(m2);
        r1.setMaterials(List.of(m1, m2));

        b1.setRooms(List.of(r1));

        LocalDateTime oStart = LocalDateTime.of(2022, 11, 23, 15, 0);
        LocalDateTime oEnd = LocalDateTime.of(2022, 11, 23, 18,0);
        Operation o1 = new Operation(oStart, oEnd, oStart.until(oEnd, ChronoUnit.HOURS), 0);
        operationRepository.save(o1);
        c1.setOperations(List.of(o1));
        b1.setOperations(List.of(o1));
        r1.setOperations(List.of(o1));

    }

    @PreDestroy
    private void destroy() {
        customerRepository.deleteAll();
        businessRepository.deleteAll();
        locationRepository.deleteAll();
        roomRepository.deleteAll();
        scheduleRepository.deleteAll();
        materialRepository.deleteAll();
        operationRepository.deleteAll();
    }
 */
}
