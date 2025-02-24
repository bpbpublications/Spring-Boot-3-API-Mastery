package com.easyshop.catalogservice.middleware.db.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public enum CategoryEntity {

    LAPTOP("laptop"),

    SMARTPHONE("smartphone");

    private final String value;


}
