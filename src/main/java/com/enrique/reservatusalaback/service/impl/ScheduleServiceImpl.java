package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.service.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
}
