package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.ScheduleService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final MaterialService materialService;
    private final ScheduleService scheduleService;
    private final OperationService operationService;

    @Override
    public Room add(final Room room) {
        return roomRepository.save(room);
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
        return 1;
    }

    @Override
    public int addOperation(final Long id, final Operation operation) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            Operation newOperation = operationService.add(operation);
            if (newOperation == null) {
                return 2;
            }
            room.getOperations().add(newOperation);
            roomRepository.save(room);
            return 0;
        }
        return 1;
    }

    @Override
    public int addSchedule(final Long id, final Schedule schedule) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            Schedule newSchedule = scheduleService.add(schedule);
            if (newSchedule == null) {
                return 2;
            }
            room.getSchedules().add(newSchedule);
            roomRepository.save(room);
            return 0;
        }
        return 1;
    }

    @Override
    public int addMaterial(final Long id, final Material material) {
        Optional<Room> result = roomRepository.findById(id);
        if (result.isPresent()) {
            Room room = result.get();
            Material newMaterial = materialService.add(material);
            if (newMaterial == null) {
                return 2;
            }
            room.getMaterials().add(newMaterial);
            roomRepository.save(room);
            return 0;
        }
        return 1;
    }
}
