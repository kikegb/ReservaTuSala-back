package com.enrique.reservatusalaback.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends DbEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @NotNull(message = "CIF or NIF is required")
    @Size(min = 9, max = 9, message = "CIF or NIF must be 9 characters long")
    private String cnif;

    @NonNull
    @NotNull(message = "Name is required")
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

    @NonNull
    @NotNull(message = "User role is required")
    private Role role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Room> rooms;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private List<Operation> BusinessOperations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private List<Operation> CustomerOperations;
}