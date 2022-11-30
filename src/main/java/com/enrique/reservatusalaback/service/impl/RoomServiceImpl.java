package com.enrique.reservatusalaback.service.impl;

import com.enrique.reservatusalaback.repository.RoomRepository;
import com.enrique.reservatusalaback.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

}
