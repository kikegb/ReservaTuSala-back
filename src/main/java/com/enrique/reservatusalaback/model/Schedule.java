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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Schedule extends DbEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
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

}
