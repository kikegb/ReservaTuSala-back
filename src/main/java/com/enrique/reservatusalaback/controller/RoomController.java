package com.enrique.reservatusalaback.controller;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.service.RoomService;
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
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Room room) {
        return ResponseEntity.ok(roomService.add(room));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping
    public ResponseEntity<?> findById(@Valid @RequestParam Long id) {
        Room room = roomService.findById(id);
        if (Objects.isNull(room)){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(room);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Room room) {
        Room updatedRoom = roomService.update(room);
        if (Objects.isNull(updatedRoom)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (roomService.deleteById(id) > 0) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ResponseCode.OK);
    }

    @PostMapping("/operation")
    public ResponseEntity<?> addOperation(@RequestParam Long id, @Valid @RequestBody Operation operation) {
        Operation addedOperation = roomService.addOperation(id, operation);
        if (Objects.isNull(addedOperation)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> addSchedule(@RequestParam Long id, @Valid @RequestBody Schedule schedule) {
        Schedule addedSchedule = roomService.addSchedule(id, schedule);
        if (Objects.isNull(addedSchedule)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedSchedule);
    }

    @PostMapping("/material")
    public ResponseEntity<?> addMaterial(@RequestParam Long id, @Valid @RequestBody Material material) {
        Material addedMaterial = roomService.addMaterial(id, material);
        if (Objects.isNull(addedMaterial)) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedMaterial);
    }
}
