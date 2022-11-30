package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

}
