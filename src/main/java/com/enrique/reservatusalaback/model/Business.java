package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    private String cif;

    @NonNull
    private String name;

    @NonNull
    private String phone;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @OneToMany
    @JoinColumn(name = "business_id")
    private List<Room> rooms;

    @OneToMany
    @JoinColumn(name = "business_id")
    private List<Operation> operations;

    private boolean deleted = Boolean.FALSE;
}
