package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.OperationRepository;
import com.enrique.reservatusalaback.service.impl.OperationServiceImpl;
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
public class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;
    @InjectMocks
    private OperationServiceImpl operationService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Operation> operationCaptor;

    @DisplayName("Test add a operation")
    @Test
    public void whenAddingNewOperation_thenReturnOperation() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(operationRepository.save(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(operation, operationService.add(operation));
    }

    @DisplayName("Test find all operations")
    @Test
    public void whenFindAllOperationes_thenReturnListWithAllOperationes() {
        List<Operation> operations = mockGenerator.objects(Operation.class, 5).toList();
        when(operationRepository.findAll()).thenReturn(operations);

        List<Operation> result = operationService.findAll();
        assertEquals(5, result.size());
        assertEquals(operations, result);
    }

    @DisplayName("Test find operation valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnOperationWithThatId() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(operationRepository.findById(operation.getId())).thenReturn(Optional.of(operation));

        assertEquals(operation, operationService.findById(operation.getId()));
    }

    @DisplayName("Test find operation invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(operationRepository.findById(operation.getId())).thenReturn(Optional.empty());

        assertNull(operationService.findById(operation.getId()));
    }

    @DisplayName("Test update existent operation")
    @Test
    public void givenExistentOperation_whenUpdate_thenReturnUpdatedOperation() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation updatedOperation = mockGenerator.nextObject(Operation.class);
        updatedOperation.setId(operation.getId());
        when(operationRepository.existsById(operation.getId())).thenReturn(true);
        when(operationRepository.save(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(operation, operationService.update(updatedOperation));
        assertEquals(updatedOperation, operationService.update(updatedOperation));
    }

    @DisplayName("Test update non-existent operation")
    @Test
    public void givenNonExistentOperation_whenUpdate_thenDontSave_andReturnNull() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        Operation updatedOperation = mockGenerator.nextObject(Operation.class);
        updatedOperation.setId(operation.getId());
        when(operationRepository.existsById(operation.getId())).thenReturn(false);

        verify(operationRepository, never()).save(updatedOperation);
        assertNull(operationService.update(updatedOperation));
    }

    @DisplayName("Test delete operation valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(operationRepository.findById(operation.getId())).thenReturn(Optional.of(operation));
        when(operationRepository.save(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, operationService.deleteById(operation.getId()));
        verify(operationRepository).deleteById(operation.getId());
    }

    @DisplayName("Test delete operation invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(operationRepository.findById(operation.getId())).thenReturn(Optional.empty());

        assertEquals(1, operationService.deleteById(operation.getId()));
        verify(operationRepository, never()).deleteById(operation.getId());
    }
}
