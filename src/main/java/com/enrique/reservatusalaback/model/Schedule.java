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
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Schedule{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @NotNull(message = "Week day is required")
    private int weekDay;

    @NonNull
    @NotNull(message = "Start is required")
    @Column(name = "s_start")
    private LocalTime start;

    @NonNull
    @NotNull(message = "End is required")
    @Column(name = "s_end")
    private LocalTime end;

}
