package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;

    @Autowired
    public BusinessServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business add(final Business business) {
        return businessRepository.save(business);
    }

    @Override
    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    @Override
    public Business findById(final Long id) {
        return businessRepository.findById(id).orElse(null);
    }

    @Override
    public Business update(final Business business) {
        if (businessRepository.existsById(business.getId())) {
            return businessRepository.save(business);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            business.setDeleted(true);
            businessRepository.save(business);
            return 0;
        }
        return -1;
    }

    @Override
    public int addRoom(Long id, Room room) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            business.getRooms().add(room);
            businessRepository.save(business);
            return 0;
        }
        return -1;
    }

    @Override
    public int addOperation(Long id, Operation operation) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            business.getOperations().add(operation);
            businessRepository.save(business);
            return 0;
        }
        return -1;
    }
}
