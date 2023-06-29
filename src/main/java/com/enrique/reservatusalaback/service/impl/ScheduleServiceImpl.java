package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.service.ScheduleService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public Schedule add(final Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findById(final Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public Schedule update(final Schedule schedule) {
        if (scheduleRepository.existsById(schedule.getId())) {
            return scheduleRepository.save(schedule);
        }
        return null;
    }

    @Override
    public int deleteById(final Long id) {
        Optional<Schedule> result = scheduleRepository.findById(id);
        if (result.isPresent()) {
            scheduleRepository.deleteById(id);
            return 0;
        }
        return 1;
    }
}
