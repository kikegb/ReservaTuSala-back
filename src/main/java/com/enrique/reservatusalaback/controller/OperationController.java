package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.service.OperationService;
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
@RequestMapping("/operation")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Operation operation) {
        if (operation == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OPERATION, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(operationService.add(operation));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Operation>> findAll() {
        return ResponseEntity.ok(operationService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        Operation operation = operationService.findById(id);
        if (operation == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(operation);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Operation operation) {
        if (operation == null) {
            return new ResponseEntity<>(ResponseCode.NULL_OPERATION, HttpStatus.BAD_REQUEST);
        }
        Operation updatedOperation = operationService.update(operation);
        if (updatedOperation == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedOperation);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (operationService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(ResponseCode.OK);
        }
    }
}
