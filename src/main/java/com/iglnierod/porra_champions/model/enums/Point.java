package com.iglnierod.porra_champions.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Point {
    WRONG(0),
    RIGHT(1),
    PERFECT(3);

    @Getter
    private final int value;
}
