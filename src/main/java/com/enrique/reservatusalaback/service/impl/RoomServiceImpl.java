package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BusinessService businessService;

    @Override
    public Room add(Long businessId, Room room) {
        Room newRoom = roomRepository.save(room);
        int result = businessService.addRoom(businessId, newRoom);
        if (result < 0) {
            roomRepository.deleteById(newRoom.getId());
            return null;
        }
        return newRoom;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room update(Room room) {
        if (roomRepository.existsById(room.getId())) {
            return roomRepository.save(room);
        }
        return null;
    }

    @Override
    public int deleteById(Long id) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            room.setDeleted(true);
            roomRepository.save(room);
            return 0;
        }
        return -1;
    }

    @Override
    public int addOperation(Long id, Operation operation) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            room.getOperations().add(operation);
            roomRepository.save(room);
            return 0;
        }
        return -1;
    }

    @Override
    public int addSchedule(Long id, Schedule schedule) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            room.getSchedule().add(schedule);
            roomRepository.save(room);
            return 0;
        }
        return -1;
    }

    @Override
    public int addMaterial(Long id, Material material) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            room.getMaterials().add(material);
            roomRepository.save(room);
            return 0;
        }
        return -1;
    }
}
