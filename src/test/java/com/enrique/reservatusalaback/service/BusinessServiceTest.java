package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.impl.BusinessServiceImpl;
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
public class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private OperationService operationService;
    @InjectMocks
    private BusinessServiceImpl businessService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Business> businessCaptor;

    @DisplayName("Test add a business")
    @Test
    public void whenAddingNewBusiness_thenReturnBusiness() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.existsByCifAndEmail(business.getCif(), business.getEmail())).thenReturn(false);
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(business, businessService.add(business));
    }

    @DisplayName("Test add already existent business")
    @Test
    public void whenAddingAlreadyExistentBusiness_thenReturnNull() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.existsByCifAndEmail(business.getCif(), business.getEmail())).thenReturn(true);

        assertNull(businessService.add(business));
        verify(businessRepository, never()).save(business);
    }

    @DisplayName("Test find all businesses")
    @Test
    public void whenFindAllBusinesses_thenReturnListWithAllBusinesses() {
        List<Business> businesses = mockGenerator.objects(Business.class, 5).toList();
        when(businessRepository.findAll()).thenReturn(businesses);

        List<Business> result = businessService.findAll();
        assertEquals(5, result.size());
        assertEquals(businesses, result);
    }

    @DisplayName("Test find business valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnBusinessWithThatId() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.of(business));

        assertEquals(business, businessService.findById(business.getId()));
    }

    @DisplayName("Test find business invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.empty());

        assertNull(businessService.findById(business.getId()));
    }

    @DisplayName("Test update existent business")
    @Test
    public void givenExistentBusiness_whenUpdate_thenReturnUpdatedBusiness() {
        Business business = mockGenerator.nextObject(Business.class);
        Business updatedBusiness = mockGenerator.nextObject(Business.class);
        updatedBusiness.setId(business.getId());
        when(businessRepository.existsById(business.getId())).thenReturn(true);
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(business, businessService.update(updatedBusiness));
        assertEquals(updatedBusiness, businessService.update(updatedBusiness));
    }

    @DisplayName("Test update non-existent business")
    @Test
    public void givenNonExistentBusiness_whenUpdate_thenDontSave_andReturnNull() {
        Business business = mockGenerator.nextObject(Business.class);
        Business updatedBusiness = mockGenerator.nextObject(Business.class);
        updatedBusiness.setId(business.getId());
        when(businessRepository.existsById(business.getId())).thenReturn(false);

        verify(businessRepository, never()).save(updatedBusiness);
        assertNull(businessService.update(updatedBusiness));
    }

    @DisplayName("Test delete business valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, businessService.deleteById(business.getId()));
        verify(businessRepository).save(businessCaptor.capture());
        assertTrue(businessCaptor.getValue().isDeleted());
    }

    @DisplayName("Test delete business invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.empty());

        assertEquals(1, businessService.deleteById(business.getId()));
        verify(businessRepository, never()).save(business);
    }

    @DisplayName("Test add a room successfully")
    @Test
    public void givenValidId_whenAddingARoom_thenBusinessHasNewRoom_andReturnCode0() {
        Business business = mockGenerator.nextObject(Business.class);
        int oldRoomsSize = business.getRooms().size();
        Room room = mockGenerator.nextObject(Room.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());
        when(roomService.add(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(room, businessService.addRoom(business.getId(), room));
        verify(businessRepository).save(businessCaptor.capture());
        assertEquals(oldRoomsSize + 1, businessCaptor.getValue().getRooms().size());
    }

    @DisplayName("Test add a room invalid id")
    @Test
    public void givenInvalidId_whenAddingARoom_thenNotAddRoom_andReturnCode1() {
        Business business = mockGenerator.nextObject(Business.class);
        Room room = mockGenerator.nextObject(Room.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.empty());

        assertNull(businessService.addRoom(business.getId(), room));
        verify(businessRepository, never()).save(business);
        verify(roomService, never()).add(room);
    }

    @DisplayName("Test add operation successfully")
    @Test
    public void givenValidId_whenAddingAOperation_thenBusinessHasNewOperation_andReturnCode0() {
        Business business = mockGenerator.nextObject(Business.class);
        int oldOperationsSize = business.getOperations().size();
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());
        when(operationService.add(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(operation, businessService.addOperation(business.getId(), operation));
        verify(businessRepository).save(businessCaptor.capture());
        assertEquals(oldOperationsSize + 1, businessCaptor.getValue().getOperations().size());
    }

    @DisplayName("Test add operation invalid id")
    @Test
    public void givenInvalidId_whenAddingAOperation_thenNotAddOperation_andReturnCode1() {
        Business business = mockGenerator.nextObject(Business.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.empty());

        assertNull(businessService.addOperation(business.getId(), operation));
        verify(businessRepository, never()).save(business);
        verify(operationService, never()).add(operation);
    }

}
