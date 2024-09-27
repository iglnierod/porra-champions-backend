package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.PredictionDTO;
import com.iglnierod.porra_champions.model.GroupedPrediction;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.PredictionRepository;
import com.iglnierod.porra_champions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {
    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

//    @GetMapping
//    public List<Prediction> getPredictions() {
//        return predictionRepository.findAll();
//    }

    @GetMapping
    public List<GroupedPrediction> getPredictions() {
        List<Prediction> predictions = predictionRepository.findAll();

        // Agrupar predicciones por partido
        Map<Long, List<Prediction>> groupedMap = predictions.stream()
                .collect(Collectors.groupingBy(prediction -> prediction.getMatch().getId()));

        // Crear una lista de GroupedPrediction
        List<GroupedPrediction> groupedPredictions = new ArrayList<>();
        for (Map.Entry<Long, List<Prediction>> entry : groupedMap.entrySet()) {
            Long matchId = entry.getKey();
            List<Prediction> matchPredictions = entry.getValue();
            Match match = matchRepository.getReferenceById(matchId); // Obtener el partido correspondiente
            groupedPredictions.add(new GroupedPrediction(match, matchPredictions));
        }

        return groupedPredictions;
    }

    @PostMapping
    public ResponseEntity<Prediction> createMatch(@RequestBody PredictionDTO predictionDTO) {
        Prediction prediction = new Prediction();
        prediction.setMatch(matchRepository.getReferenceById(predictionDTO.getMatchId()));
        prediction.setGoalsLocalPrediction(predictionDTO.getLocalGoals());
        prediction.setGoalsAwayPrediction(predictionDTO.getAwayGoals());
        prediction.setUser(userRepository.getReferenceById(predictionDTO.getUserId()));

        Prediction savedPrediction = predictionRepository.save(prediction);

        return ResponseEntity.ok(savedPrediction);
    }
}
