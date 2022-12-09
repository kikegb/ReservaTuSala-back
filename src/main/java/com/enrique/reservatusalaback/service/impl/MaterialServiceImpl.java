package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.model.Material;
import com.enrique.reservatusalaback.repository.MaterialRepository;
import com.enrique.reservatusalaback.service.MaterialService;
import com.enrique.reservatusalaback.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final RoomService roomService;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, RoomService roomService) {
        this.materialRepository = materialRepository;
        this.roomService = roomService;
    }

    @Override
    public Material add(Long roomId, Material material) {
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
        return materialRepository.save(material);
    }

    @Override
    public int deleteById(Long id) {
        Optional<Material> result = materialRepository.findById(id);
        if (result.isPresent()) {
            Material material = result.get();
            material.setDeleted(true);
            materialRepository.save(material);
            return 1;
        }
        return -1;
    }
}
