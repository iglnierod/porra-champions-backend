package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.PredictionDTO;
import com.iglnierod.porra_champions.model.GroupedPrediction;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.PredictionRepository;
import com.iglnierod.porra_champions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/matchday/{matchDay}")
    public List<Prediction> findPredictionsByMatchDay(@PathVariable("matchDay") int matchDay) {
        return predictionRepository.findByMatch_MatchDay(matchDay);
    }




//    @PostMapping
//    public ResponseEntity<Prediction> createMatch(@RequestBody PredictionDTO predictionDTO) {
//        Prediction prediction = new Prediction();
//        prediction.setMatch(matchRepository.getReferenceById(predictionDTO.getMatchId()));
//        prediction.setGoalsLocalPrediction(predictionDTO.getLocalGoals());
//        prediction.setGoalsAwayPrediction(predictionDTO.getAwayGoals());
//        prediction.setUser(userRepository.getReferenceById(predictionDTO.getUserId()));
//
//        Prediction savedPrediction = predictionRepository.save(prediction);
//
//        return ResponseEntity.ok(savedPrediction);
//    }

    @PostMapping("/predict")
    public ResponseEntity<String> createPredictions(@RequestBody List<PredictionDTO> predictions) {
        for(PredictionDTO pred : predictions) {
            Prediction prediction = new Prediction();
            prediction.setMatch(matchRepository.getReferenceById(pred.getMatchId()));
            prediction.setGoalsLocalPrediction(pred.getLocalGoals());
            prediction.setGoalsAwayPrediction(pred.getAwayGoals());
            prediction.setUser(userRepository.getReferenceById(pred.getUserId()));

            predictionRepository.save(prediction);
        }
        return ResponseEntity.ok("Predicciones recibidas correctamente");
    }

    @PutMapping("/edit")
    public ResponseEntity<Prediction> editPrediction(@RequestBody PredictionDTO predictionDTO) {
        // Buscar la predicción existente por el ID del partido y del usuario
        List<Prediction> existingPredictions = predictionRepository.findByMatch_IdAndUser_Id(
                predictionDTO.getMatchId(), predictionDTO.getUserId());

        if (existingPredictions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Tomar la primera predicción (asumiendo que solo hay una)
        Prediction prediction = existingPredictions.get(0);
        // Actualizar los goles
        prediction.setGoalsLocalPrediction(predictionDTO.getLocalGoals());
        prediction.setGoalsAwayPrediction(predictionDTO.getAwayGoals());

        // Guardar los cambios en la base de datos
        Prediction updatedPrediction = predictionRepository.save(prediction);

        // Devolver el objeto de predicción actualizado
        return ResponseEntity.ok(updatedPrediction);
    }
}
