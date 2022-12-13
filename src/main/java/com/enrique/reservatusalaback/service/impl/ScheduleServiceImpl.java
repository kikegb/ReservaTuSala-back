package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.service.RoomService;
import com.enrique.reservatusalaback.service.ScheduleService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RoomService roomService;

    @Override
    public Schedule add(Long roomId, Schedule schedule) {
        Schedule newSchedule = scheduleRepository.save(schedule);
        int result = roomService.addSchedule(roomId, newSchedule);
        if (result < 0) {
            scheduleRepository.deleteById(newSchedule.getId());
            return null;
        }
        return newSchedule;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public Schedule update(Schedule schedule) {
        if (scheduleRepository.existsById(schedule.getId())) {
            return scheduleRepository.save(schedule);
        }
        return null;
    }

    @Override
    public int deleteById(Long id) {
        Optional<Schedule> result = scheduleRepository.findById(id);
        if (result.isPresent()) {
            Schedule schedule = result.get();
            schedule.setDeleted(true);
            scheduleRepository.save(schedule);
            return 0;
        }
        return -1;
    }
}
