package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.service.LocationService;
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
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.add(location));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Location>> findAll() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        Location location = locationService.findById(id);
        if (location == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(location);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Location location) {
        Location updatedLocation = locationService.update(location);
        if (updatedLocation == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (locationService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(ResponseCode.OK);
        }
    }
}
