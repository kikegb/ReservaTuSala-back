package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BusinessService businessService;

    @Override
    public Room add(final Long businessId, final Room room) {
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
    public Room findById(final Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room update(final Room room) {
        if (roomRepository.existsById(room.getId())) {
            return roomRepository.save(room);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
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
    public int addOperation(final Long id, final Operation operation) {
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
    public int addSchedule(final Long id, final Schedule schedule) {
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
    public int addMaterial(final Long id, final Material material) {
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
