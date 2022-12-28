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

import java.util.List;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Room room) {
        if (room == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ROOM, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(roomService.add(room));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Room>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        Room room = roomService.findById(id);
        if (room == null){
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(room);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Room room) {
        if (room == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ROOM, HttpStatus.BAD_REQUEST);
        }
        Room updatedRoom = roomService.update(room);
        if (updatedRoom == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping
    public ResponseEntity<ResponseCode> deleteById(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (roomService.deleteById(id) > 0) {
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
        Operation addedOperation = roomService.addOperation(id, operation);
        if (addedOperation == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedOperation);
    }

    @PostMapping("/addSchedule")
    public ResponseEntity<?> addSchedule(@RequestParam Long id, @RequestBody Schedule schedule) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (schedule == null) {
            return new ResponseEntity<>(ResponseCode.NULL_SCHEDULE, HttpStatus.BAD_REQUEST);
        }
        Schedule addedSchedule = roomService.addSchedule(id, schedule);
        if (addedSchedule == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedSchedule);
    }

    @PostMapping("/addMaterial")
    public ResponseEntity<?> addMaterial(@RequestParam Long id, @RequestBody Material material) {
        if (id == null) {
            return new ResponseEntity<>(ResponseCode.NULL_ID, HttpStatus.BAD_REQUEST);
        }
        if (material == null) {
            return new ResponseEntity<>(ResponseCode.NULL_MATERIAL, HttpStatus.BAD_REQUEST);
        }
        Material addedMaterial = roomService.addMaterial(id, material);
        if (addedMaterial == null) {
            return new ResponseEntity<>(ResponseCode.NOT_FOUND_ID, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(addedMaterial);
    }
}
