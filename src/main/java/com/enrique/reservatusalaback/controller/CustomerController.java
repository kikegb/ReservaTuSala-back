package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Customer;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.service.CustomerService;
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
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Customer customer) {
        if (customer == null) {
            return new ResponseEntity<>(ResponseCode.NULL_CUSTOMER, HttpStatus.BAD_REQUEST);
        }
        Customer addedCustomer = customerService.add(customer);
        if (addedCustomer == null) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_USER, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(addedCustomer);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Customer customer) {
        if (customer == null) {
            return new ResponseEntity<>(ResponseCode.NULL_CUSTOMER, HttpStatus.BAD_REQUEST);
        }
        Customer updatedCustomer = customerService.update(customer);
        if (updatedCustomer == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (customerService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(ResponseCode.OK);
        }
    }

    @PostMapping("/addOperation")
    public ResponseEntity<?> addOperation(@RequestParam Long id, @RequestBody Operation operation) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (operation == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OPERATION, HttpStatus.BAD_REQUEST);
        }
        Operation addedOperation = customerService.addOperation(id, operation);
        if (addedOperation == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }
}
