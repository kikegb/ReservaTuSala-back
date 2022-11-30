package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.service.LocationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

}
