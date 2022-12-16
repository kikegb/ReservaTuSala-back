package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Location;
import com.enrique.reservatusalaback.repository.LocationRepository;
import com.enrique.reservatusalaback.service.impl.LocationServiceImpl;
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
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;
    @InjectMocks
    private LocationServiceImpl locationService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Location> locationCaptor;

    @DisplayName("Test add a location")
    @Test
    public void whenAddingNewLocation_thenReturnLocation() {
        Location location = mockGenerator.nextObject(Location.class);
        when(locationRepository.save(any(Location.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(location, locationService.add(location));
    }

    @DisplayName("Test find all locations")
    @Test
    public void whenFindAllLocations_thenReturnListWithAllLocations() {
        List<Location> locations = mockGenerator.objects(Location.class, 5).toList();
        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.findAll();
        assertEquals(5, result.size());
        assertEquals(locations, result);
    }

    @DisplayName("Test find location valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnLocationWithThatId() {
        Location location = mockGenerator.nextObject(Location.class);
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));

        assertEquals(location, locationService.findById(location.getId()));
    }

    @DisplayName("Test find location invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Location location = mockGenerator.nextObject(Location.class);
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());

        assertNull(locationService.findById(location.getId()));
    }

    @DisplayName("Test update existent location")
    @Test
    public void givenExistentLocation_whenUpdate_thenReturnUpdatedLocation() {
        Location location = mockGenerator.nextObject(Location.class);
        Location updatedLocation = mockGenerator.nextObject(Location.class);
        updatedLocation.setId(location.getId());
        when(locationRepository.existsById(location.getId())).thenReturn(true);
        when(locationRepository.save(any(Location.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(location, locationService.update(updatedLocation));
        assertEquals(updatedLocation, locationService.update(updatedLocation));
    }

    @DisplayName("Test update non-existent location")
    @Test
    public void givenNonExistentLocation_whenUpdate_thenDontSave_andReturnNull() {
        Location location = mockGenerator.nextObject(Location.class);
        Location updatedLocation = mockGenerator.nextObject(Location.class);
        updatedLocation.setId(location.getId());
        when(locationRepository.existsById(location.getId())).thenReturn(false);

        verify(locationRepository, never()).save(updatedLocation);
        assertNull(locationService.update(updatedLocation));
    }

    @DisplayName("Test delete location valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Location location = mockGenerator.nextObject(Location.class);
        when(locationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, locationService.deleteById(location.getId()));
        verify(locationRepository).save(locationCaptor.capture());
        assertTrue(locationCaptor.getValue().isDeleted());
    }

    @DisplayName("Test delete location invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Location location = mockGenerator.nextObject(Location.class);
        when(locationRepository.findById(location.getId())).thenReturn(Optional.empty());

        assertEquals(1, locationService.deleteById(location.getId()));
        verify(locationRepository, never()).save(location);
    }
}
