package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.repository.UserRepository;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoomService roomService;
    private final OperationService operationService;

    @Override
    public User add(final User user) {
        if (userRepository.existsByCnifAndEmail(user.getCnif(), user.getEmail())) {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(final Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User update(final User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            User user = result.get();
            user.setDeleted(true);
            userRepository.save(user);
            return 0;
        }
        return 1;
    }

    @Override
    public Room addRoom(final Long id, final Room room) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            User user = result.get();
            Room newRoom = roomService.add(room);
            user.getRooms().add(newRoom);
            userRepository.save(user);
            return newRoom;
        }
        return null;
    }

    @Override
    public Operation addBusinessOperation(final Long id, final Operation operation) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            User user = result.get();
            Operation newOperation = operationService.add(operation);
            user.getBusinessOperations().add(newOperation);
            userRepository.save(user);
            return newOperation;
        }
        return null;
    }

    @Override
    public Operation addCustomerOperation(final Long id, final Operation operation) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            User user = result.get();
            Operation newOperation = operationService.add(operation);
            user.getCustomerOperations().add(newOperation);
            userRepository.save(user);
            return newOperation;
        }
        return null;
    }
}
