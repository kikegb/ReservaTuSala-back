package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.model.Schedule;
import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.service.impl.RoomServiceImpl;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private OperationService operationService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private MaterialService materialService;
    @InjectMocks
    private RoomServiceImpl roomService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Room> roomCaptor;

    @DisplayName("Test add a room")
    @Test
    public void whenAddingNewRoom_thenReturnRoom() {
        Room room = mockGenerator.nextObject(Room.class);
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(room, roomService.add(room));
    }

    @DisplayName("Test find all rooms")
    @Test
    public void whenFindAllRoomes_thenReturnListWithAllRoomes() {
        List<Room> rooms = mockGenerator.objects(Room.class, 5).toList();
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.findAll();
        assertEquals(5, result.size());
        assertEquals(rooms, result);
    }

    @DisplayName("Test find room valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnRoomWithThatId() {
        Room room = mockGenerator.nextObject(Room.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        assertEquals(room, roomService.findById(room.getId()));
    }

    @DisplayName("Test find room invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Room room = mockGenerator.nextObject(Room.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        assertNull(roomService.findById(room.getId()));
    }

    @DisplayName("Test update existent room")
    @Test
    public void givenExistentRoom_whenUpdate_thenReturnUpdatedRoom() {
        Room room = mockGenerator.nextObject(Room.class);
        Room updatedRoom = mockGenerator.nextObject(Room.class);
        updatedRoom.setId(room.getId());
        when(roomRepository.existsById(room.getId())).thenReturn(true);
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(room, roomService.update(updatedRoom));
        assertEquals(updatedRoom, roomService.update(updatedRoom));
    }

    @DisplayName("Test update non-existent room")
    @Test
    public void givenNonExistentRoom_whenUpdate_thenDontSave_andReturnNull() {
        Room room = mockGenerator.nextObject(Room.class);
        Room updatedRoom = mockGenerator.nextObject(Room.class);
        updatedRoom.setId(room.getId());
        when(roomRepository.existsById(room.getId())).thenReturn(false);

        verify(roomRepository, never()).save(updatedRoom);
        assertNull(roomService.update(updatedRoom));
    }

    @DisplayName("Test delete room valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Room room = mockGenerator.nextObject(Room.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        assertEquals(0, roomService.deleteById(room.getId()));
        verify(roomRepository).deleteById(room.getId());
    }

    @DisplayName("Test delete room invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Room room = mockGenerator.nextObject(Room.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        assertEquals(1, roomService.deleteById(room.getId()));
        verify(roomRepository, never()).deleteById(room.getId());
    }

    @DisplayName("Test add operation successfully")
    @Test
    public void givenValidId_whenAddingAOperation_thenRoomHasNewOperation_andReturnAddedOperation() {
        Room room = mockGenerator.nextObject(Room.class);
        int oldOperationsSize = room.getOperations().size();
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());
        when(operationService.add(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(operation, roomService.addOperation(room.getId(), operation));
        verify(roomRepository).save(roomCaptor.capture());
        assertEquals(oldOperationsSize + 1, roomCaptor.getValue().getOperations().size());
    }

    @DisplayName("Test add operation invalid id")
    @Test
    public void givenInvalidId_whenAddingAOperation_thenNotAddOperation_andReturnNull() {
        Room room = mockGenerator.nextObject(Room.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        assertNull(roomService.addOperation(room.getId(), operation));
        verify(roomRepository, never()).save(room);
        verify(operationService, never()).add(operation);
    }

    @DisplayName("Test add schedule successfully")
    @Test
    public void givenValidId_whenAddingASchedule_thenRoomHasNewSchedule_andReturnAddedSchedule() {
        Room room = mockGenerator.nextObject(Room.class);
        int oldSchedulesSize = room.getSchedules().size();
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());
        when(scheduleService.add(any(Schedule.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(schedule, roomService.addSchedule(room.getId(), schedule));
        verify(roomRepository).save(roomCaptor.capture());
        assertEquals(oldSchedulesSize + 1, roomCaptor.getValue().getSchedules().size());
    }

    @DisplayName("Test add schedule invalid id")
    @Test
    public void givenInvalidId_whenAddingASchedule_thenNotAddSchedule_andReturnNull() {
        Room room = mockGenerator.nextObject(Room.class);
        Schedule schedule = mockGenerator.nextObject(Schedule.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        assertNull(roomService.addSchedule(room.getId(), schedule));
        verify(roomRepository, never()).save(room);
        verify(scheduleService, never()).add(schedule);
    }

    @DisplayName("Test add material successfully")
    @Test
    public void givenValidId_whenAddingAMaterial_thenRoomHasNewMaterial_andReturnAddedMaterial() {
        Room room = mockGenerator.nextObject(Room.class);
        int oldMaterialsSize = room.getMaterials().size();
        Material material = mockGenerator.nextObject(Material.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());
        when(materialService.add(any(Material.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(material, roomService.addMaterial(room.getId(), material));
        verify(roomRepository).save(roomCaptor.capture());
        assertEquals(oldMaterialsSize + 1, roomCaptor.getValue().getMaterials().size());
    }

    @DisplayName("Test add material invalid id")
    @Test
    public void givenInvalidId_whenAddingAMaterial_thenNotAddMaterial_andReturnNull() {
        Room room = mockGenerator.nextObject(Room.class);
        Material material = mockGenerator.nextObject(Material.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        assertNull(roomService.addMaterial(room.getId(), material));
        verify(roomRepository, never()).save(room);
        verify(materialService, never()).add(material);
    }
}
