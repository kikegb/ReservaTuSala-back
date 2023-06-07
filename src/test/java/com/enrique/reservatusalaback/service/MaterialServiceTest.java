package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.service.impl.MaterialServiceImpl;
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
public class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;
    @InjectMocks
    private MaterialServiceImpl materialService;

    private final EasyRandom mockGenerator = new EasyRandom(
            new EasyRandomParameters()
                    .randomizationDepth(2)
                    .collectionSizeRange(0,5)
    );

    @Captor
    ArgumentCaptor<Material> materialCaptor;

    @DisplayName("Test add a material")
    @Test
    public void whenAddingNewMaterial_thenReturnMaterial() {
        Material material = mockGenerator.nextObject(Material.class);
        when(materialRepository.save(any(Material.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(material, materialService.add(material));
    }

    @DisplayName("Test find all materials")
    @Test
    public void whenFindAllMateriales_thenReturnListWithAllMateriales() {
        List<Material> materials = mockGenerator.objects(Material.class, 5).toList();
        when(materialRepository.findAll()).thenReturn(materials);

        List<Material> result = materialService.findAll();
        assertEquals(5, result.size());
        assertEquals(materials, result);
    }

    @DisplayName("Test find material valid id")
    @Test
    public void givenValidId_whenFindingById_thenReturnMaterialWithThatId() {
        Material material = mockGenerator.nextObject(Material.class);
        when(materialRepository.findById(material.getId())).thenReturn(Optional.of(material));

        assertEquals(material, materialService.findById(material.getId()));
    }

    @DisplayName("Test find material invalid id")
    @Test
    public void givenInvalidId_whenFindingById_thenReturnNull() {
        Material material = mockGenerator.nextObject(Material.class);
        when(materialRepository.findById(material.getId())).thenReturn(Optional.empty());

        assertNull(materialService.findById(material.getId()));
    }

    @DisplayName("Test update existent material")
    @Test
    public void givenExistentMaterial_whenUpdate_thenReturnUpdatedMaterial() {
        Material material = mockGenerator.nextObject(Material.class);
        Material updatedMaterial = mockGenerator.nextObject(Material.class);
        updatedMaterial.setId(material.getId());
        when(materialRepository.existsById(material.getId())).thenReturn(true);
        when(materialRepository.save(any(Material.class))).then(AdditionalAnswers.returnsFirstArg());

        assertNotEquals(material, materialService.update(updatedMaterial));
        assertEquals(updatedMaterial, materialService.update(updatedMaterial));
    }

    @DisplayName("Test update non-existent material")
    @Test
    public void givenNonExistentMaterial_whenUpdate_thenDontSave_andReturnNull() {
        Material material = mockGenerator.nextObject(Material.class);
        Material updatedMaterial = mockGenerator.nextObject(Material.class);
        updatedMaterial.setId(material.getId());
        when(materialRepository.existsById(material.getId())).thenReturn(false);

        verify(materialRepository, never()).save(updatedMaterial);
        assertNull(materialService.update(updatedMaterial));
    }

    @DisplayName("Test delete material valid id")
    @Test
    public void givenValidId_whenDeletingById_thenDeletedIsTrue_andReturnCode0() {
        Material material = mockGenerator.nextObject(Material.class);
        when(materialRepository.findById(material.getId())).thenReturn(Optional.of(material));
        when(materialRepository.save(any(Material.class))).then(AdditionalAnswers.returnsFirstArg());

        assertEquals(0, materialService.deleteById(material.getId()));
        verify(materialRepository).deleteById(material.getId());
    }

    @DisplayName("Test delete material invalid id")
    @Test
    public void givenInvalidId_whenDeletingById_thenReturnCode1() {
        Material material = mockGenerator.nextObject(Material.class);
        when(materialRepository.findById(material.getId())).thenReturn(Optional.empty());

        assertEquals(1, materialService.deleteById(material.getId()));
        verify(materialRepository, never()).deleteById(material.getId());
    }
}
