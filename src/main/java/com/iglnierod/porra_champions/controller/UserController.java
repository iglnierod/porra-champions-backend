package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.model.Matchday;
import com.iglnierod.porra_champions.model.User;
import com.iglnierod.porra_champions.repository.MatchdayRepository;
import com.iglnierod.porra_champions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchdayRepository matchdayRepository;

    @Autowired
    private MatchdayController matchdayController;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/calculatePoints/{matchdayId}")
    public ResponseEntity<List<User>> calculatePoints(@PathVariable Long matchdayId) {
        Matchday matchday = matchdayRepository.findById(matchdayId)
                .orElseThrow(() -> new RuntimeException("Matchday not found"));

        // Actualiza los puntos para todos los partidos de la jornada
        matchdayController.updateUserPointsForMatchday(matchday);

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"points"));

        return ResponseEntity.ok(users);
    }
}
