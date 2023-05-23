package com.enrique.reservatusalaback.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Room extends DbEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @NotNull(message = "Location is required")
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @NonNull
    @NotNull(message = "Name is required")
    private String name;

    @NonNull
    @NotNull(message = "Size is required")
    private double size;

    @NonNull
    @NotNull(message = "Capacity is required")
    private int capacity;

    @NonNull
    @NotNull(message = "Price is required")
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    @JsonIgnoreProperties({"BusinessOperations", "CustomerOperations", "rooms"})
    private User business;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private List<Operation> operations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private List<Schedule> schedules;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private List<Material> materials;

}
