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

import java.util.List;

@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Business business) {
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
        Business business = businessService.findById(id);
        if (business == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(business);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Business business) {
        Business updatedBusiness = businessService.update(business);
        if (updatedBusiness == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedBusiness);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (businessService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(ResponseCode.OK);
        }
    }

    @PostMapping("/addRoom")
    public ResponseEntity<ResponseCode> addRoom(@RequestParam Long id, @RequestBody Room room) {
        int resultCode = businessService.addRoom(id, room);
        if (resultCode == 1) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        if (resultCode == 2) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_ROOM, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }

    @PostMapping("/addOperation")
    public ResponseEntity<ResponseCode> addOperation(@RequestParam Long id, @RequestBody Operation operation) {
        int resultCode = businessService.addOperation(id, operation);
        if (resultCode == 1) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        if (resultCode == 2) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_OPERATION, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }
}
