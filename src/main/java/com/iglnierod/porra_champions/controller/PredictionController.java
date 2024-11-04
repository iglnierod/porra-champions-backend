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
        // Buscar la predicción existente por el ID
        Prediction existingPrediction = predictionRepository.findById(predictionDTO.getId()).orElse(null);

        if (existingPrediction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Actualizar los goles
        existingPrediction.setGoalsLocalPrediction(predictionDTO.getLocalGoals());
        existingPrediction.setGoalsAwayPrediction(predictionDTO.getAwayGoals());

        // Guardar los cambios en la base de datos
        Prediction updatedPrediction = predictionRepository.save(existingPrediction);

        // Devolver el objeto de predicción actualizado
        return ResponseEntity.ok(updatedPrediction);
    }

    // Añadir una nueva predicción
    @PostMapping("/add")
    public ResponseEntity<Prediction> addPrediction(@RequestBody PredictionDTO predictionDTO) {
        // Comprobar si ya existe una predicción para este partido y usuario
        List<Prediction> existingPredictions = predictionRepository.findByMatch_IdAndUser_Id(
                predictionDTO.getMatchId(), predictionDTO.getUserId());

        if (!existingPredictions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }

        // Crear una nueva predicción
        Prediction prediction = new Prediction();
        prediction.setMatch(matchRepository.getReferenceById(predictionDTO.getMatchId()));
        prediction.setGoalsLocalPrediction(predictionDTO.getLocalGoals());
        prediction.setGoalsAwayPrediction(predictionDTO.getAwayGoals());
        prediction.setUser(userRepository.getReferenceById(predictionDTO.getUserId()));

        // Guardar la nueva predicción en la base de datos
        Prediction savedPrediction = predictionRepository.save(prediction);

        // Devolver la nueva predicción
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPrediction); // 201 Created
    }
}
