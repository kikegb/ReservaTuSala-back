package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @Override
    public Location add(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @Override
    public Location update(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public int deleteById(Long id) {
        Optional<Location> result = locationRepository.findById(id);
        if (result.isPresent()) {
            Location location = result.get();
            location.setDeleted(true);
            locationRepository.save(location);
            return 1;
        }
        return -1;
    }
}
