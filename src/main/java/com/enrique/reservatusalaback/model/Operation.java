package com.enrique.reservatusalaback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties(value = {"businessOperations", "customerOperations", "rooms"})
    private User customer;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    @JsonIgnoreProperties(value = {"businessOperations", "customerOperations", "rooms"})
    private User business;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties(value = {"operations", "schedules", "materials"})
    private Room room;

    @NonNull
    @NotNull(message = "Start is required")
    @Column(name = "op_start")
    private LocalDateTime start;

    @NonNull
    @NotNull(message = "End is required")
    @Column(name = "op_end")
    private LocalDateTime end;

    @NonNull
    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be a positive value")
    private double cost;

    @NonNull
    @NotNull(message = "Status is required")
    private StatusCode status;
}
