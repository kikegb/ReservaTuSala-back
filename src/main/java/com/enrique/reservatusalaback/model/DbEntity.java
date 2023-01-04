package com.enrique.reservatusalaback.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class DbEntity {

    protected boolean deleted;

}
