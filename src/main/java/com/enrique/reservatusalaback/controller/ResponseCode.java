package com.enrique.reservatusalaback.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {
    OK(0, "OK"),
    NOT_FOUND_ID(1, "Invalid ID: ID not found in database."),
    ALREADY_EXISTENT_USER(3, "User conflict: There is already a user with that email o cif/nif."),
    ALREADY_EXISTENT_ROOM(4, "Room conflict: There is already a room with this name or location"),
    ALREADY_EXISTENT_OPERATION(5, "Operation conflict: This operation already exists.");

    public final int code;
    public final String description;
    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
