package com.iglnierod.porra_champions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupedPrediction {
    private Match match;
    private List<Prediction> predictions;
}
