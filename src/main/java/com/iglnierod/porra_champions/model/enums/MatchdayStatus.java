package com.iglnierod.porra_champions.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MatchdayStatus {
    CURRENT(0),
    PLAYED(1),
    BLOCKED(2);

    @Getter
    private final int value;

}
