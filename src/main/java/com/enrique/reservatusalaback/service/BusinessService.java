package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;

import java.util.List;

public interface BusinessService {
    Business add(Business business);
    List<Business> findAll();
    Business findById(Long id);
    Business update(Business business);
    int deleteById(Long id);
    Room addRoom(Long id, Room room);
    Operation addOperation(Long id, Operation operation);
}
