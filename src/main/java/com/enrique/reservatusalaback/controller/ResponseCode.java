package com.enrique.reservatusalaback.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {
    OK(0, "OK"),
    NOT_FOUND_ID(1, "Invalid ID: ID not found in database."),
    ALREADY_EXISTENT_USER(2, "User conflict: There is already a user with that email or cif/nif."),
    NULL_BUSINESS(3, "Bad request: The provided business is empty. Data is required."),
    NULL_CUSTOMER(4, "Bad request: The provided customer is empty. Data is required."),
    NULL_LOCATION(5, "Bad request: The provided location is empty. Data is required."),
    NULL_MATERIAL(6, "Bad request: The provided material is empty. Data is required."),
    NULL_OPERATION(7, "Bad request: The provided operation is empty. Data is required."),
    NULL_ROOM(8, "Bad request: The provided room is empty. Data is required."),
    NULL_SCHEDULE(9, "Bad request: The provided schedule is empty. Data is required."),
    NULL_ID(10, "Bad request: Null ID. A value is required.");

    public final int code;
    public final String description;
    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
