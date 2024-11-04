package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.MatchDTO;
import com.iglnierod.porra_champions.dto.MatchWithPredictionsDTO;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Matchday;
import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.model.Team;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.MatchdayRepository;
import com.iglnierod.porra_champions.repository.PredictionRepository;
import com.iglnierod.porra_champions.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchdayRepository matchdayRepository;  // Nuevo repositorio para Matchday

    @Autowired
    private PredictionRepository predictionRepository;

    @GetMapping
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/matchday/{matchDayId}")
    public List<Match> findMatchesByMatchDay(@PathVariable Long matchDayId) {
        Matchday matchday = matchdayRepository.findById(matchDayId)
                .orElseThrow(() -> new RuntimeException("Matchday not found"));

        return matchRepository.findByMatchday(matchday);
    }

    @GetMapping("/v2/matchday/{matchdayId}")
    public List<MatchWithPredictionsDTO> getMatchesWithPredictionsByMatchDay(@PathVariable Long matchdayId) {
        // Obtiene el matchday por ID
        Matchday matchday = matchdayRepository.findById(matchdayId)
                .orElseThrow(() -> new RuntimeException("Matchday not found"));

        // Obtiene todos los partidos asociados al matchday
        List<Match> matches = matchRepository.findByMatchday(matchday);
        List<MatchWithPredictionsDTO> result = new ArrayList<>();

        // Itera sobre los partidos y obtiene las predicciones para cada uno
        for (Match match : matches) {
            List<Prediction> predictions = predictionRepository.findByMatch_Id(match.getId());
            result.add(new MatchWithPredictionsDTO(match, predictions));
        }

        return result;
    }

    // Obtener los partidos de un matchday con las predicciones de un usuario específico
    @GetMapping("/matchday/{matchdayId}/user/{userId}")
    public List<MatchWithPredictionsDTO> getMatchesWithPredictionsByMatchDayAndUser(
            @PathVariable Long matchdayId,
            @PathVariable Long userId) {

        Matchday matchday = matchdayRepository.findById(matchdayId)
                .orElseThrow(() -> new RuntimeException("No matchday found with id: " + matchdayId));
        // Obtener los partidos por matchDay
        List<Match> matches = matchRepository.findByMatchday(matchday);

        // Crear la lista de MatchWithPredictionsDTO
        List<MatchWithPredictionsDTO> result = new ArrayList<>();

        // Para cada partido, buscar las predicciones del usuario y agregarlas al DTO
        for (Match match : matches) {
            List<Prediction> predictions = predictionRepository.findByMatch_IdAndUser_Id(match.getId(), userId);
            result.add(new MatchWithPredictionsDTO(match, predictions));
        }

        return result;
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody MatchDTO matchDTO) {
        Team localTeam = teamRepository.findById(matchDTO.getLocalTeamId())
                .orElseThrow(() -> new RuntimeException("Local team not found"));

        Team awayTeam = teamRepository.findById(matchDTO.getAwayTeamId())
                .orElseThrow(() -> new RuntimeException("Away team not found"));

        Matchday matchday = matchdayRepository.findById(matchDTO.getMatchdayId())
                .orElseThrow(() -> new RuntimeException("Matchday not found"));

        if (Objects.equals(localTeam.getId(), awayTeam.getId())) {
            throw new RuntimeException("Teams cannot be the same");
        }

        Match match = new Match();
        match.setLocalTeam(localTeam);
        match.setAwayTeam(awayTeam);
        match.setLocalGoals(matchDTO.getLocalGoals());
        match.setAwayGoals(matchDTO.getAwayGoals());
        match.setMatchday(matchday);  // Asociar el partido con el Matchday

        Match savedMatch = matchRepository.save(match);

        return ResponseEntity.ok(savedMatch);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Match>> createMatches(@RequestBody List<MatchDTO> matchDTOs) {
        List<Match> savedMatches = new ArrayList<>();

        for (MatchDTO matchDTO : matchDTOs) {
            Team localTeam = teamRepository.findById(matchDTO.getLocalTeamId())
                    .orElseThrow(() -> new RuntimeException("Local team not found"));

            Team awayTeam = teamRepository.findById(matchDTO.getAwayTeamId())
                    .orElseThrow(() -> new RuntimeException("Away team not found"));

            Matchday matchday = matchdayRepository.findById(matchDTO.getMatchdayId())
                    .orElseThrow(() -> new RuntimeException("Matchday not found"));

            if (Objects.equals(localTeam.getId(), awayTeam.getId())) {
                throw new RuntimeException("Teams cannot be the same");
            }

            Match match = new Match();
            match.setLocalTeam(localTeam);
            match.setAwayTeam(awayTeam);
            match.setLocalGoals(matchDTO.getLocalGoals());
            match.setAwayGoals(matchDTO.getAwayGoals());
            match.setMatchday(matchday);  // Asociar cada partido con su Matchday

            savedMatches.add(matchRepository.save(match));
        }

        return ResponseEntity.ok(savedMatches);
    }
}
