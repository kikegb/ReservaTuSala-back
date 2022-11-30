package com.enrique.reservatusalaback.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @NonNull
    private Location location;

    @NonNull
    private String name;

    @NonNull
    private double size;

    @NonNull
    private double price;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Operation> operations;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Schedule> schedule;

    @OneToMany
    @JoinColumn(name = "room_id")
    private List<Material> materials;

    private boolean deleted = Boolean.FALSE;
}
