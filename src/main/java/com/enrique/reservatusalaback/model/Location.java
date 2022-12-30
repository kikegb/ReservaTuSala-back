package com.enrique.reservatusalaback.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Location extends DbEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @NotNull(message = "Street is required")
    private String street;

    @NonNull
    @NotNull(message = "Address number is required")
    private String number;

    @NonNull
    @NotNull(message = "Postcode is required")
    @Size(min = 5, max = 5, message = "Postcode must be 5 characters long")
    private String postcode;

    @NonNull
    @NotNull(message = "Town is required")
    private String town;

    @NonNull
    @NotNull(message = "Province is required")
    private String province;

    @NonNull
    @NotNull(message = "Country is required")
    private String country;

}
