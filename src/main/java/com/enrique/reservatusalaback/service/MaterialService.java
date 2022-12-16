package com.enrique.reservatusalaback.service;

import com.enrique.reservatusalaback.model.Material;

import java.util.List;

public interface MaterialService {
    Material add(Material material);
    List<Material> findAll();
    Material findById(Long id);
    Material update(Material material);
    int deleteById(Long id);

}
