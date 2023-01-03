package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.service.BusinessService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Business business) {
        Business addedBusiness = businessService.add(business);
        if (Objects.isNull(addedBusiness)) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_USER, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(addedBusiness);
    }

    @GetMapping
    public ResponseEntity<List<Business>> findAll() {
        return ResponseEntity.ok(businessService.findAll());
    }

    @GetMapping("/byId")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Business business = businessService.findById(id);
        if (Objects.isNull(business)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(business);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Business business) {
        Business updatedBusiness = businessService.update(business);
        if (Objects.isNull(updatedBusiness)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (businessService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }

    @PostMapping("/room")
    public ResponseEntity<?> addRoom(@RequestParam Long id, @Valid @RequestBody Room room) {
        Room addedRoom = businessService.addRoom(id, room);
        if (Objects.isNull(addedRoom)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedRoom);
    }

    @PostMapping("/operation")
    public ResponseEntity<?> addOperation(@RequestParam Long id, @Valid @RequestBody Operation operation) {
        Operation addedOperation = businessService.addOperation(id, operation);
        if (Objects.isNull(addedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }
}