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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Customer customer) {
        Customer addedCustomer = customerService.add(customer);
        if (Objects.isNull(addedCustomer)) {
            return new ResponseEntity<>(ResponseCode.ALREADY_EXISTENT_USER, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(addedCustomer);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Customer customer = customerService.findById(id);
        if (Objects.isNull(customer)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.update(customer);
        if (Objects.isNull(updatedCustomer)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (customerService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }

    @PostMapping("/operation")
    public ResponseEntity<?> addOperation(@RequestParam Long id, @Valid @RequestBody Operation operation) {
        Operation addedOperation = customerService.addOperation(id, operation);
        if (Objects.isNull(addedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }
}
