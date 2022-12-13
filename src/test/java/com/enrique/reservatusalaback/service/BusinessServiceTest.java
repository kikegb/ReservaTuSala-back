package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Business;
import com.enrique.reservatusalaback.repository.BusinessRepository;
import com.enrique.reservatusalaback.service.impl.BusinessServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {

    private final EasyRandom mockGenerator = new EasyRandom();

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessServiceImpl businessService;


    @DisplayName("Test add a business")
    @Test
    public void whenAddingNewBusiness_thenReturnsBusinessWithId() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.save(any(Business.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(business, businessService.add(business));
        verify(businessRepository).save(business);
    }

    @DisplayName("Test find all businesses")
    @Test
    public void whenFindAllBusinesses_thenReturnListWithAllBusinesses() {
        List<Business> businesses = mockGenerator.objects(Business.class, 5).toList();
        when(businessRepository.findAll()).thenReturn(businesses);

        List<Business> result = businessService.findAll();
        assertEquals(5, result.size());
        assertEquals(businesses, result);
        verify(businessRepository).findAll();
    }

    @DisplayName("Test success finding business by id")
    @Test
    public void givenAValidId_thenReturnBusinessWithThatId() {
        Business business = mockGenerator.nextObject(Business.class);
        when(businessRepository.findById(business.getId())).thenReturn(Optional.of(business));

        assertEquals(business, businessService.findById(business.getId()));
        verify(businessRepository).findById(business.getId());
    }
}
