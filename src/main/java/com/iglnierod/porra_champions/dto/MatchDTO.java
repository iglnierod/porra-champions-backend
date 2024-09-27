package com.iglnierod.porra_champions.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Long localTeamId;
    private Long awayTeamId;
    private int localGoals;
    private int awayGoals;
}
