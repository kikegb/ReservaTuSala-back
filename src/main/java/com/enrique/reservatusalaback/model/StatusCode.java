package com.enrique.reservatusalaback.model;

public enum StatusCode {
    PENDING(0),
    APPROVED(1),
    CANCELLED(2);

    public final int code;

    StatusCode(int code) {
        this.code = code;
    }
}
