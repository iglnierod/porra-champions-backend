package com.iglnierod.porra_champions.controller;

import com.iglnierod.porra_champions.model.Team;
import com.iglnierod.porra_champions.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
  @Autowired
  private TeamRepository teamRepository;

  @GetMapping
  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }
}
