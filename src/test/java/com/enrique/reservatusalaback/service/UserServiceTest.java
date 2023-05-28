package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.User;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.model.Room;
import com.enrique.reservatusalaback.repository.UserRepository;
import com.enrique.reservatusalaback.service.impl.UserServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private OperationService operationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<User> userCaptor;

    @DisplayName("Test add a user")
    @Test
    public void whenAddingNewUser_thenReturnUser() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.existsByCnifAndEmail(user.getCnif(), user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(user, userService.add(user));
    }

    @DisplayName("Test add already existent user")
    @Test
    public void whenAddingAlreadyExistentUser_thenReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.existsByCnifAndEmail(user.getCnif(), user.getEmail())).thenReturn(true);

        assertNull(userService.add(user));
        verify(userRepository, never()).save(user);
    }

    @DisplayName("Test find all users")
    @Test
    public void whenFindAllUseres_thenReturnListWithAllUseres() {
        List<User> users = mockGenerator.objects(User.class, 5).toList();
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();
        assertEquals(5, result.size());
        assertEquals(users, result);
    }

    @DisplayName("Test find user valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnUserWithThatId() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(user.getId()));
    }

    @DisplayName("Test find user invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertNull(userService.findById(user.getId()));
    }

    @DisplayName("Test update existent user")
    @Test
    public void givenExistentUser_whenUpdate_thenReturnUpdatedUser() {
        User user = mockGenerator.nextObject(User.class);
        User updatedUser = mockGenerator.nextObject(User.class);
        updatedUser.setId(user.getId());
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(user, userService.update(updatedUser));
        assertEquals(updatedUser, userService.update(updatedUser));
    }

    @DisplayName("Test update non-existent user")
    @Test
    public void givenNonExistentUser_whenUpdate_thenDontSave_andReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        User updatedUser = mockGenerator.nextObject(User.class);
        updatedUser.setId(user.getId());
        when(userRepository.existsById(user.getId())).thenReturn(false);

        verify(userRepository, never()).save(updatedUser);
        assertNull(userService.update(updatedUser));
    }

    @DisplayName("Test delete user valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, userService.deleteById(user.getId()));
        verify(userRepository).save(userCaptor.capture());
        assertTrue(userCaptor.getValue().isDeleted());
    }

    @DisplayName("Test delete user invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        User user = mockGenerator.nextObject(User.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertEquals(1, userService.deleteById(user.getId()));
        verify(userRepository, never()).save(user);
    }

    @DisplayName("Test add a room successfully")
    @Test
    public void givenValidId_whenAddingARoom_thenUserHasNewRoom_andReturnAddedRoom() {
        User user = mockGenerator.nextObject(User.class);
        int oldRoomsSize = user.getRooms().size();
        Room room = mockGenerator.nextObject(Room.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        when(roomService.add(any(Room.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(room, userService.addRoom(user.getId(), room));
        verify(userRepository).save(userCaptor.capture());
        assertEquals(oldRoomsSize + 1, userCaptor.getValue().getRooms().size());
    }

    @DisplayName("Test add a room invalid id")
    @Test
    public void givenInvalidId_whenAddingARoom_thenNotAddRoom_andReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        Room room = mockGenerator.nextObject(Room.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertNull(userService.addRoom(user.getId(), room));
        verify(userRepository, never()).save(user);
        verify(roomService, never()).add(room);
    }

    @DisplayName("Test add business operation successfully")
    @Test
    public void givenValidId_whenAddingABusinessOperation_thenUserHasNewOperation_andReturnAddedOperation() {
        User user = mockGenerator.nextObject(User.class);
        int oldOperationsSize = user.getBusinessOperations().size();
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        when(operationService.add(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(operation, userService.addBusinessOperation(user.getId(), operation));
        verify(userRepository).save(userCaptor.capture());
        assertEquals(oldOperationsSize + 1, userCaptor.getValue().getBusinessOperations().size());
    }

    @DisplayName("Test add business operation invalid id")
    @Test
    public void givenInvalidId_whenAddingABusinessOperation_thenNotAddOperation_andReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertNull(userService.addBusinessOperation(user.getId(), operation));
        verify(userRepository, never()).save(user);
        verify(operationService, never()).add(operation);
    }

    @DisplayName("Test add customer operation successfully")
    @Test
    public void givenValidId_whenAddingACustomerOperation_thenUserHasNewOperation_andReturnAddedOperation() {
        User user = mockGenerator.nextObject(User.class);
        int oldOperationsSize = user.getCustomerOperations().size();
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        when(operationService.add(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(operation, userService.addCustomerOperation(user.getId(), operation));
        verify(userRepository).save(userCaptor.capture());
        assertEquals(oldOperationsSize + 1, userCaptor.getValue().getCustomerOperations().size());
    }

    @DisplayName("Test add customer operation invalid id")
    @Test
    public void givenInvalidId_whenAddingACustomerOperation_thenNotAddOperation_andReturnNull() {
        User user = mockGenerator.nextObject(User.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertNull(userService.addCustomerOperation(user.getId(), operation));
        verify(userRepository, never()).save(user);
        verify(operationService, never()).add(operation);
    }

}
