package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.MatchDTO;
import com.iglnierod.porra_champions.dto.MatchWithPredictionsDTO;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.model.Team;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.PredictionRepository;
import com.iglnierod.porra_champions.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private PredictionRepository predictionRepository;

    @GetMapping
    public List<Match> getAllMatches() {
//        return matchRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return matchRepository.findAll();
    }

    @GetMapping("/matchday")
    public List<Integer> getMatchDays() {
        return matchRepository.findDistinctMatchDays();
    }

    @GetMapping("/matchday/{matchDay}")
    public List<Match> findMatchesByMatchDay(@PathVariable int matchDay) {
        return matchRepository.findByMatchDay(matchDay);
    }

    @GetMapping("/v2/matchday/{matchDay}")
    public List<MatchWithPredictionsDTO> getMatchesWithPredictionsByMatchDay(@PathVariable int matchDay) {
        // Obtener los partidos por matchDay
        List<Match> matches = matchRepository.findByMatchDay(matchDay);

        // Crear la lista de MatchWithPredictionsDTO

        List<MatchWithPredictionsDTO> result = new ArrayList<>();

        // Para cada partido, buscar las predicciones y agregarlas al DTO
        for (Match match : matches) {
            List<Prediction> predictions = predictionRepository.findByMatch_Id(match.getId());
            result.add(new MatchWithPredictionsDTO(match, predictions));
        }

        return result;
    }

    // Obtener los partidos de un matchday con las predicciones de un usuario específico
    @GetMapping("/matchday/{matchDay}/user/{userId}")
    public List<MatchWithPredictionsDTO> getMatchesWithPredictionsByMatchDayAndUser(
            @PathVariable int matchDay,
            @PathVariable Long userId) {

        // Obtener los partidos por matchDay
        List<Match> matches = matchRepository.findByMatchDay(matchDay);

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

        if(Objects.equals(localTeam.getId(), awayTeam.getId())) {
            throw new RuntimeException("Teams cannot be the same");
        }

        Match match = new Match();
        match.setLocalTeam(localTeam);
        match.setAwayTeam(awayTeam);
        match.setLocalGoals(matchDTO.getLocalGoals());
        match.setAwayGoals(matchDTO.getAwayGoals());

        Match savedMatch = matchRepository.save(match);

        return ResponseEntity.ok(savedMatch);
    }
}
