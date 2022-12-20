package com.enrique.reservatusalaback.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Operation extends DbEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @Column(name = "op_start")
    private LocalDateTime start;

    @NonNull
    @Column(name = "op_end")
    private LocalDateTime end;

    @NonNull
    private double cost;

    @NonNull
    private StatusCode status;

}
