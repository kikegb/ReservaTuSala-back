package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @NonNull
    private String cnif;

    @NonNull
    private String name;

    @NonNull
    private String phone;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private List<Operation> operations;

    private boolean deleted = Boolean.FALSE;
}
