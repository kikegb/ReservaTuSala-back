package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.service.LocationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location add(final Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(final Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @Override
    public Location update(final Location location) {
        if (locationRepository.existsById(location.getId())) {
            return locationRepository.save(location);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Location> result = locationRepository.findById(id);
        if (result.isPresent()) {
            Location location = result.get();
            location.setDeleted(true);
            locationRepository.save(location);
            return 0;
        }
        return -1;
    }
}
