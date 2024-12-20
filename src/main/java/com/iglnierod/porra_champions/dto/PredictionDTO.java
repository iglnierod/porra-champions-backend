package com.iglnierod.porra_champions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionDTO {
    private Long id;
    private int localGoals;
    private int awayGoals;
    private Long matchId;
    private Long userId;
    private int status;
}
