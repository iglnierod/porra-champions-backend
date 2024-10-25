package com.iglnierod.porra_champions.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PredictionStatus {

    WAITING(0),
    WRONG(1),
    RIGHT(2),
    PERFECT(3);

    @Getter
    private final int value;
}
