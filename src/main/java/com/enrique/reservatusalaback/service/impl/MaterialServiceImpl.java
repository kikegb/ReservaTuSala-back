package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final RoomService roomService;

    @Override
    public Material add(final Long roomId, final Material material) {
        Material newMaterial = materialRepository.save(material);
        int result = roomService.addMaterial(roomId, material);
        if (result < 0) {
            materialRepository.deleteById(newMaterial.getId());
            return null;
        }
        return newMaterial;
    }

    @Override
    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Override
    public Material findById(Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    @Override
    public Material update(Material material) {
        if (materialRepository.existsById(material.getId())) {
            return materialRepository.save(material);
        }
        return null;
    }

    @Override
    public int deleteById(Long id) {
        Optional<Material> result = materialRepository.findById(id);
        if (result.isPresent()) {
            Material material = result.get();
            material.setDeleted(true);
            materialRepository.save(material);
            return 0;
        }
        return -1;
    }
}
