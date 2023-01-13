package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;

import java.util.List;

public interface UserService {
    User add(User user);

    List<User> findAll();

    User findById(Long id);

    User update(User user);

    int deleteById(Long id);

    Room addRoom(Long id, Room room);

    Operation addBusinessOperation(Long id, Operation operation);

    Operation addCustomerOperation(Long id, Operation operation);
}
