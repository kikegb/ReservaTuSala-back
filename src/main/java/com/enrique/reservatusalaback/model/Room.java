package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @NonNull
    private Business business;

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

    private boolean deleted = Boolean.FALSE;
}
