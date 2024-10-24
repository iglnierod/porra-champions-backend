package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.MatchdayDTO;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Matchday;
import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.model.User;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.MatchdayRepository;
import com.iglnierod.porra_champions.repository.PredictionRepository;
import com.iglnierod.porra_champions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matchdays")
public class MatchdayController {
    @Autowired
    private MatchdayRepository matchdayRepository;

    @Autowired
    private MatchRepository matchrepository;

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Matchday> getMatchdays() {
        return matchdayRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @PostMapping
    public ResponseEntity<Matchday> createMatchday(@RequestBody MatchdayDTO matchdayDTO) {
        Matchday matchday = new Matchday();
        matchday.setName(matchdayDTO.getName());
        matchday.setStatus(matchdayDTO.getStatus());

        Matchday savedMatchday = matchdayRepository.save(matchday);
        return ResponseEntity.ok(savedMatchday);
    }

    public void updateUserPointsForMatchday(Matchday matchday) {
        List<Match> matches = matchrepository.findByMatchday(matchday);

        for (Match match : matches) {
            List<Prediction> predictions = predictionRepository.findByMatch_Id(match.getId());
            int matchResult = checkMatchWinner(match.getLocalGoals(), match.getAwayGoals());
            for (Prediction prediction : predictions) {
                User user = getUser(match, prediction, matchResult);
                userRepository.save(user);
            }
        }
    }

    private User getUser(Match match, Prediction prediction, int matchResult) {
        int predictionResult = checkMatchWinner(prediction.getGoalsLocalPrediction(), prediction.getGoalsAwayPrediction());
        int points = 0;
        if (match.getLocalGoals() == prediction.getGoalsLocalPrediction() && match.getAwayGoals() == prediction.getGoalsAwayPrediction()) {
            points = 3;
        } else if (matchResult == predictionResult) {
            points = 1;
        }

        User user = prediction.getUser();
        user.setPoints(user.getPoints() + points);
        return user;
    }

    private int checkMatchWinner(int localGoals, int awayGoals) {
        if (localGoals == awayGoals) return 0; // Empate
        if (localGoals > awayGoals) return 1; // Gana equipo local
        else return 2; // Gana equipo visitante
    }
}
