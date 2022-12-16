package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Customer;
import com.enrique.reservatusalaback.model.Operation;
import com.enrique.reservatusalaback.repository.CustomerRepository;
import com.enrique.reservatusalaback.service.impl.CustomerServiceImpl;
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
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OperationService operationService;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Customer> customerCaptor;

    @DisplayName("Test add a customer")
    @Test
    public void whenAddingNewCustomer_thenReturnCustomer() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        when(customerRepository.save(any(Customer.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(customer, customerService.add(customer));
    }

    @DisplayName("Test find all customers")
    @Test
    public void whenFindAllCustomers_thenReturnListWithAllCustomers() {
        List<Customer> customers = mockGenerator.objects(Customer.class, 7).toList();
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.findAll();
        assertEquals(7, result.size());
        assertEquals(customers, result);
    }

    @DisplayName("Test find customer valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnCustomerWithThatId() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        assertEquals(customer, customerService.findById(customer.getId()));
    }

    @DisplayName("Test find customer invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        assertNull(customerService.findById(customer.getId()));
    }

    @DisplayName("Test update existent customer")
    @Test
    public void givenExistentCustomer_whenUpdate_thenReturnUpdatedCustomer() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Customer updatedCustomer = mockGenerator.nextObject(Customer.class);
        updatedCustomer.setId(customer.getId());
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(customerRepository.save(any(Customer.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(customer, customerService.update(updatedCustomer));
        assertEquals(updatedCustomer, customerService.update(updatedCustomer));
    }

    @DisplayName("Test update non-existent customer")
    @Test
    public void givenNonExistentCustomer_whenUpdate_thenDontSave_andReturnNull() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Customer updatedCustomer = mockGenerator.nextObject(Customer.class);
        updatedCustomer.setId(customer.getId());
        when(customerRepository.existsById(customer.getId())).thenReturn(false);

        verify(customerRepository, never()).save(updatedCustomer);
        assertNull(customerService.update(updatedCustomer));
    }

    @DisplayName("Test delete customer valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, customerService.deleteById(customer.getId()));
        verify(customerRepository).save(customerCaptor.capture());
        assertTrue(customerCaptor.getValue().isDeleted());
    }

    @DisplayName("Test delete customer invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        assertEquals(1, customerService.deleteById(customer.getId()));
        verify(customerRepository, never()).save(customer);
    }

    @DisplayName("Test add operation successfully")
    @Test
    public void givenValidId_whenAddingAOperation_thenCustomerHasNewOperation_andReturnCode0() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        int oldOperationsSize = customer.getOperations().size();
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).then(AdditionalAnswers.returnsFirstArg());
        when(operationService.add(any(Operation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, customerService.addOperation(customer.getId(), operation));
        verify(customerRepository).save(customerCaptor.capture());
        assertEquals(oldOperationsSize + 1, customerCaptor.getValue().getOperations().size());
    }

    @DisplayName("Test add operation invalid id")
    @Test
    public void givenInvalidId_whenAddingAOperation_thenNotAddOperation_andReturnCode1() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        assertEquals(1, customerService.addOperation(customer.getId(), operation));
        verify(customerRepository, never()).save(customer);
        verify(operationService, never()).add(operation);
    }

    @DisplayName("Test add operation error saving operation")
    @Test
    public void givenValidId_whenAddingAOperation_andErrorSavingOperation_thenNotAddOperation_andReturnCode2() {
        Customer customer = mockGenerator.nextObject(Customer.class);
        Operation operation = mockGenerator.nextObject(Operation.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(operationService.add(operation)).thenReturn(null);

        assertEquals(2, customerService.addOperation(customer.getId(), operation));
        verify(operationService).add(operation);
        verify(customerRepository, never()).save(customer);
    }
}
