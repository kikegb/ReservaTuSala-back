package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NonNull
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "business_id")
    @NonNull
    private Business business;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NonNull
    private Room room;

    @NonNull
    @Column(name = "op_start")
    private LocalDateTime start;

    @NonNull
    @Column(name = "op_end")
    private LocalDateTime end;

    @NonNull
    private double cost;

    @NonNull
    private int status;

    private boolean deleted = Boolean.FALSE;
}
