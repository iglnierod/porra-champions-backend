package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.dto.MatchDTO;
import com.iglnierod.porra_champions.model.Match;
import com.iglnierod.porra_champions.model.Team;
import com.iglnierod.porra_champions.repository.MatchRepository;
import com.iglnierod.porra_champions.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public List<Match> getAllMatches() {
//        return matchRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return matchRepository.findAll();
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
