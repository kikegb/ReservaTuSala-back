package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.service.MaterialService;
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
@RequestMapping("/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Material material) {
        return ResponseEntity.ok(materialService.add(material));
    }

    @GetMapping
    public ResponseEntity<List<Material>> findAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @GetMapping("/byId")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Material material = materialService.findById(id);
        if (Objects.isNull(material)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(material);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Material material) {
        Material updatedMaterial = materialService.update(material);
        if (Objects.isNull(updatedMaterial)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedMaterial);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (materialService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }
}
