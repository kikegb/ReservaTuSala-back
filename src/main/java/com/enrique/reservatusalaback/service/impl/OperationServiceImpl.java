package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.service.OperationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

}
