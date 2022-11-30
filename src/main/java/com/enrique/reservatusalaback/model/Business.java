package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;

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

    private boolean deleted = Boolean.FALSE;
}
