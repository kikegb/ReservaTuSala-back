package com.enrique.reservatusalaback.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NonNull
    private Room room;

    @NonNull
    private int weekDay;

    @NonNull
    @Column(name = "s_start")
    private LocalTime start;

    @NonNull
    @Column(name = "s_end")
    private LocalTime end;

    private boolean deleted = Boolean.FALSE;
}
