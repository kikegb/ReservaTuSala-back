package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import com.enrique.reservatusalaback.service.OperationService;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final RoomService roomService;
    private final OperationService operationService;

    @Override
    public Business add(final Business business) {
        if (businessRepository.existsByCifAndEmail(business.getCif(), business.getEmail())) {
            return null;
        }
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
        return 1;
    }

    @Override
    public int addRoom(final Long id, final Room room) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            Room newRoom = roomService.add(room);
            business.getRooms().add(newRoom);
            businessRepository.save(business);
            return 0;
        }
        return 1;
    }

    @Override
    public int addOperation(final Long id, final Operation operation) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            Operation newOperation = operationService.add(operation);
            business.getOperations().add(newOperation);
            businessRepository.save(business);
            return 0;
        }
        return 1;
    }
}
