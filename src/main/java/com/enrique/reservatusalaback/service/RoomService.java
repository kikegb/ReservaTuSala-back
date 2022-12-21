package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;

import java.util.List;

public interface RoomService {
    Room add(Room room);
    List<Room> findAll();
    Room findById(Long id);
    Room update(Room room);
    int deleteById(Long id);
    Operation addOperation(Long id, Operation operation);
    Schedule addSchedule(Long id, Schedule schedule);
    Material addMaterial(Long id, Material material);
}
