package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    private String street;

    @NonNull
    private String number;

    @NonNull
    private String postcode;

    @NonNull
    private String town;

    @NonNull
    private String province;

    @NonNull
    private String country;

    private boolean deleted = Boolean.FALSE;
}
