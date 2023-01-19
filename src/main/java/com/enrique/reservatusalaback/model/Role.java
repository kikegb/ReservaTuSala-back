package com.enrique.reservatusalaback.model;

public enum Role {
    BUSINESS(0),
    CUSTOMER(1);

    public final int role;

    Role(int role) {
        this.role = role;
    }
}
