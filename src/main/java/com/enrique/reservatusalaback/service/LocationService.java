package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Location;

import java.util.List;

public interface LocationService {
    Location add(Location location);
    List<Location> findAll();
    Location findById(Long id);
    Location update(Location location);
    int deleteById(Long id);
}
