package com.iglnierod.porra_champions.dto;

import com.iglnierod.porra_champions.model.enums.MatchdayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchdayDTO {
    private String name;
    private MatchdayStatus status;
}
