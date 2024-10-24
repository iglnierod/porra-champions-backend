package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.MatchdayDTO;
import com.iglnierod.porra_champions.model.Matchday;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.MatchdayRepository;
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

    @GetMapping
    public List<Matchday> getMatchdays() {
        return matchdayRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @PostMapping
    public ResponseEntity<Matchday> createMatchday(@RequestBody MatchdayDTO matchdayDTO) {
        Matchday matchday = new Matchday();
        matchday.setName(matchdayDTO.getName());
        matchday.setStatus(matchdayDTO.getStatus());

        Matchday savedMatchday = matchdayRepository.save(matchday);
        return ResponseEntity.ok(savedMatchday);
    }
}
