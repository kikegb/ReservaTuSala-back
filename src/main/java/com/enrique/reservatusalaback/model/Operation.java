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
