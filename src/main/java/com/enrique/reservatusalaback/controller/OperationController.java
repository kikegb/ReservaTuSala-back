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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Operation operation) {
        return ResponseEntity.ok(operationService.add(operation));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Operation>> findAll() {
        return ResponseEntity.ok(operationService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Operation operation = operationService.findById(id);
        if (Objects.isNull(operation)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(operation);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Operation operation) {
        Operation updatedOperation = operationService.update(operation);
        if (Objects.isNull(updatedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedOperation);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (operationService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }
}
