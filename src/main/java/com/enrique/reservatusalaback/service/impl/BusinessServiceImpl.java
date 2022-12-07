package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

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
        return businessRepository.save(business);
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Business> result = businessRepository.findById(id);
        if (result.isPresent()) {
            Business business = result.get();
            business.setDeleted(true);
            businessRepository.save(business);
            return 1;
        }
        return -1;
    }

}
