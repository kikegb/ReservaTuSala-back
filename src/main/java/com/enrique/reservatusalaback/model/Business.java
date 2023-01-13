package com.enrique.reservatusalaback.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Business extends DbEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @NotNull(message = "CIF is required")
    @ToString.Include
    @Size(min = 9, max = 9, message = "CIF must be 9 characters long")
    private String cif;

    @NonNull
    @NotNull(message = "Name is required")
    @ToString.Include
    private String name;

    @NonNull
    @NotNull(message = "Phone is required")
    @Size(min = 9, max = 9, message = "Phone must be 9 characters long")
    private String phone;

    @NonNull
    @NotNull(message = "Password is required")
    private String password;

    @NonNull
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Operation> operations;
}
