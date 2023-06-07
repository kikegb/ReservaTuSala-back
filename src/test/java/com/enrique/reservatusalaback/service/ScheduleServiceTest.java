package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.ScheduleRepository;
import com.enrique.reservatusalaback.service.impl.ScheduleServiceImpl;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Schedule> scheduleCaptor;

    @DisplayName("Test add a schedule")
    @Test
    public void whenAddingNewSchedule_thenReturnSchedule() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(scheduleRepository.save(any(Schedule.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(schedule, scheduleService.add(schedule));
    }

    @DisplayName("Test find all schedules")
    @Test
    public void whenFindAllSchedulees_thenReturnListWithAllSchedulees() {
        List<Schedule> schedules = mockGenerator.objects(Schedule.class, 5).toList();
        when(scheduleRepository.findAll()).thenReturn(schedules);

        List<Schedule> result = scheduleService.findAll();
        assertEquals(5, result.size());
        assertEquals(schedules, result);
    }

    @DisplayName("Test find schedule valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnScheduleWithThatId() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));

        assertEquals(schedule, scheduleService.findById(schedule.getId()));
    }

    @DisplayName("Test find schedule invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.empty());

        assertNull(scheduleService.findById(schedule.getId()));
    }

    @DisplayName("Test update existent schedule")
    @Test
    public void givenExistentSchedule_whenUpdate_thenReturnUpdatedSchedule() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        Schedule updatedSchedule = mockGenerator.nextObject(Schedule.class);
        updatedSchedule.setId(schedule.getId());
        when(scheduleRepository.existsById(schedule.getId())).thenReturn(true);
        when(scheduleRepository.save(any(Schedule.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(schedule, scheduleService.update(updatedSchedule));
        assertEquals(updatedSchedule, scheduleService.update(updatedSchedule));
    }

    @DisplayName("Test update non-existent schedule")
    @Test
    public void givenNonExistentSchedule_whenUpdate_thenDontSave_andReturnNull() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        Schedule updatedSchedule = mockGenerator.nextObject(Schedule.class);
        updatedSchedule.setId(schedule.getId());
        when(scheduleRepository.existsById(schedule.getId())).thenReturn(false);

        verify(scheduleRepository, never()).save(updatedSchedule);
        assertNull(scheduleService.update(updatedSchedule));
    }

    @DisplayName("Test delete schedule valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, scheduleService.deleteById(schedule.getId()));
        verify(scheduleRepository).deleteById(schedule.getId());
    }

    @DisplayName("Test delete schedule invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.empty());

        assertEquals(1, scheduleService.deleteById(schedule.getId()));
        verify(scheduleRepository, never()).deleteById(schedule.getId());
    }
}
