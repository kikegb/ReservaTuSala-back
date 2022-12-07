package com.enrique.reservatusalaback.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Business extends DbEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Operation> operations;
}
