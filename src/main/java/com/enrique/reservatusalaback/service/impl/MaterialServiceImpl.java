package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.service.MaterialService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

}
