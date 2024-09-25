package com.iglnierod.porra_champions.repository;

import com.iglnierod.porra_champions.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
