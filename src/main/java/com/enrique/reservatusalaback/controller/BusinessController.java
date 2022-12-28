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

import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Business business) {
        if (business == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OBJECT, HttpStatus.BAD_REQUEST);
        }
        Business addedBusiness = businessService.add(business);
        if (addedBusiness == null) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_USER, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(addedBusiness);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Business>> findAll() {
        return ResponseEntity.ok(businessService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        Business business = businessService.findById(id);
        if (business == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(business);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Business business) {
        if (business == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OBJECT, HttpStatus.BAD_REQUEST);
        }
        Business updatedBusiness = businessService.update(business);
        if (updatedBusiness == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (businessService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(ResponseCode.OK);
        }
    }

    @PostMapping("/addRoom")
    public ResponseEntity<?> addRoom(@RequestParam Long id, @RequestBody Room room) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (room == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OBJECT, HttpStatus.BAD_REQUEST);
        }
        Room addedRoom = businessService.addRoom(id, room);
        if (addedRoom == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedRoom);
    }

    @PostMapping("/addOperation")
    public ResponseEntity<?> addOperation(@RequestParam Long id, @RequestBody Operation operation) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (operation == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OBJECT, HttpStatus.BAD_REQUEST);
        }
        Operation addedOperation = businessService.addOperation(id, operation);
        if (addedOperation == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }
}