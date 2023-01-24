package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceAlreadyExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody User user) throws InstanceAlreadyExistsException {
        User addedUser = userService.add(user);
        if (Objects.isNull(addedUser)) {
            throw new InstanceAlreadyExistsException();
        }
        return ResponseEntity.ok(addedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping
    public ResponseEntity<?> findById(@RequestParam Long id) {
        User user = userService.findById(id);
        if (Objects.isNull(user)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        User updatedUser = userService.update(user);
        if (Objects.isNull(updatedUser)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (userService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }

    @PostMapping("/business/room")
    public ResponseEntity<?> addRoom(@RequestParam Long id, @Valid @RequestBody Room room) {
        Room addedRoom = userService.addRoom(id, room);
        if (Objects.isNull(addedRoom)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedRoom);
    }

    @PostMapping("/business/operation")
    public ResponseEntity<?> addBusinessOperation(@RequestParam Long id, @Valid @RequestBody Operation operation) {
        Operation addedOperation = userService.addBusinessOperation(id, operation);
        if (Objects.isNull(addedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }

    @PostMapping("/customer/operation")
    public ResponseEntity<?> addCustomerOperation(@RequestParam Long id, @Valid @RequestBody Operation operation) {
        Operation addedOperation = userService.addCustomerOperation(id, operation);
        if (Objects.isNull(addedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }
}