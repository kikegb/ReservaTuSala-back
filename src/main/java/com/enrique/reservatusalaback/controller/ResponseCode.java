package com.enrique.reservatusalaback.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {
    OK(0, "OK"),
    NOT_FOUND_ID(1, "Invalid ID: ID not found in database."),
    ALREADY_EXISTENT_USER(2, "User conflict: There is already a user with that email or cif/nif."),
    BAD_REQUEST(3, "Bad request: Request body has empty or wrong formatted data.");

    public final int code;
    public final String description;
    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
