package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NonNull
    private Room room;

    @NonNull
    private String material;

    @NonNull
    private int quantity;

    private boolean deleted = Boolean.FALSE;
}
