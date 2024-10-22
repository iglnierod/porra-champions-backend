package com.iglnierod.porra_champions.dto;

import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Prediction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchWithPredictionsDTO {
    private Match match;
    private List<Prediction> predictions;
}
