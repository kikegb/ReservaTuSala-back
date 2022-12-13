package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule add(Schedule schedule);
    List<Schedule> findAll();
    Schedule findById(Long id);
    Schedule update(Schedule schedule);
    int deleteById(Long id);
}
