package com.enrique.reservatusalaback.repository;

import com.enrique.reservatusalaback.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}